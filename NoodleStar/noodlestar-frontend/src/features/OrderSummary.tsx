import React, { useState } from 'react';
import './OrderSummary.css';

interface OrderItem {
    name: string;
    quantity: number;
    price: number;
}

const OrderSummary: React.FC = (): JSX.Element => {
    const TAX_RATE = 0.15;

    // Dummy data for now
    const [orderItems] = useState<OrderItem[]>([
        { name: 'FRIED RICE', quantity: 2, price: 12 },
        { name: 'WONTON SOUP', quantity: 2, price: 6 },
    ]);

    const subtotal = orderItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
    const taxes = subtotal * TAX_RATE;
    const grandTotal = subtotal + taxes;

    return (
        <>
            <div className="checkout-section">
                <h1 className="checkout-title">Checkout</h1>
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
                            {orderItems.map((item, index) => (
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
                                <span>Subtotal: ${subtotal.toFixed(2)}</span>
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

export default OrderSummary;
