import { menuResponseModel } from '@/features/model/menuResponseModel.ts';

export const getMenuItemById = async (
  menuId: string
): Promise<menuResponseModel> => {
  const response = await fetch(`http://localhost:8080/api/v1/menu/${menuId}`);
  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`);
  }
  return response.json();
};
