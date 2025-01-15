import axiosInstance from '../../Shared/Api/axiosInstance';
import { OrderResponseModel } from '../model/orderResponseModel';

export const getOrderById = async (
  orderId: number
): Promise<OrderResponseModel> => {
  const response = await axiosInstance.get<OrderResponseModel>(
    `/api/v1/orders/${orderId}`
  );
  return response.data;
};
