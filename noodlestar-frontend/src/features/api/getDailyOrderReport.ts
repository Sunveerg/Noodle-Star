/* eslint-disable no-console */
/* eslint-disable prettier/prettier */
import { AxiosResponse } from 'axios';
import axiosInstance from '../../Shared/Api/axiosInstance';
import { DailyOrderReportResponseModel } from '../model/dailyOrderReportModels';

export const getDailyOrderReport = async (): Promise<AxiosResponse<DailyOrderReportResponseModel[]>> => {
  try {
    console.log("Sending request to fetch daily order report...");
    const response = await axiosInstance.post<DailyOrderReportResponseModel[]>(
      'api/reports/generate/daily-orders' // ✅ Ensure this matches backend route
    );
    console.log("Received daily order report:", response.data);
    return response;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    console.error("Error fetching daily order report:", error.response?.data || error.message);
    throw new Error(`Failed to generate daily order report: ${error}`);
  }
};
