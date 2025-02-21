export interface DailyOrderReportResponseModel {
  reportId: string;
  reportType: string;
  menuItemName: string; // This will store the order date as a string (e.g., "2024-02-20")
  itemCount: number;
  generatedAt: string; // Timestamp of when the report was generated
}
