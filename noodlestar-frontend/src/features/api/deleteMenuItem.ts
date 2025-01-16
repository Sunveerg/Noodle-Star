import axios from 'axios';

export const deleteMenuItem = async (menuId: number): Promise<void> => {
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  try {
    const url = `${backendUrl}/api/v1/menu/${menuId}`;
    await axios.delete(url);
  } catch (error: unknown) {
    if (error instanceof Error) {
      throw new Error(
        `Failed to delete menu item with id ${menuId}: ${error.message}`
      );
    } else {
      throw new Error(
        `Failed to delete menu item with id ${menuId}: Unknown error`
      );
    }
  }
};
