import axiosInstance from '../Shared/Api/axiosInstance';
import React, { useEffect, useState, useRef } from 'react';
import './Profile.css';
import noodleImg from '../components/assets/noodle.png';

const OwnerProfile: React.FC = () => {
  const [userData, setUserData] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const loginCalledRef = useRef(false); // Track if login has already been called

  const handleUserLogin = async (userId: string, accessToken: string) => {
    try {
      if (loginCalledRef.current) {
        return; // Prevent multiple calls
      }
      loginCalledRef.current = true; // Mark login as called
      const encodedUserId = encodeURIComponent(userId).replace(/\|/g, '%7C');
      console.log('Access Token:', accessToken);
      console.log('User ID:', encodedUserId);

      const response = await axiosInstance.post(
        `http://localhost:8080/api/v1/users/${encodedUserId}/login`,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
          },
        }
      );
      console.log('Response data:', response.data);
      console.log('User successfully logged in:', response.data);
    } catch (error) {
      console.error('Error during user login:', error);
    }
  };

  useEffect(() => {
    const fetchUserInfo = async () => {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        setError('No access token found');
        setLoading(false);
        return;
      }

      try {
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
        console.log('User Info:', userInfo);
        setUserData(userInfo);
        handleUserLogin(userInfo.sub, accessToken); // Only called once due to flag
      } catch (err) {
        setError(`Error fetching user info: ${err.message}`);
        console.error('Error fetching user info:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, []); // Empty dependency array ensures this only runs once

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

      <div className="manage-staff-section">
        <button className="manage-staff-button">Manage Staff</button>
      </div>
    </div>
  );
};

export default OwnerProfile;
