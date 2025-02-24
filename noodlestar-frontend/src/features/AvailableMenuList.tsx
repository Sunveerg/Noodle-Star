/* eslint-disable no-console */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useEffect, useState } from 'react';
import { menuResponseModel } from './model/menuResponseModel';
import noodleImg from '../components/assets/noodle.png';
import styles from '../components/css/AboutUs.module.css';
import './AvailableMenu.css';
import { createOrder } from '../features/api/createOrder';
import { getAllmenu } from '../features/api/getAllMenu';
import { useNavigate } from 'react-router-dom';

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
  const [checkoutMessage, setCheckoutMessage] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMenuData = async (): Promise<void> => {
      try {
        const response = await getAllmenu();
        if (Array.isArray(response)) {
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
    console.log('Adding item to cart:', menuItem);
    setCartItems((prevCartItems: CartItem[]) => {
      const existingItem = prevCartItems.find(
        item => String(item.menuId) === String(menuItem.menuId)
      );

      let updatedCartItems;
      if (existingItem) {
        updatedCartItems = prevCartItems.map(item =>
          String(item.menuId) === String(menuItem.menuId)
            ? { ...item, quantity: item.quantity + 1 }
            : item
        ) as CartItem[];
      } else {
        updatedCartItems = [
          ...prevCartItems,
          {
            menuId: String(menuItem.menuId),
            name: menuItem.name,
            price: menuItem.price,
            quantity: 1,
          } as CartItem,
        ];
      }

      document.cookie = `cartItems=${JSON.stringify(updatedCartItems)}; path=/;`;

      return updatedCartItems;
    });

    setTotalPrice(prevTotal => prevTotal + menuItem.price);
  };

  const handleRemoveFromCart = (menuId: string) => {
    setCartItems(prevCartItems => {
      const updatedCartItems = prevCartItems.filter(
        item => item.menuId !== menuId
      );

      const removedItem = prevCartItems.find(item => item.menuId === menuId);
      if (removedItem) {
        setTotalPrice(
          prevTotal => prevTotal - removedItem.price * removedItem.quantity
        );
      }

      return updatedCartItems;
    });
  };

  const handleClearAll = () => {
    setCartItems([]);
    setTotalPrice(0);
    setCheckoutMessage('All items have been cleared from the cart.');
  };

  const handleCheckout = async () => {
    if (Object.keys(cartItems).length === 0) {
      setCheckoutMessage('Your cart is empty. Please add items to proceed.');
      return;
    }

    console.log('Cart Items before checkout:', cartItems);

    // Retrieve access token from localStorage
    const accessToken = localStorage.getItem('access_token'); // Adjust to your token key
    if (!accessToken) {
      setCheckoutMessage('User is not authenticated');
      return;
    }

    // Decode the token to get customerId from the payload (JWT)
    const base64Url = accessToken.split('.')[1];
    const decodedPayload = JSON.parse(atob(base64Url));

    const customerId = decodedPayload.sub; // Assuming 'sub' contains the customerId
    if (!customerId) {
      setCheckoutMessage('Customer ID not found in token');
      return;
    }

    const orderDetails = Object.values(cartItems).map(item => ({
      menuId: item.menuId,
      quantity: item.quantity,
      price: item.price,
    }));

    const orderRequest = {
      orderId: `orderId-${Date.now()}`,
      customerId, // Use the customerId from the token
      status: 'Pending',
      orderDate: new Date().toISOString().split('T')[0],
      orderDetails,
      total: totalPrice,
    };

    console.log('Order Request:', orderRequest);

    try {
      await createOrder(orderRequest);
      setCheckoutMessage('Order has been placed!');
      setCartItems([]);
      setTotalPrice(0);
      navigate('/orderSummary', {
        state: { cartItems: Object.values(cartItems), totalPrice },
      });
    } catch (error) {
      console.error('Failed to create order:', error);
      setCheckoutMessage('Failed to place the order. Please try again.');
    }
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
              {Object.values(cartItems).length > 0 ? (
                Object.values(cartItems).map(item => (
                  <div className="productDetails" key={item.menuId}>
                    <div className="productLabel">
                      {item.name} x {item.quantity}{' '}
                      <button
                        className="removeButton"
                        onClick={() => handleRemoveFromCart(item.menuId)}
                      >
                        -
                      </button>
                    </div>
                    <div className="totalLabel">
                      {(item.price * item.quantity).toFixed(2)}$
                    </div>
                  </div>
                ))
              ) : (
                <p className="no-items">No items in cart</p>
              )}
              <div className="totalDetails">
                <div className="total">Total</div>
                <div className="totalLabel">{totalPrice.toFixed(2)}$</div>
              </div>
              <button className="checkoutButton" onClick={handleCheckout}>
                Checkout
              </button>
              <button className="clearAllButton" onClick={handleClearAll}>
                Clear All
              </button>
              {checkoutMessage && (
                <p className="checkoutMessage">{checkoutMessage}</p>
              )}
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
                    <button
                      className="addButton"
                      onClick={() => handleAddToCart(item)}
                    >
                      <img
                        src="https://pngbasket.com/wp-content/uploads/2021/08/plus-icon-png-design.png"
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
