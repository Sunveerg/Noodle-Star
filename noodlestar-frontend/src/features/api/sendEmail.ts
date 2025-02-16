import { AxiosResponse } from 'axios';
import { emailRequestModel } from '@/features/model/emailRequestModel';
import axiosInstance from '../../Shared/Api/axiosInstance';

export const sendEmail = async (
  emailRequest: emailRequestModel
): Promise<AxiosResponse<void>> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  try {
    return await axiosInstance.post<void>(
      `${backendUrl}/api/v1/sendEmail`,
      emailRequest
    );
  } catch (error) {
    throw new Error(`Failed to send email: ${error}`);
  }
};
