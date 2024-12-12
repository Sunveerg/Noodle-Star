export interface OrderDetailsResponseModel {
    menuId: string;
    quantity: number;
    price: number;
}

export interface OrderResponseModel {
    orderId: string;
    customerId: string;
    status: string;
    orderDate: string;
    orderDetails: OrderDetailsResponseModel[];
    total: number;
}