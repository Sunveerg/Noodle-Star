import axiosInstance from '../../Shared/Api/axiosInstance';
import { UserRequestModel } from '@/features/model/userRequestModel';
import { UserResponseModel } from '@/features/model/userResponseModel';

export const updateUser = async (
  userId: string,
  user: UserRequestModel
): Promise<void> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  await axiosInstance.put<void>(`${backendUrl}/api/v1/users/${userId}`, user);
};

export const getUserById = async (
  userId: string
): Promise<UserResponseModel> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  const response = await axiosInstance.get<UserResponseModel>(
    `${backendUrl}/api/v1/users/${userId}`
  );
  return response.data;
};
