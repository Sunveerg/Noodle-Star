import React, { useEffect, useState, useRef } from 'react';
import axiosInstance from '../Shared/Api/axiosInstance';
import './Profile.css';
import noodleImg from '../components/assets/noodle.png';
import { useNavigate } from 'react-router-dom';
import styles from '../components/css/HomePage.module.css';

const Profile: React.FC = () => {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [userData, setUserData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [isOwner, setIsOwner] = useState(false);
  const [isCustomer, setIsCustomer] = useState(false);
  const [isStaff, setIsStaff] = useState(false);
  const loginCalledRef = useRef(false);
  const navigate = useNavigate();

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleReviewClick = () => {
    navigate('/review');
  };

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleManageStaffClick = () => {
    navigate('/manageStaff');
  };

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleUserLogin = async (userId: string, accessToken: string) => {
    try {
      if (loginCalledRef.current) {
        return;
      }
      loginCalledRef.current = true;

      const encodedUserId = encodeURIComponent(userId).replace(/\|/g, '%7C');
      await axiosInstance.post(
        `http://localhost:8080/api/v1/users/${encodedUserId}/login`,
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
    // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
    const fetchUserInfo = async () => {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        setError('No access token found');
        setLoading(false);
        return;
      }

      try {
        const base64Url = accessToken.split('.')[1];
        const decodedPayload = JSON.parse(atob(base64Url));
        const roles = decodedPayload['https://noodlestar/roles'] || [];

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
        setUserData(userInfo);
        handleUserLogin(userInfo.sub, accessToken);
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

      {isCustomer && (
        <div className="order-history-section">
          <p className="order-history-text">Order History</p>
          <div className="order-history-box"></div>
        </div>
      )}

      {isOwner && (
        <div className="owner-section">
          <button className="owner-button" onClick={handleManageStaffClick}>
            Manage Staff
          </button>
        </div>
      )}

      <br></br>
      {isStaff && (
        <button className={styles.reviewButton} onClick={handleReviewClick}>
          Go to Reviews
        </button>
      )}
    </div>
  );
};

export default Profile;
