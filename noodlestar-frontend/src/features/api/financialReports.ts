/* eslint-disable no-console */
/* eslint-disable prettier/prettier */
import { AxiosResponse } from 'axios';
import axiosInstance from '../../Shared/Api/axiosInstance';
import {
  FinancialReportRequestModel,
  FinancialReportResponseModel,
} from '../model/financialReportModels';

export const getFinancialReport = async (
  reportRequest: FinancialReportRequestModel
): Promise<AxiosResponse<FinancialReportResponseModel>> => {
  try {
      console.log("Sending request:", reportRequest);
      const response = await axiosInstance.post<FinancialReportResponseModel>(
        'api/reports/generate/financial', // ✅ Make sure this is correct
        reportRequest
      );
      console.log("Received response:", response.data);
      return response;
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    } catch (error: any) {
      console.error("Error fetching financial report:", error.response?.data || error.message);
      throw new Error(`Failed to generate financial report: ${error}`);
    }
  };
  