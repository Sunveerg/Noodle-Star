import { AxiosResponse } from 'axios';
import { ReportRequestModel } from '../model/reportRequestModel';
import { ReportResponseModel } from '../model/reportResponseModel';
import axiosInstance from '../../Shared/Api/axiosInstance';

export const getAllReports = async (
  order: ReportRequestModel
): Promise<AxiosResponse<ReportResponseModel>> => {
  try {
    const response = await axiosInstance.post<ReportResponseModel>(
      'api/reports/generate',
      order
    );
    return response;
  } catch (error) {
    throw new Error(`Failed to create order: ${error}`);
  }
};
