/* eslint-disable @typescript-eslint/explicit-function-return-type */
/* eslint-disable no-console */
/* eslint-disable prettier/prettier */
import React, { useState, useRef } from 'react';
import { getDailyOrderReport } from '../features/api/getDailyOrderReport';
import { DailyOrderReportResponseModel } from '../features/model/dailyOrderReportModels';
import {
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  BarChart,
  Bar,
  ResponsiveContainer,
} from 'recharts';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import Papa from 'papaparse';
import './Report.css';

const DailyOrderReport: React.FC = () => {
  const [report, setReport] = useState<DailyOrderReportResponseModel[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');
  const [filteredReport, setFilteredReport] = useState<DailyOrderReportResponseModel[]>([]);
  const reportRef = useRef<HTMLDivElement>(null);

  const handleGenerateReport = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await getDailyOrderReport();
      setReport(response.data);
      setFilteredReport(response.data); // Default to full report
    } catch (err) {
      setError('Failed to fetch daily order report');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterReport = () => {
    if (!report.length) {
      setError('No report available to filter');
      return;
    }

    const start = new Date(startDate);
    const end = new Date(endDate);

    const filteredData = report.filter((entry) => {
      const entryDate = new Date(entry.generatedAt);
      return entryDate >= start && entryDate <= end;
    });

    setFilteredReport(filteredData);
  };

  const handleResetFilter = () => {
    setStartDate('');
    setEndDate('');
    setFilteredReport(report); // Reset to original report
  };

  const chartData = filteredReport.map((entry) => ({
    name: entry.menuItemName,
    orders: entry.itemCount,
  }));

  // 📜 Export Report as PDF
  const handleDownloadPDF = async () => {
    if (!reportRef.current) return;

    const canvas = await html2canvas(reportRef.current);
    const imgData = canvas.toDataURL('image/png');

    const pdf = new jsPDF();
    pdf.text('Daily Order Report', 10, 10);
    pdf.addImage(imgData, 'PNG', 10, 20, 180, 100);
    pdf.save('DailyOrderReport.pdf');
  };

  // 📄 Export Report as CSV
  const handleDownloadCSV = () => {
    if (!filteredReport.length) return;

    const csvData = filteredReport.map(({ menuItemName, itemCount, generatedAt }) => ({
      Name: menuItemName,
      Orders: itemCount,
      Date: new Date(generatedAt).toLocaleDateString(),
    }));

    // eslint-disable-next-line import/no-named-as-default-member
    const csv = Papa.unparse(csvData);
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.setAttribute('download', 'DailyOrderReport.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="report-container">
      <h2 className="report-header">Daily Order Report</h2>
      <button className="report-button" onClick={handleGenerateReport} disabled={loading}>
        Generate Report
      </button>

      {loading && <p>Loading...</p>}
      {error && <p className="error-message">{error}</p>}

      <div className="filter-controls">
        <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
        <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
        <button onClick={handleFilterReport} disabled={!startDate || !endDate}>Filter</button>
        <button onClick={handleResetFilter} disabled={!report.length}>Reset</button>
      </div>

      {filteredReport.length > 0 ? (
        <div ref={reportRef} className="report-content">
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="orders" fill="#82ca9d" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      ) : (
        <p>No orders found in the selected range.</p>
      )}

      {/* Export Buttons */}
      <div className="export-buttons">
        <button onClick={handleDownloadPDF} disabled={!filteredReport.length}>Download PDF</button>
        <button onClick={handleDownloadCSV} disabled={!filteredReport.length}>Download CSV</button>
      </div>
    </div>
  );
};

export default DailyOrderReport;
