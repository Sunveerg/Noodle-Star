import React, { useEffect, useState } from 'react';
import '../components/css/Orders.css'; // Adjust the path for your styles
import noodleImg from '../components/assets/noodle.png';
import { OrderResponseModel } from '../features/model/orderResponseModel';
import { getAllOrders } from '../features/api/getAllOrders';
import { updateOrderStatus } from '../features/api/getOrderById';

const Orders: React.FC = () => {
  const [orders, setOrders] = useState<OrderResponseModel[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');

  useEffect(() => {
    const fetchOrders = async (): Promise<void> => {
      try {
        const fetchedOrders = await getAllOrders();
        setOrders(fetchedOrders);
      } catch (err) {
        console.error('Error fetching orders:', err);
        setError('Failed to fetch orders.');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  const handleStatusChange = async (
    orderId: string,
    newStatus: string
  ): Promise<void> => {
    try {
      const updatedOrder = await updateOrderStatus(orderId, newStatus);
      // Update the status of the order in the local state
      setOrders(prevOrders =>
        prevOrders.map(order =>
          order.orderId === orderId
            ? { ...order, status: updatedOrder.status }
            : order
        )
      );
      alert('Order status updated successfully!');
    } catch (err) {
      console.error('Error updating order status:', err);
      alert('Failed to update order status.');
    }
  };

  if (loading) {
    return <div className="loading-message">Loading...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="orders-page">
      <h2 className="pageTitle">
        Orders
        <img src={noodleImg} alt="Noodle" className="logo-img" />
      </h2>
      <div className="cloud-container">
        <div className="cloud4"></div>
        <div className="cloud5"></div>
        <div className="cloud6"></div>
      </div>
      <div className="topRightImage"></div>
      <div className="order-history-container">
        <h2 className="order-history-title">Order History</h2>
        {orders.length === 0 ? (
          <p className="order-history-empty">No orders found.</p>
        ) : (
          <ul className="order-history-list">
            {orders.map(order => (
              <li key={order.orderId} className="order-item">
                <h3 className="order-id">Order ID: {order.orderId}</h3>
                <p className="order-status">Status: {order.status}</p>
                <p className="order-date">Date: {order.orderDate}</p>
                <p className="order-total">
                  Total: $
                  {order.total && !isNaN(order.total)
                    ? order.total.toFixed(2)
                    : 'N/A'}
                </p>

                {/* Dropdown for status */}
                <div className="order-status-dropdown">
                  <label htmlFor={`status-${order.orderId}`}>
                    Update Status:{' '}
                  </label>
                  <select
                    id={`status-${order.orderId}`}
                    value={order.status}
                    onChange={e =>
                      handleStatusChange(order.orderId, e.target.value)
                    }
                  >
                    <option value="PENDING">PENDING</option>
                    <option value="CANCELED">CANCELED</option>
                    <option value="COLLECTED">COLLECTED</option>
                  </select>
                </div>

                <h4 className="order-details-title">Order Details:</h4>
                <ul className="order-details-list">
                  {order.orderDetails.map(detail => (
                    <li key={detail.menuId} className="order-detail-item">
                      <p className="order-detail-dish-name">
                        {detail.dishName}
                      </p>
                      <p className="order-detail-quantity">
                        Quantity: {detail.quantity}
                      </p>
                      <p className="order-detail-price">
                        Price: $
                        {detail.price && !isNaN(detail.price)
                          ? detail.price.toFixed(2)
                          : 'N/A'}
                      </p>
                    </li>
                  ))}
                </ul>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Orders;
