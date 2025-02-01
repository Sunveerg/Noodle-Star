/* eslint-disable prettier/prettier */
export interface FinancialReportRequestModel {
    reportType: string;
    menuItemName: string;
    itemCount: number;
    generatedAt: string;
  }
  
  export interface FinancialReportResponseModel {
    reportId: string;
    reportType: string;
    menuItemName: string; // ✅ Matches backend field
    itemCount: number;    // ✅ Matches backend field (formerly totalRevenue)
    generatedAt: string;
  }
  