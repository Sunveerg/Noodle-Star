import React, { useEffect, useState } from 'react';
import { getAllOrders } from '../features/api/getAllOrders.ts';
import { createOrder } from '../features/api/createOrder.ts';
import { OrderResponseModel } from '../features/model/orderResponseModel';
import {OrderRequestModel} from "../features/model/orderRequestModel.ts";

const OrderComponent: React.FC = () => {
    const [orders, setOrders] = useState<OrderResponseModel[]>([]);
    const [newOrder, setNewOrder] = useState<OrderRequestModel>({
        customerId: '',
        orderDetails: []
    });

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const orders = await getAllOrders();
                setOrders(orders);
            } catch (error) {
                console.error('Failed to fetch orders:', error);
            }
        };

        fetchOrders();
    }, []);

    const handleCreateOrder = async () => {
        try {
            const response = await createOrder(newOrder);
            setOrders([...orders, response.data]);
        } catch (error) {
            console.error('Failed to create order:', error);
        }
    };

    return (
        <div>
            <h1>Orders</h1>
            <ul>
                {orders.map(order => (
                    <li key={order.orderId}>
                        {order.customerId} - {order.total}
                    </li>
                ))}
            </ul>
            <button onClick={handleCreateOrder}>Create Order</button>
        </div>
    );
};

export default OrderComponent;