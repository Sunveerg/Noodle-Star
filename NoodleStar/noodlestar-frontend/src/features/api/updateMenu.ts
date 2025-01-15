import axiosInstance from '../../Shared/Api/axiosInstance.ts';
import { menuRequestModel } from '@/features/model/menuRequestModel.ts';
import { menuResponseModel } from '@/features/model/menuResponseModel.ts';

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
