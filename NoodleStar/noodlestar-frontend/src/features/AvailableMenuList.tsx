import * as React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate
import { menuResponseModel } from './model/menuResponseModel';
import { getAllmenu } from './api/getAllMenu';
import noodleImg from '../components/assets/noodle.png';
import styles from '../components/css/AboutUs.module.css';

import './AvailableMenu.css';

interface CartItem {
  menuId: string;
  name: string;
  price: number;
  quantity: number;
}

const AvailableMenuList: React.FC = (): JSX.Element => {
  const [menuItems, setMenuItems] = useState<menuResponseModel[]>([]);
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [totalPrice, setTotalPrice] = useState<number>(0);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMenuData = async (): Promise<void> => {
      try {
        const response = await getAllmenu();
        if (Array.isArray(response)) {
          // Filter items with status "AVAILABLE"
          const availableItems = response.filter(
              (item: menuResponseModel) => item.status === 'AVAILABLE'
          );
          setMenuItems(availableItems);
        } else {
          console.error('Fetched data is not an array:', response);
        }
      } catch (error) {
        console.error('Error fetching menu items:', error);
      }
    };

    fetchMenuData().catch(error =>
        console.error('Error in fetchMenuData:', error)
    );
  }, []);

  const handleAddToCart = (menuItem: menuResponseModel) => {
    setCartItems((prevCartItems: CartItem[]) => {
      const existingItem = prevCartItems.find(item => item.menuId === menuItem.menuId);
      if (existingItem) {
        return prevCartItems.map(item =>
            item.menuId === menuItem.menuId
                ? { ...item, quantity: item.quantity + 1 }
                : item
        ) as CartItem[];
      } else {
        return [
          ...prevCartItems,
          {
            menuId: menuItem.menuId,
            name: menuItem.name,
            price: menuItem.price,
            quantity: 1,
          } as CartItem,
        ];
      }
    });

    setTotalPrice(prevTotal => prevTotal + menuItem.price);
  };

  const handleOrderSummary = () => {
    console.log("Cart Items:", cartItems.length);
    if (cartItems.length === 0) {
      alert("Your cart is empty! Please add items before proceeding to checkout.");
      return;
    }
    navigate('/orderSummary', { state: { cartItems, totalPrice } });
  };

  return (
      <div className="orderPage">
        <div className={styles.cloud3}></div>

        <div className="mainContent">
          <div className="titleSectionO">
            <h2 className="pageTitleO">
              Order Now
              <img
                  src={noodleImg}
                  alt="Noodle"
                  style={{ width: '100px', height: '100px' }}
              />
            </h2>
            <div className="menu-list">
              <div className="topRightImage"></div>
              <div className="orderDetailsBox">
                <h2>Order Details</h2>
                <div className="productDetails">
                  <div className="productLabel">Product</div>
                  <div className="totalLabel">Total</div>
                </div>
                {cartItems.length > 0 ? (
                    cartItems.map(item => (
                        <div className="productDetails" key={item.menuId}>
                          <div className="productLabel">{item.name} x {item.quantity}</div>
                          <div className="totalLabel">{(item.price * item.quantity).toFixed(2)}$</div>
                        </div>
                    ))
                ) : (
                    <p className="no-items">No items in cart</p>
                )}
                <div className="totalDetails">
                  <div className="total">Total</div>
                  <div className="totalLabel">{totalPrice.toFixed(2)}$</div>
                </div>
                <button
                    className="checkoutButton"
                    onClick={handleOrderSummary}
                    disabled={cartItems.length === 0}
                >
                  Checkout
                </button>
              </div>

              {menuItems.length > 0 ? (
                  <div className="menuGrid">
                    {menuItems.map(item => (
                        <div className="menuCard" key={item.menuId}>
                          <h3 className="dishTitle">{item.name}</h3>
                          <div className="cardContent">
                            <div className="dish-image">
                              <img
                                  src={item.itemImage}
                                  alt={item.name}
                                  className="dishImage"
                              />
                            </div>
                            <div className="cardDetails">
                              <p className="dishDescription">{item.description}</p>
                              <p className="price">{item.price}$</p>
                            </div>
                          </div>
                          <button className="addButton" onClick={() => handleAddToCart(item)}>
                            <img
                                src="https://pngbasket.com/wp-content/uploads/2021/08/plus-icon-png-design.png" // Use the appropriate icon path
                                alt="Add"
                                className="addIcon"
                            />
                            Add
                          </button>
                        </div>
                    ))}
                  </div>
              ) : (
                  <p className="no-items">No available menu items</p>
              )}
            </div>
          </div>
        </div>
      </div>
  );
};

export default AvailableMenuList;