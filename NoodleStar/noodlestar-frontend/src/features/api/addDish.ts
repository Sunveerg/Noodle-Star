import { AxiosResponse } from 'axios';
import { menuRequestModel } from '@/features/model/menuRequestModel';
import axiosInstance from '../../Shared/Api/axiosInstance';

export const addDish = async (
  dish: menuRequestModel
): Promise<AxiosResponse<void>> => {
  try {
    const response = await axiosInstance.post<void>('/menu/dishes', dish);
    return response;
  } catch (error) {
    throw new Error(`Failed to add dish: ${error}`);
  }
};
