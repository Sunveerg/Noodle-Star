import axiosInstance from '../../Shared/Api/axiosInstance';
import { OrderResponseModel } from '../model/orderResponseModel';

export const getAllOrders = async (): Promise<OrderResponseModel[]> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  const response = await axiosInstance.get<OrderResponseModel[]>(
    `${backendUrl}/api/v1/orders`
  );
  return response.data;
};
