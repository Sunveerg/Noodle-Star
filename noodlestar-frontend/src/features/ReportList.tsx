/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useEffect, useState, useRef } from 'react';
import { ReportResponseModel } from './model/reportResponseModel';
import { ReportRequestModel } from './model/reportRequestModel';
import { getAllReports } from './api/getAllReports';
import { getAllmenu } from './api/getAllMenu'; // Import the API function to get the menu
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import Papa from 'papaparse';
import './Report.css';

const ReportList: React.FC = () => {
  const [reports, setReports] = useState<ReportResponseModel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [menuItems, setMenuItems] = useState<{ [key: string]: string }>({});
  const reportRef = useRef<HTMLDivElement>(null); // Reference for PDF export

  useEffect(() => {
    const fetchData = async (): Promise<void> => {
      const requestPayload: ReportRequestModel = {
        reportType: '',
        menuItemName: '',
        itemCount: 0,
      };

      try {
        // Fetch reports
        const reportResponse = await getAllReports(requestPayload);

        // Fetch menu items
        const menuResponse = await getAllmenu();

        // Map menuId to menuName
        const menuItemsMap = menuResponse.reduce(
          (acc: { [key: string]: string }, item) => {
            acc[String(item.menuId)] = item.name;
            return acc;
          },
          {}
        );
        setMenuItems(menuItemsMap);

        const seenMenuItems = new Set<string>();
        const filteredReports = (
          Array.isArray(reportResponse.data)
            ? reportResponse.data
            : [reportResponse.data]
        )
          .sort((a, b) => b.itemCount - a.itemCount)
          .filter(report => {
            if (seenMenuItems.has(report.menuItemName)) {
              return false;
            }
            seenMenuItems.add(report.menuItemName);
            return true;
          });

        const reportsWithMenuNames = filteredReports.map(report => ({
          ...report,
          menuName:
            menuItemsMap[String(report.menuItemName)] || 'Unknown Menu Item',
        }));

        setReports(reportsWithMenuNames);
      } catch (err) {
        setError('Failed to fetch reports');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();

    return () => {
      setReports([]);
    };
  }, []);

  // Export Report as PDF
  const exportToPDF = () => {
    if (!reports.length || !reportRef.current) return;

    html2canvas(reportRef.current, { scale: 2 }).then(canvas => {
      const imgData = canvas.toDataURL('image/png');
      const pdf = new jsPDF('p', 'mm', 'a4');
      const imgWidth = 190;
      const imgHeight = (canvas.height * imgWidth) / canvas.width;

      pdf.text('Menu Item Order Frequency Report', 15, 10);
      pdf.addImage(imgData, 'PNG', 10, 20, imgWidth, imgHeight);
      pdf.save('Order_Frequency_Report.pdf');
    });
  };

  // Export Report as CSV
  const exportToCSV = () => {
    if (!reports.length) return;

    // eslint-disable-next-line import/no-named-as-default-member
    const csv = Papa.unparse(
      reports.map(report => ({
        'Report Type': report.reportType,
        'Menu Item': menuItems[String(report.menuItemName)] || 'Unknown',
        'Order Count': report.itemCount,
      }))
    );

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.setAttribute('download', 'Order_Frequency_Report.csv');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  if (loading) return <p className="loading-text">Loading reports...</p>;
  if (error) return <p className="error-text">{error}</p>;

  return (
    <div className="report-container">
      <h2 className="report-header">Menu Item Order Frequency Report</h2>

      {reports.length === 0 ? (
        <p className="no-reports">No reports available.</p>
      ) : (
        <div ref={reportRef}>
          <table className="report-table">
            <thead>
              <tr>
                <th>Type</th>
                <th>Menu Item</th>
                <th>Order Count</th>
              </tr>
            </thead>
            <tbody>
              {reports.map((report, index) => (
                <tr key={index}>
                  <td>{report.reportType}</td>
                  <td>
                    {menuItems[String(report.menuItemName)] ||
                      'Unknown Menu Item'}
                  </td>
                  <td>{report.itemCount}</td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Export Buttons */}
          <div className="export-buttons">
            <button onClick={exportToPDF}>Export as PDF</button>
            <button onClick={exportToCSV}>Export as CSV</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ReportList;
