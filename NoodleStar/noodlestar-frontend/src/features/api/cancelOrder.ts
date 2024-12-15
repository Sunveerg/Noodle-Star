import axios from "axios";

export const cancelOrder = async (orderId: string): Promise<void> => {
    if (!orderId) {
        throw new Error("Order ID is required to cancel an order.");
    }

    try {
        const url = `http://localhost:8080/api/v1/orders/${orderId}`;
        await axios.delete(url);
    } catch (error: any) {
        console.error(`Error occurred while canceling order: ${error}`);
        throw new Error(`Failed to cancel order with ID ${orderId}: ${error.response?.data?.message || error.message}`);
    }
};
