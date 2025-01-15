
import axiosInstance from "../../Shared/Api/axiosInstance";  // Adjust the relative path
import { menuResponseModel } from "../model/menuResponseModel";


export const getAllmenu = async (): Promise<menuResponseModel[]> => {
  // Use menuResponseModel[] directly in the get call
  const response = await axiosInstance.get<menuResponseModel[]>(
      'http://localhost:8080/api/v1/menu'
  );
  return response.data;
};
