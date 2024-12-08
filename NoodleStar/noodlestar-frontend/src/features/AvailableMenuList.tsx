import * as React from 'react';
import { useState, useEffect } from 'react';
import { menuResponseModel } from './model/menuResponseModel';
import { getAllmenu } from './api/getAllMenu';
import noodleImg from '../components/assets/noodle.png';

import './AvailableMenu.css'; // Ensure the CSS is linked correctly

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
      <div className="mainContent">
        <div className="titleSection">
          <h2 className="pageTitle">
           Order Now{' '}
            <img
              src={noodleImg}
              alt="Noodle"
              style={{ width: '50px', height: '50px' }}
            />
          </h2>
          <div className="menu-list">
            <div className="cloud-container">
              <div className="cloud4"></div>
              <div className="cloud5"></div>
              <div className="cloud6"></div>
            </div>
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
                      <div className="menu-image">
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
