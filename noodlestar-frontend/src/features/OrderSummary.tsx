/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { cancelOrder } from '../features/api/cancelOrder';
import noodleImg from '../components/assets/noodle.png';
import './OrderSummary.css';
import { sendEmail } from '../features/api/sendEmail';
import { emailRequestModel } from '../features/model/emailRequestModel';

interface CartItem {
  menuId: string;
  name: string;
  price: number;
  quantity: number;
}

const OrderSummary: React.FC = (): JSX.Element => {
  const TAX_RATE = 0.15;
  const location = useLocation();
  const {
    cartItems = [],
    totalPrice = 0,
    orderId = 'orderId2',
  } = location.state || {};
  /* eslint-disable no-console */
  console.log(orderId);

  const taxes = totalPrice * TAX_RATE;
  const grandTotal = totalPrice + taxes;
  const navigate = useNavigate();

  const handleReviewClick = () => {
    const backendUrl = process.env.REACT_APP_BACKEND_URL;
    const portURl = process.env.Port;
    window.location.href = `${backendUrl}: ${portURl}`;
  };

  const handlePickupClick = async () => {
    navigate(`emailSent`);

    const orderedItems = cartItems
      .map(
        (item: CartItem) =>
          `${item.name} x${item.quantity} - $${(item.price * item.quantity).toFixed(2)}`
      )
      .join('<br>');

    const token = localStorage.getItem('access_token');

    let userEmail = 'default@example.com';

    if (token) {
      try {
        const response = await fetch(
          'https://dev-5kbvxb8zgblo1by3.us.auth0.com/userinfo',
          {
            method: 'GET',
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error('Failed to fetch user info');
        }

        const userInfo = await response.json();
        userEmail = userInfo.email || userEmail;
      } catch (error) {
        console.error('Error fetching user info:', error);
      }
    }

    const emailRequest: emailRequestModel = {
      to: userEmail,
      subject: 'Your Pickup Order Confirmation',
      body: `
        <div style="font-family: Arial, sans-serif; background-color: #f4e4c6; padding: 20px; border-radius: 10px; color: #333;">
            <h2 style="color: #8b1e1e;">Thank you for your order!</h2>
            <p style="font-size: 1.2rem;">Your total is <strong style="color: #d9a52e;">$${grandTotal.toFixed(2)}</strong>. 
            Please pick up your order at <strong>1871 Sainte-Catherine</strong> at your selected time.</p>
            
            <h3 style="color: #8b1e1e;">Ordered Item(s):</h3>
            <div style="font-size: 1rem; color: #555;">
                ${orderedItems}
            </div>
            
            <div style="margin-top: 20px; padding: 10px; background-color: #8b1e1e; color: #fff; border-radius: 5px; text-align: center;">
                <p style="margin: 0;">We look forward to serving you!</p>
            </div>
        </div>
      `,
    };

    try {
      await sendEmail(emailRequest);
    } catch (error) {
      console.error('Error sending email:', error);
    }
  };

  const handleCancelOrder = async () => {
    const orderIdToCancel = 'orderId2';

    try {
      await cancelOrder(orderIdToCancel);
      alert('Order canceled successfully!');
      navigate('/menuOrder');
    } catch (error) {
      alert('Failed to cancel the order. Please try again.');
    }
  };

  return (
    <>
      <div className="checkout-section">
        <h1 className="checkout-title">
          Checkout
          <img
            src={noodleImg}
            alt="Noodle"
            style={{ width: '100px', height: '100px' }}
          />
        </h1>
      </div>
      <div className="order-summary-container">
        <div className="order-details-container">
          <div className="order-details">
            <h2 className="order-heading">Order Details</h2>
            <table className="order-table">
              <thead>
                <tr>
                  <th>Product</th>
                  <th>Total</th>
                </tr>
              </thead>
              <tbody>
                {cartItems.map((item: CartItem, index: number) => (
                  <tr key={index}>
                    <td>
                      {item.name} <span>x{item.quantity}</span>
                    </td>
                    <td>${(item.price * item.quantity).toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="order-summary">
              <div className="summary-item">
                <span>Subtotal: ${totalPrice.toFixed(2)}</span>
              </div>
              <div className="summary-item">
                <span>Taxes: ${taxes.toFixed(2)}</span>
              </div>
              <div className="summary-item grand-total">
                <span>Grand Total: ${grandTotal.toFixed(2)}</span>
              </div>
            </div>
            <div className="order-actions">
              <button className="btn-delivery" onClick={handleReviewClick}>
                Delivery
              </button>
              <button className="btn-pickup" onClick={handlePickupClick}>
                Pickup
              </button>
              <button className="btn-cancel-order" onClick={handleCancelOrder}>
                Cancel Order
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default OrderSummary;
