import React, { useEffect, useState } from "react";
import { menuResponseModel } from "./model/menuResponseModel";
import noodleImg from "../components/assets/noodle.png";
import styles from "../components/css/AboutUs.module.css";

import "./AvailableMenu.css";
import { createOrder } from "../features/api/createOrder.ts";
import {getAllmenu} from "../features/api/getAllMenu.ts";

interface CartItem {
  menuId: string;
  name: string;
  price: number;
  quantity: number;
}

const AvailableMenuList: React.FC = (): JSX.Element => {
  const [menuItems, setMenuItems] = useState<menuResponseModel[]>([]);
  const [cartItems, setCartItems] = useState<Record<string, CartItem>>({});
  const [totalPrice, setTotalPrice] = useState<number>(0);
  const [checkoutMessage, setCheckoutMessage] = useState<string>("");

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
    setCartItems((prevCartItems) => {
      const updatedCartItems = { ...prevCartItems };
      const existingItem = updatedCartItems[String(menuItem.menuId)];

      if (existingItem) {
        updatedCartItems[String(menuItem.menuId)].quantity += 1;
      } else {
        updatedCartItems[String(menuItem.menuId)] = {
          menuId: String(menuItem.menuId),
          name: menuItem.name,
          price: menuItem.price,
          quantity: 1,
        };
      }

      const newTotalPrice = Object.values(updatedCartItems).reduce(
        (total, item) => total + item.price * item.quantity,
        0
      );

      setTotalPrice(newTotalPrice);
      return updatedCartItems;
    });
  };

  const handleCheckout = async () => {
    if (Object.keys(cartItems).length === 0) {
      setCheckoutMessage("Your cart is empty. Please add items to proceed.");
      return;
    }

    const orderDetails = Object.values(cartItems).map((item) => ({
      menuId: item.menuId,
      quantity: item.quantity,
    }));

    const orderRequest: {
      orderDetails: { quantity: number; menuId: string }[];
      total: number;
      orderId: string;
      customerId: string;
      orderDate: string;
      status: string;
    } = {
      orderId: `orderId-${Date.now()}`,
      customerId: "guestCustomerId",
      status: "Pending",
      orderDate: new Date().toISOString().split("T")[0],
      orderDetails,
      total: totalPrice,
    };

    console.log("Submitting order request:", orderRequest);

    try {
      await createOrder(orderRequest);
      setCheckoutMessage("Order successfully placed! Thank you for your purchase.");
      console.log("Order placed successfully:", orderRequest);

      setCartItems({});
      setTotalPrice(0);
    } catch (error) {
      console.error("Failed to create order:", error);
      setCheckoutMessage("Failed to place the order. Please try again.");
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
              style={{ width: "100px", height: "100px" }}
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
                Object.values(cartItems).map((item) => (
                  <div className="productDetails" key={item.menuId}>
                    <div className="productLabel">
                      {item.name} x {item.quantity}
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
              {checkoutMessage && (
                <p className="checkoutMessage">{checkoutMessage}</p>
              )}
            </div>

            {menuItems.length > 0 ? (
              <div className="menuGrid">
                {menuItems.map((item) => (
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
