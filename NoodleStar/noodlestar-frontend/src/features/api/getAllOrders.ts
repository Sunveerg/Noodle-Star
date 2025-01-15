import axiosInstance from '../../Shared/Api/axiosInstance';
import { OrderResponseModel } from '../model/orderResponseModel';

export const getAllOrders = async (): Promise<OrderResponseModel[]> => {
  const response =
    await axiosInstance.get<OrderResponseModel[]>('/api/v1/orders');
  return response.data;
};
