/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useState } from 'react';
import { getFinancialReport } from '../features/api/financialReports';
import { FinancialReportResponseModel } from '../features/model/financialReportModels';
import './Report.css';

const FinancialReport: React.FC = () => {
  const [report, setReport] = useState<FinancialReportResponseModel | null>(
    null
  );
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleGenerateReport = async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await getFinancialReport({
        reportType: 'Financial Report',
        menuItemName: 'Total Revenue',
        itemCount: 0, // Placeholder value
        generatedAt: new Date().toISOString(),
      });

      setReport(response.data);
    } catch (err) {
      setError('Failed to fetch financial report');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="report-container">
      <h2 className="report-header">Financial Report</h2>
      <button className="report-button" onClick={handleGenerateReport}>
        Generate Report
      </button>

      {loading && <p>Loading...</p>}
      {error && <p className="error-message">{error}</p>}

      {report && (
        <div className="report-content">
          <p>
            <strong>Report Type:</strong> {report.reportType}
          </p>
          <p>
            <strong>Report Name:</strong> {report.menuItemName}
          </p>
          <p>
            <strong>$$$:</strong> {report.itemCount}
          </p>
          <p>
            <strong>Generated At:</strong>{' '}
            {new Date(report.generatedAt).toLocaleString()}
          </p>
        </div>
      )}
    </div>
  );
};

export default FinancialReport;
