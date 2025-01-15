/* eslint-disable no-console */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useEffect } from 'react';

const Callback: React.FC = () => {
  useEffect(() => {
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
          if (response.status === 429) {
            console.error('Rate limit exceeded. Please try again later.');
            return;
          }
          throw new Error('Failed to fetch user info');
        }

        const userInfo = await response.json();
        console.log('User Info:', userInfo);

        // Get the userId directly from Auth0
        const userId = userInfo.sub || userInfo.userId;

        if (userId) {
          console.log('User ID:', userId);
          //await handleUserLogin(userId, accessToken); // Pass accessToken and userId here
        } else {
          console.error('User ID is missing.');
        }
      } catch (error) {
        console.error('Error fetching user info:', error);
      }
    };

    /*const handleUserLogin = async (userId: string, accessToken: string) => {
          try {
              // Explicitly replace '|' with '%7C' for encoding the userId
              const encodedUserId = encodeURIComponent(userId).replace(/\|/g, '%7C');
              console.log("Access Token:", accessToken);
              console.log("User ID:", encodedUserId);
      
              // Use axiosInstance to make the POST request
              const response = await axiosInstance.post(
                  `http://localhost:8080/api/v1/users/${encodedUserId}/login`, // Directly passing userId here
                  {}, // Empty body (adjust if necessary)
                  {
                      headers: {
                          Authorization: `Bearer ${accessToken}`,
                          "Content-Type": "application/json",
                      },
                  }
              );
      
              console.log("Response data:", response.data);
              console.log("User successfully logged in:", response.data);
      
          } catch (error) {
              console.error("Error during user login:", error);
          }
      };
  */
    const hash = window.location.hash;
    console.log('Hash:', hash); // Log the full hash for debugging
    const params = new URLSearchParams(hash.replace('#', '?'));
    const accessToken = params.get('access_token');

    if (accessToken) {
      console.log('Access Token:', accessToken); // Log the access token
      localStorage.setItem('access_token', accessToken);
      fetchUserInfo(accessToken); // Fetch user info using the access token
      window.location.href = '/profile'; // Redirect to home or
    } else {
      console.error('Authentication failed.');
    }
  }, []);

  return <div>Redirecting...</div>;
};

export default Callback;
