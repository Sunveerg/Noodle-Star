import { AxiosResponse } from 'axios';
import { emailRequestModel } from '@/features/model/emailRequestModel';
import axiosInstance from '../../Shared/Api/axiosInstance';

export const sendEmail = async (
  emailRequest: emailRequestModel
): Promise<AxiosResponse<void>> => {
  try {
    return await axiosInstance.post<void>('/api/v1/sendEmail', emailRequest);
  } catch (error) {
    throw new Error(`Failed to send email: ${error}`);
  }
};
