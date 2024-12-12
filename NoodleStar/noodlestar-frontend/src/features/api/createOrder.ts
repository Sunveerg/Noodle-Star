import { AxiosResponse } from 'axios';
import axiosInstance from "../../Shared/Api/axiosInstance";
import { OrderResponseModel } from "../model/orderResponseModel";
import {OrderRequestModel} from "@/features/model/orderRequestModel.ts";

export const createOrder = async (order: OrderRequestModel): Promise<AxiosResponse<OrderResponseModel>> => {
    try {
        const response = await axiosInstance.post<OrderResponseModel>('/api/v1/orders', order);
        return response;
    } catch (error) {
        throw new Error(`Failed to create order: ${error}`);
    }
};