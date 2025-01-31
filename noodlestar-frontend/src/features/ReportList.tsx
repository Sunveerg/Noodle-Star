import React, { useEffect, useState } from 'react';
import { ReportResponseModel } from './model/reportResponseModel';
import { ReportRequestModel } from './model/reportRequestModel';
import { getAllReports } from './api/getAllReports';
import { getAllmenu } from './api/getAllMenu'; // Import the API function to get the menu
import './Report.css';

const ReportList: React.FC = () => {
  const [reports, setReports] = useState<ReportResponseModel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [menuItems, setMenuItems] = useState<{ [key: string]: string }>({}); // { menuId: menuName }

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

        // Fetch menu items (menuResponseModel)
        const menuResponse = await getAllmenu(); // Assuming this API returns an array of { menuId, name }

        // Map menuId to menuName
        const menuItemsMap = menuResponse.reduce(
          (acc: { [key: string]: string }, item) => {
            acc[String(item.menuId)] = item.name; // Map menuId to menuName
            return acc;
          },
          {}
        );
        setMenuItems(menuItemsMap);

        // To track unique menu items
        const seenMenuItems = new Set<string>();

        const filteredReports = (
          Array.isArray(reportResponse.data)
            ? reportResponse.data
            : [reportResponse.data]
        )
          .sort((a, b) => b.itemCount - a.itemCount)
          .filter(report => {
            // If menu item is already seen, filter it out, else add it to the seen set
            if (seenMenuItems.has(report.menuItemName)) {
              return false; // Skip duplicate
            }
            seenMenuItems.add(report.menuItemName); // Mark as seen
            return true; // Keep the first occurrence
          });

        // Update reports and display menu names
        const reportsWithMenuNames = filteredReports.map(report => ({
          ...report,
          menuName:
            menuItemsMap[String(report.menuItemName)] || 'Unknown Menu Item', // Find the menu name by menuItemName
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

    // Cleanup function to clear the state on unmount
    return () => {
      setReports([]);
    };
  }, []); // Empty dependency array ensures this effect runs only once when the component mounts

  if (loading) return <p className="loading-text">Loading reports...</p>;
  if (error) return <p className="error-text">{error}</p>;

  return (
    <div className="report-container">
      <h2 className="report-header">Menu Item Order Frequency Report</h2>
      {reports.length === 0 ? (
        <p className="no-reports">No reports available.</p>
      ) : (
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
      )}
    </div>
  );
};

export default ReportList;
