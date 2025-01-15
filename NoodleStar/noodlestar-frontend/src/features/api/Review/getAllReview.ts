import { reviewResponseModel } from '../../../features/model/reviewResponseModel';
import axiosInstance from '../../../Shared/Api/axiosInstance';

export const getAllReview = async (): Promise<reviewResponseModel[]> => {
  // Use menuResponseModel[] directly in the get call
  const response = await axiosInstance.get<reviewResponseModel[]>(
    'http://localhost:8080/api/v1/review'
  );
  return response.data;
};
