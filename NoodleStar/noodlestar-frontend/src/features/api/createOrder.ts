import { AxiosResponse } from 'axios';
import axiosInstance from '../../Shared/Api/axiosInstance';
import { OrderRequestModel } from '@/features/model/orderRequestModel.ts';

export const createOrder = async (
  order: OrderRequestModel
): Promise<AxiosResponse<void>> => {
  try {
    const response = await axiosInstance.post<void>('/orders', order);
    return response;
  } catch (error) {
    throw new Error(`Failed to create order: ${error}`);
  }
};
