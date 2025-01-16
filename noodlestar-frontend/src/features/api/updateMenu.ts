import axiosInstance from '../../Shared/Api/axiosInstance';
import { menuRequestModel } from '@/features/model/menuRequestModel';
import { menuResponseModel } from '@/features/model/menuResponseModel';

export const updateMenu = async (
  menuId: string,
  menu: menuRequestModel
): Promise<void> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  await axiosInstance.put<void>(`${backendUrl}/api/v1/menu/${menuId}`, menu);
};

export const getMenu = async (menuId: string): Promise<menuResponseModel> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  const response = await axiosInstance.get<menuResponseModel>(
    `${backendUrl}/api/v1/menu/${menuId}`
  );
  return response.data;
};
