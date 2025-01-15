import axios from 'axios';

export const deleteMenuItem = async (menuId: number): Promise<void> => {
  try {
    const url = `http://localhost:8080/api/v1/menu/${menuId}`;
    await axios.delete(url);
  } catch (error) {
    throw new Error(
      `Failed to delete menu item with id ${menuId}: ${error.message}`
    );
  }
};
