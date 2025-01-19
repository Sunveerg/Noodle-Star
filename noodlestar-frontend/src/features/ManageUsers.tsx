/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import './ManageUsers.css';
import { StaffResponseModel } from './model/staffResponseModel';

const ManageUsers: React.FC = (): JSX.Element => {
  const [users, setUsers] = useState<StaffResponseModel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  const fetchUsers = async (): Promise<StaffResponseModel[]> => {
    try {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        setError('No access token found');
        setLoading(false);
        return [];
      }

      const response = await fetch(`${backendUrl}/api/v1/users`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${accessToken}`,
        },
      });

      if (!response.ok) {
        if (response.status === 401) {
          setError('Unauthorized access. Please login again.');
        } else {
          setError('Failed to fetch user data.');
        }
        return [];
      }

      const userData: StaffResponseModel[] = await response.json();
      return userData;
    } catch (err: any) {
      setError(err.message || 'Error fetching user data.');
      return [];
    }
  };

  const handleAddAsStaff = async (userId: string) => {
    try {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        setError('No access token found');
        return;
      }

      // Correct endpoint format
      const response = await fetch(
        `${backendUrl}/api/v1/users/staff/${userId}`,
        {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${accessToken}`, // Ensure you're passing the access token
          },
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          setError('Unauthorized access. Please log in again.');
        } else if (response.status === 404) {
          setError('User not found.');
        } else if (response.status === 409) {
          setError('User is already a staff member.');
        } else {
          setError('Failed to update user role.');
        }
        return;
      }

      alert('User has been successfully granted the Staff role.');
    } catch (err: any) {
      setError(err.message || 'Error updating user role.');
      console.error('Error updating user role:', err);
    }
  };

  useEffect(() => {
    const loadUsers = async (): Promise<void> => {
      try {
        setLoading(true);
        const userData = await fetchUsers();
        setUsers(userData);
      } catch (err: any) {
        setError(err.message || 'Failed to load users.');
      } finally {
        setLoading(false);
      }
    };

    loadUsers();
  }, []);

  if (loading) {
    return <div className="loading-message">Loading user data...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="manage-users-container">
      <h2 className="user-list-title">Manage Users</h2>
      <div className="user-list">
        {users.length === 0 ? (
          <div className="empty-message">No users found.</div>
        ) : (
          users.map(user => (
            <div key={user.userId} className="user-item">
              <img
                src={`https://ui-avatars.com/api/?name=${user.firstName}+${user.lastName}`}
                alt={`${user.firstName} ${user.lastName}`}
                className="user-avatar"
              />
              <div className="user-info">
                <h3>
                  {user.firstName} {user.lastName}
                </h3>
                <p>
                  <strong>Email:</strong> {user.email}
                </p>
                <p>
                  <strong>Roles:</strong>{' '}
                  {user.roles ? user.roles.join(', ') : 'No roles assigned'}
                </p>
              </div>
              <div className="user-actions">
                <button
                  className="btn-add-staff"
                  onClick={() => handleAddAsStaff(user.userId)}
                >
                  Add as Staff
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ManageUsers;
