import { AxiosResponse } from 'axios';
import {menuRequestModel} from "@/features/model/menuRequestModel.ts";  // Path to the Status model
import axiosInstance from "../../Shared/Api/axiosInstance";  // Adjust the relative path

// Define the structure for the dish you want to add
export const addDish = async (dish: menuRequestModel): Promise<AxiosResponse<void>> => {
    try {
        // Send a POST request to add a new dish to the menu
        const response = await axiosInstance.post<void>('/menu/dishes', dish);
        return response;
    } catch (error) {
        // Handle any errors that occur during the request
        throw new Error(`Failed to add dish: ${error}`);
    }
};
