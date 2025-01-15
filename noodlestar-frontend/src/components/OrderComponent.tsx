/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useState } from 'react';
import { createOrder } from '../features/api/createOrder';
import { OrderResponseModel } from '../features/model/orderResponseModel';
import { OrderRequestModel } from '../features/model/orderRequestModel';

const OrderComponent: React.FC = () => {
  const [orders, setOrders] = useState<OrderResponseModel[]>([]);
  const [newOrder, setNewOrder] = useState<OrderRequestModel>({
    customerId: '',
    orderDetails: [],
  });

  const handleCreateOrder = async () => {
    setNewOrder(prev => ({
      ...prev,
      orderDetails: [
        { menuId: 'someMenuId', quantity: 2 },
        ...prev.orderDetails,
      ],
    }));

    try {
      const response = await createOrder(newOrder);

      if (response.data) {
        setOrders([...orders, response.data]);
      } else {
        console.error('Order creation failed, no data received');
      }
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
