/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useEffect, useState, useRef } from 'react';
import axiosInstance from '../Shared/Api/axiosInstance';
import './Profile.css';
import noodleImg from '../components/assets/noodle.png';
import { useNavigate } from 'react-router-dom';
import { getOrderByCustomerId } from '../features/api/getOrderByCustomerId';
import { getMenuItemById } from '../features/api/getMenuItemById';
import { OrderResponseModel } from '../features/model/orderResponseModel';
import { getUserById } from './api/updateUser';

const Profile: React.FC = () => {
  const [userData, setUserData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [isOwner, setIsOwner] = useState(false);
  const [isCustomer, setIsCustomer] = useState(false);
  const [isStaff, setIsStaff] = useState(false);
  const [orderHistory, setOrderHistory] = useState<OrderResponseModel[]>([]);
  const [areOrdersCollapsed, setAreOrdersCollapsed] = useState(true); // State to collapse/expand all orders
  const [isNavbarExpanded, setIsNavbarExpanded] = useState(false); // State for navbar expansion
  const loginCalledRef = useRef(false);
  const navigate = useNavigate();

  const handleReviewClick = () => {
    navigate('/review');
  };

  const handleManageStaffClick = () => {
    navigate('/manageStaff');
  };

  const handleUpdateClick = (userId: string) => {
    navigate(`/updateUsers/${userId}`);
  };

  const handleUserLogin = async (userId: string, accessToken: string) => {
    try {
      if (loginCalledRef.current) {
        return;
      }
      loginCalledRef.current = true;
      const backendUrl = process.env.REACT_APP_BACKEND_URL;
      const encodedUserId = encodeURIComponent(userId).replace(/\|/g, '%7C');
      await axiosInstance.post(
        `${backendUrl}/api/v1/users/${encodedUserId}/login`,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
          },
        }
      );
    } catch (error) {
      console.error('Error during user login:', error);
    }
  };

  useEffect(() => {
    const fetchUserInfo = async () => {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        setError('Please log in to view your profile.');
        setLoading(false);
        return;
      }

      try {
        const base64Url = accessToken.split('.')[1];
        const decodedPayload = JSON.parse(atob(base64Url));

        const roles = decodedPayload['https://noodlestar/roles'] || [];
        const userId = decodedPayload.sub;

        setIsOwner(roles.includes('Owner'));
        setIsCustomer(roles.includes('Customer'));
        setIsStaff(roles.includes('Staff'));

        const response = await fetch(
          'https://dev-5kbvxb8zgblo1by3.us.auth0.com/userinfo',
          {
            method: 'GET',
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );

        if (!response.ok) {
          throw new Error('Failed to fetch user info');
        }

        const userInfo = await response.json();
        const userInfo2 = await getUserById(userId);

        setUserData({
          ...userInfo2,
          picture: userInfo.picture,
          nickname: userInfo.nickname,
          userId,
        });
        handleUserLogin(userId, accessToken);
      } catch (err: unknown) {
        if (err && typeof err === 'object' && 'message' in err) {
          const error = err as { message: string };
          setError(`Error fetching user info: ${error.message}`);
          console.error('Error fetching user info:', error);
        } else {
          setError('An unknown error occurred');
          console.error('An unknown error occurred:', err);
        }
      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, []);

  useEffect(() => {
    const fetchOrderHistory = async (): Promise<void> => {
      const accessToken = localStorage.getItem('access_token');

      if (!accessToken) {
        console.error('Access token not found');
        setError('Access token is required to fetch order history.');
        setLoading(false);
        return;
      }

      try {
        const base64Url = accessToken.split('.')[1];
        const decodedPayload = JSON.parse(atob(base64Url));
        const customerId = decodedPayload.sub;

        if (!customerId) {
          console.error('Customer ID not found in access token');
          setError('Customer ID is required to fetch order history.');
          setLoading(false);
          return;
        }

        const orders = await getOrderByCustomerId(customerId);

        const updatedOrders = await Promise.all(
          orders.map(async order => {
            const updatedOrderDetails = await Promise.all(
              order.orderDetails.map(async detail => {
                try {
                  const menuItem = await getMenuItemById(detail.menuId);
                  return {
                    ...detail,
                    dishName: menuItem?.name || 'Unknown Dish',
                  };
                } catch (err) {
                  console.error(
                    `Error fetching menu item for menuId ${detail.menuId}:`,
                    err
                  );
                  return {
                    ...detail,
                    dishName: 'Unknown Dish',
                  };
                }
              })
            );

            return {
              ...order,
              orderDetails: updatedOrderDetails,
            };
          })
        );

        // Sort orders by date (most recent first)
        updatedOrders.sort(
          (a, b) =>
            new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()
        );

        setOrderHistory(updatedOrders as OrderResponseModel[]);
      } catch (err) {
        console.error('Error fetching order history:', err);
        setError('Failed to fetch order history.');
      } finally {
        setLoading(false);
      }
    };

    if (isCustomer) {
      fetchOrderHistory();
    }
  }, [isCustomer]);

  const toggleOrdersCollapse = () => {
    setAreOrdersCollapsed(prev => !prev);
  };

  if (loading) {
    return <div className="loading-message">Loading...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (!userData) {
    return <div className="error-message">No user data available</div>;
  }

  return (
    <div className="profile-page">
      <h2 className="pageTitle">
        Account
        <img src={noodleImg} alt="Noodle" className="logo-img" />
      </h2>
      <div className="cloud-container">
        <div className="cloud4"></div>
        <div className="cloud5"></div>
        <div className="cloud6"></div>
      </div>
      <div className="topRightImage"></div>
      <div className="welcome-section">
        <p className="welcome-text">Welcome back, {userData.nickname}</p>
      </div>
      <div className="profile-info-section">
        <div className="profile-image-container">
          <img src={userData.picture} alt="Profile" className="profile-image" />
        </div>
        <p className="profile-name">{userData.nickname}</p>
        <p className="profile-email">{userData.email}</p>
      </div>

      <div className="button-container">
        <button
          className="modify-button"
          onClick={() => handleUpdateClick(userData.userId)}
        >
          Modify
        </button>
      </div>

      {/* Only show Order History if the user is a customer */}
      {isCustomer && (
        <div className="order-history-container">
          <h2 className="order-history-title">Order History</h2>
          {orderHistory.length === 0 ? (
            <p className="order-history-empty">No orders found.</p>
          ) : (
            <>
              <button
                className="collapse-all-button"
                onClick={toggleOrdersCollapse}
              >
                {areOrdersCollapsed ? 'Show All Orders' : 'Hide All Orders'}
              </button>
              {!areOrdersCollapsed && (
                <ul className="order-history-list">
                  {orderHistory.map(order => (
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
            </>
          )}
        </div>
      )}

      {(isOwner || isStaff) && (
        <div className={`side-navbar ${isNavbarExpanded ? 'expanded' : ''}`}>
          {(isOwner || isStaff) && (
            <button
              className="toggle-button"
              onClick={() => setIsNavbarExpanded(!isNavbarExpanded)}
            >
              {isNavbarExpanded ? '✕' : '☰'}
            </button>
          )}
          <div className={`navbar-content ${isNavbarExpanded ? 'show' : ''}`}>
            {isOwner && (
              <>
                <button className="nav-button" onClick={handleManageStaffClick}>
                  Manage Staff
                </button>
                <button
                  className="nav-button"
                  onClick={() => navigate('/reports')}
                >
                  Dish Report
                </button>
                <button
                  className="nav-button"
                  onClick={() => navigate('/daily-order')}
                >
                  Daily Order Total
                </button>
                <button
                  className="nav-button"
                  onClick={() => navigate('/financial-report')}
                >
                  Financial Report
                </button>
              </>
            )}
            {(isOwner || isStaff) && (
              <>
                <button className="nav-button" onClick={handleReviewClick}>
                  Reviews
                </button>
                <button
                  className="nav-button"
                  onClick={() => navigate('/orders')}
                >
                  Orders
                </button>
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default Profile;
