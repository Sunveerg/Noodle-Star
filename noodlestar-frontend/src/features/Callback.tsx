/* eslint-disable no-console */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useEffect } from 'react';
import axiosInstance from '../Shared/Api/axiosInstance';
import { useNavigate } from 'react-router-dom';

const Callback: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const handleUserLogin = async (userId: string, accessToken: string) => {
      try {
        const encodedUserId = encodeURIComponent(userId).replace(/\|/g, '%7C');
        const backendUrl = process.env.REACT_APP_BACKEND_URL;
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
        console.log('User successfully logged in or created in the backend.');
      } catch (error) {
        console.error('Error during user login:', error);
      }
    };

    const fetchUserInfo = async (accessToken: string) => {
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
        const userId = userInfo.sub;

        if (userId) {
          // Call the /login endpoint to create the user in the backend
          await handleUserLogin(userId, accessToken);
        } else {
          console.error('User ID is missing.');
        }
      } catch (error) {
        console.error('Error fetching user info:', error);
      }
    };

    const hash = window.location.hash;
    const params = new URLSearchParams(hash.replace('#', '?'));
    const accessToken = params.get('access_token');

    if (accessToken) {
      localStorage.setItem('access_token', accessToken);
      fetchUserInfo(accessToken); // Fetch user info and call /login endpoint
      navigate('/profile'); // Redirect to profile page
    } else {
      console.error('Authentication failed.');
    }
  }, [navigate]);

  return <div>Redirecting...</div>;
};

export default Callback;
