import { reviewResponseModel } from '../../../features/model/reviewResponseModel';
import axiosInstance from '../../../Shared/Api/axiosInstance';

export const getReviewByUserId = async (
  userId: string
): Promise<reviewResponseModel[]> => {
  if (!userId) {
    throw new Error('User ID is required');
  }

  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  if (!backendUrl) {
    throw new Error('Backend URL is not set in environment variables');
  }

  // Make the API request with the userId
  const response = await axiosInstance.get<reviewResponseModel[]>(
    `${backendUrl}/api/v1/review/${userId}`
  );

  return response.data;
};
