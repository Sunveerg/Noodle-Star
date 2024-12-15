import axiosInstance from "../../Shared/Api/axiosInstance";

export const cancelOrder = async (orderId: string): Promise<void> => {
    try {
        const url = `/orders/${orderId}`;
        await axiosInstance.delete(url);
    } catch (error) {
        throw new Error(`Failed to cancel order with ID ${orderId}: ${error.message}`);
    }
};
