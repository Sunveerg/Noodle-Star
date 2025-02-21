/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useState, useRef } from 'react';
import { getFinancialReport } from '../features/api/financialReports';
import { FinancialReportResponseModel } from '../features/model/financialReportModels';
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

const FinancialReport: React.FC = () => {
  const [report, setReport] = useState<FinancialReportResponseModel | null>(
    null
  );
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');
  const [filteredReport, setFilteredReport] =
    useState<FinancialReportResponseModel | null>(null);
  const reportRef = useRef<HTMLDivElement>(null); // Reference for PDF export

  const handleGenerateReport = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await getFinancialReport({
        reportType: 'Financial Report',
        menuItemName: 'Total Revenue',
        itemCount: 0, // Placeholder value
        generatedAt: new Date().toLocaleString('en-CA', {
          weekday: 'long',
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
          timeZone: 'America/Toronto',
        }),
      });

      setReport(response.data);
      setFilteredReport(response.data);
    } catch (err) {
      setError('Failed to fetch financial report');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterReport = () => {
    if (!report) {
      setError('No report available to filter');
      return;
    }
    const reportDate = new Date(report.generatedAt);
    const start = new Date(startDate);
    const end = new Date(endDate);

    if (start <= reportDate && reportDate <= end) {
      setFilteredReport(report);
    } else {
      setFilteredReport(null);
    }
  };

  const handleResetFilter = () => {
    setStartDate('');
    setEndDate('');
    setFilteredReport(report);
  };

  // Transform data for the chart
  const chartData = filteredReport
    ? [
        {
          name: 'Revenue',
          value: filteredReport.itemCount,
        },
      ]
    : [];

  // Export Report as PDF (Using html2canvas)
  const exportToPDF = () => {
    if (!filteredReport || !reportRef.current) return;

    html2canvas(reportRef.current, { scale: 2 }).then(canvas => {
      const imgData = canvas.toDataURL('image/png');
      const pdf = new jsPDF('p', 'mm', 'a4');
      const imgWidth = 190; // Adjust width to fit
      const imgHeight = (canvas.height * imgWidth) / canvas.width;

      pdf.text('Financial Report', 15, 10);
      pdf.addImage(imgData, 'PNG', 10, 20, imgWidth, imgHeight);
      pdf.save('Financial_Report.pdf');
    });
  };

  // Export Report as CSV
  const exportToCSV = () => {
    if (!filteredReport) return;

    // eslint-disable-next-line import/no-named-as-default-member
    const csv = Papa.unparse([
      {
        'Report Type': filteredReport.reportType,
        'Menu Item': filteredReport.menuItemName,
        'Item Count': filteredReport.itemCount,
        'Generated At': filteredReport.generatedAt,
      },
    ]);

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.setAttribute('download', 'Financial_Report.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div className="report-container">
      <h2 className="report-header">Financial Report</h2>
      <button
        className="report-button"
        onClick={handleGenerateReport}
        disabled={loading}
      >
        Generate Report
      </button>

      {loading && <p>Loading...</p>}
      {error && <p className="error-message">{error}</p>}

      <div className="filter-controls">
        <input
          type="date"
          value={startDate}
          onChange={e => setStartDate(e.target.value)}
          placeholder="Start Date"
        />
        <input
          type="date"
          value={endDate}
          onChange={e => setEndDate(e.target.value)}
          placeholder="End Date"
        />
        <button
          onClick={handleFilterReport}
          disabled={loading || !startDate || !endDate}
        >
          Filter
        </button>
        <button onClick={handleResetFilter} disabled={loading || !report}>
          Reset Filter
        </button>
      </div>

      {filteredReport && (
        <div className="report-content" ref={reportRef}>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart
              data={chartData}
              margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="value" fill="#8884d8" />
            </BarChart>
          </ResponsiveContainer>

          {/* Export Buttons */}
          <div className="export-buttons">
            <button onClick={exportToPDF}>Export as PDF</button>
            <button onClick={exportToCSV}>Export as CSV</button>
          </div>
        </div>
      )}

      {!filteredReport && report && (
        <p>No report available within the selected date range.</p>
      )}
    </div>
  );
};

export default FinancialReport;
