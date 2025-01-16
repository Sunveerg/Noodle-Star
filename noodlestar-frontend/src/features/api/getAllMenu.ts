import axiosInstance from '../../Shared/Api/axiosInstance'; // Adjust the relative path
import { menuResponseModel } from '../model/menuResponseModel';

export const getAllmenu = async (): Promise<menuResponseModel[]> => {
  // Use menuResponseModel[] directly in the get call
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  const response = await axiosInstance.get<menuResponseModel[]>(
    `${backendUrl}/api/v1/menu`
  );
  return response.data;
};
