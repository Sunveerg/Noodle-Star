import axiosInstance from '../../Shared/Api/axiosInstance';
import { menuRequestModel } from '@/features/model/menuRequestModel';
import { menuResponseModel } from '@/features/model/menuResponseModel';

export const updateMenu = async (
  menuId: string,
  menu: menuRequestModel
): Promise<void> => {
  await axiosInstance.put<void>(
    `http://localhost:8080/api/v1/menu/${menuId}`,
    menu
  );
};

export const getMenu = async (menuId: string): Promise<menuResponseModel> => {
  const response = await axiosInstance.get<menuResponseModel>(
    `http://localhost:8080/api/v1/menu/${menuId}`
  );
  return response.data;
};
