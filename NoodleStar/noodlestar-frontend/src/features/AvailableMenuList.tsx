import * as React from 'react';
import { useState, useEffect } from 'react';
import { menuResponseModel } from './model/menuResponseModel';
import { getAllmenu } from './api/getAllMenu';
import noodleImg from '../components/assets/noodle.png';
import styles from '../components/css/AboutUs.module.css';
import goldenLine from '../components/assets/goldenLine.png';  // Adjust the path as needed

import './AvailableMenu.css';

const AvailableMenuList: React.FC = (): JSX.Element => {
  const [menuItems, setMenuItems] = useState<menuResponseModel[]>([]);

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

  return (
      <div className="orderPage">
        <div className={styles.cloud3}></div>

        <div className="mainContent">
          <div className="titleSection">
            <h2 className="pageTitle">
              Order Now{' '}
              <img
                  src={noodleImg}
                  alt="Noodle"
                  style={{width: '100px', height: '100px'}}
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
                <div className="totalDetails">
                  <div className="total">Total</div>
                </div>
                <button className="checkoutButton">
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
                          <button className="addButton">
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
