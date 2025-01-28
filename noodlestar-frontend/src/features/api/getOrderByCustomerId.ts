import axiosInstance from '../../Shared/Api/axiosInstance';
import { OrderResponseModel } from '../model/orderResponseModel';

export const getOrderByCustomerId = async (
  customerId: string
): Promise<OrderResponseModel[]> => {
  const response = await axiosInstance.get<OrderResponseModel[]>(
    `/api/v1/orders/orderHistory/${customerId}`
  );
  return response.data;
};
