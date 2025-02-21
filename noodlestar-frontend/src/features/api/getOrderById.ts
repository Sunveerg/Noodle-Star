import axiosInstance from '../../Shared/Api/axiosInstance';
import { OrderResponseModel } from '../model/orderResponseModel';

export const getOrderById = async (
  orderId: string
): Promise<OrderResponseModel> => {
  const response = await axiosInstance.get<OrderResponseModel>(
    `/api/v1/orders/${orderId}`
  );
  return response.data;
};

export const updateOrderStatus = async (
  orderId: string,
  newStatus: string
): Promise<OrderResponseModel> => {
  const response = await axiosInstance.patch<OrderResponseModel>(
    `/api/v1/orders/${orderId}/status`,
    null, // You can also send the status in the body, but this depends on your backend's design
    { params: { newStatus } } // Send the newStatus as a query parameter
  );
  return response.data;
};
