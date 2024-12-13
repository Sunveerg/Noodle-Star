import React from 'react';
import { useLocation } from 'react-router-dom';
import './OrderSummary.css';
import noodleImg from "../components/assets/noodle.png";

interface CartItem {
    menuId: string;
    name: string;
    price: number;
    quantity: number;
}

const OrderSummary: React.FC = (): JSX.Element => {
    const TAX_RATE = 0.15;

    // Access the state passed via `useNavigate`
    const location = useLocation();
    const { cartItems, totalPrice } = location.state || { cartItems: [], totalPrice: 0 };

    const taxes = totalPrice * TAX_RATE;
    const grandTotal = totalPrice + taxes;

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