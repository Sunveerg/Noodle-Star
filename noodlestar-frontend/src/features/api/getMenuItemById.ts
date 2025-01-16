import { menuResponseModel } from '@/features/model/menuResponseModel';

export const getMenuItemById = async (
  menuId: string
): Promise<menuResponseModel> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  const response = await fetch(`${backendUrl}/api/v1/menu/${menuId}`);
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  return response.json();
};
