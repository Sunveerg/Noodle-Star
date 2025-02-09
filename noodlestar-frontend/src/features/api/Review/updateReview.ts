import axiosInstance from '../../../Shared/Api/axiosInstance';
import { reviewRequestModel } from '../../model/reviewRequestModel';
import { reviewResponseModel } from '../../model/reviewResponseModel';

export const updateReview = async (
  reviewId: string,
  review: reviewRequestModel
): Promise<void> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  await axiosInstance.put<void>(
    `${backendUrl}/api/v1/review/${reviewId}`,
    review
  );
};

export const getReviewById = async (
  reviewId: string
): Promise<reviewResponseModel> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  const response = await axiosInstance.get<reviewResponseModel>(
    `${backendUrl}/api/v1/review/reviewId/${reviewId}`
  );
  return response.data;
};
