import { reviewResponseModel } from '../../../features/model/reviewResponseModel';
import axiosInstance from '../../../Shared/Api/axiosInstance';

export const getAllReview = async (): Promise<reviewResponseModel[]> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  // Use menuResponseModel[] directly in the get call
  const response = await axiosInstance.get<reviewResponseModel[]>(
    `${backendUrl}/api/v1/review`
  );
  return response.data;
};
