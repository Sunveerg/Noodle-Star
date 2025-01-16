import axios from 'axios';
import { menuResponseModel } from '../model/menuResponseModel';

export const getAllmenu = async (): Promise<menuResponseModel[]> => {
  // Axios instance setup with credentials included
  const axiosInstance = axios.create({
    baseURL: process.env.REACT_APP_BACKEND_URL, // Base URL from environment variables
    withCredentials: true, // Ensure credentials are sent with requests
    headers: {
      'Content-Type': 'application/json', // Specify JSON content type
    },
  });

  try {
    // Perform the GET request to fetch menu data
    const response =
      await axiosInstance.get<menuResponseModel[]>('/api/v1/menu');
    return response.data; // Return the response data
  } catch (error) {
    console.error('Error fetching menu data:', error);
    throw error; // Re-throw the error to handle it where the function is called
  }
};
