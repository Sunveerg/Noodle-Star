<<<<<<< HEAD
import React from 'react';
import { useLocation } from 'react-router-dom';
import './OrderSummary.css';
import noodleImg from "../components/assets/noodle.png";

interface CartItem {
    menuId: string;
    name: string;
    price: number;
    quantity: number;
=======
import React, { useState } from 'react';
import './OrderSummary.css';

interface OrderItem {
    name: string;
    quantity: number;
    price: number;
>>>>>>> 6fe70c4aee88656d0da62b67cde02e1faed0738b
}

const OrderSummary: React.FC = (): JSX.Element => {
    const TAX_RATE = 0.15;

<<<<<<< HEAD
    // Access the state passed via `useNavigate`
    const location = useLocation();
    const { cartItems, totalPrice } = location.state || { cartItems: [], totalPrice: 0 };

    const taxes = totalPrice * TAX_RATE;
    const grandTotal = totalPrice + taxes;
=======
    // Dummy data for now
    const [orderItems] = useState<OrderItem[]>([
        { name: 'FRIED RICE', quantity: 2, price: 12 },
        { name: 'WONTON SOUP', quantity: 2, price: 6 },
    ]);

    const subtotal = orderItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const taxes = subtotal * TAX_RATE;
    const grandTotal = subtotal + taxes;
>>>>>>> 6fe70c4aee88656d0da62b67cde02e1faed0738b

    return (
        <>
            <div className="checkout-section">
<<<<<<< HEAD
                <h1 className="checkout-title">
                    Checkout
                    <img
                        src={noodleImg}
                        alt="Noodle"
                        style={{ width: '100px', height: '100px' }}
                    />
                </h1>
=======
                <h1 className="checkout-title">Checkout</h1>
>>>>>>> 6fe70c4aee88656d0da62b67cde02e1faed0738b
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
<<<<<<< HEAD
                            {cartItems.map((item: CartItem, index: number) => (
=======
                            {orderItems.map((item, index) => (
>>>>>>> 6fe70c4aee88656d0da62b67cde02e1faed0738b
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
<<<<<<< HEAD
                                <span>Subtotal: ${totalPrice.toFixed(2)}</span>
=======
                                <span>Subtotal: ${subtotal.toFixed(2)}</span>
>>>>>>> 6fe70c4aee88656d0da62b67cde02e1faed0738b
                            </div>
                            <div className="summary-item">
                                <span>Taxes: ${taxes.toFixed(2)}</span>
                            </div>
                            <div className="summary-item grand-total">
                                <span>Grand Total: ${grandTotal.toFixed(2)}</span>
                            </div>
                        </div>
                        <div className="order-actions">
                            <button className="btn-delivery">Delivery</button>
                            <button className="btn-pickup">Pickup</button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
};

<<<<<<< HEAD
export default OrderSummary;
=======
export default OrderSummary;
>>>>>>> 6fe70c4aee88656d0da62b67cde02e1faed0738b
