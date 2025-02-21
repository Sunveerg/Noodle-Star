/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useState } from 'react';
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

  const handleGenerateReport = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await getFinancialReport({
        reportType: 'Financial Report',
        menuItemName: 'Total Revenue',
        itemCount: 0, // Placeholder value
        generatedAt: new Date('Z').toLocaleString('en-CA', {
          weekday: 'long',
          year: 'numeric',
          month: 'long',
          day: 'numeric',
          hour: '2-digit',
          minute: '2-digit',
          timeZone: 'America/Toronto',
        }),
      });

      setReport(response.data); // Assuming response.data has the report data
      setFilteredReport(response.data); // Initially, the filtered report is the same as the generated one
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

    // Check if the report generated date is within the selected range
    if (start <= reportDate && reportDate <= end) {
      setFilteredReport(report); // Show the report if it is within the range
    } else {
      setFilteredReport(null); // Hide the report if it is outside the range
    }
  };

  const handleResetFilter = () => {
    setStartDate('');
    setEndDate('');
    setFilteredReport(report); // Reset to the original report
  };

  // Transform the report data into a format suitable for Recharts
  const chartData = filteredReport
    ? [
        {
          name: 'Revenue',
          value: filteredReport.itemCount,
        },
      ]
    : [];

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
        <div className="report-content">
          {/* Bar Chart to visualize the revenue */}
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
        </div>
      )}

      {!filteredReport && report && (
        <p>No report available within the selected date range.</p>
      )}
    </div>
  );
};

export default FinancialReport;
