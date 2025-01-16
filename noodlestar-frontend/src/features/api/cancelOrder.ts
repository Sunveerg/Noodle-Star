import axios from 'axios';

export const cancelOrder = async (orderId: string): Promise<void> => {
  if (!orderId) {
    throw new Error('Order ID is required to cancel an order.');
  }
  const backendUrl = process.env.REACT_APP_BACKEND_URL;
  try {
    const url = `${backendUrl}/api/v1/orders/${orderId}`;
    await axios.delete(url);
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  } catch (error: any) {
    console.error(`Error occurred while canceling order: ${error}`);
    throw new Error(
      `Failed to cancel order with ID ${orderId}: ${error.response?.data?.message || error.message}`
    );
  }
};
