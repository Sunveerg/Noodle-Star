/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { StaffResponseModel } from './model/staffResponseModel';
import './ManageStaff.css';

const ManageStaff: React.FC = (): JSX.Element => {
  const [staffUsers, setStaffUsers] = useState<StaffResponseModel[]>([]);
  const [, setUserData] = useState<any>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [isOwner, setIsOwner] = useState<boolean>(false);
  const [, setIsCustomer] = useState<boolean>(false);
  const navigate = useNavigate();
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  const getStaff = async (): Promise<StaffResponseModel[]> => {
    try {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        setError('No access token found');
        setLoading(false);
        return [];
      }

      const response = await fetch(`${backendUrl}/api/v1/users/staff`, {
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
          setError('Failed to fetch staff data');
        }
        return [];
      }

      const staffData: StaffResponseModel[] = await response.json();
      return staffData;
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message || 'Error fetching staff data');
      } else {
        setError('An unknown error occurred');
      }
      return [];
    }
  };

  const handleDeleteStaff = async (userId: string) => {
    if (!window.confirm('Are you sure you want to delete this staff member?')) {
      return;
    }

    try {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        setError('No access token found');
        return;
      }
      const backendUrl = process.env.REACT_APP_BACKEND_URL;

      const response = await fetch(
        `${backendUrl}/api/v1/users/staff/${userId}`,
        {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          setError('Unauthorized access. Please log in again.');
        } else {
          setError('Failed to delete staff member.');
        }
        return;
      }

      setStaffUsers(prevStaff =>
        prevStaff.filter(staff => staff.userId !== userId)
      );
      alert('Staff member deleted successfully.');
    } catch (err: any) {
      setError(err.message || 'Error deleting staff member.');
      console.error('Error deleting staff member:', err);
    }
  };
  const handleManageStaffClick = () => {
    navigate('/addStaff');
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
        const base64Url = accessToken.split('.')[1];
        const decodedPayload = JSON.parse(atob(base64Url));
        const roles = decodedPayload['https://noodlestar/roles'] || [];

        setIsOwner(roles.includes('Owner'));
        setIsCustomer(roles.includes('Customer'));

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
      } catch (err: any) {
        setError(`Error fetching user info: ${err.message}`);
        console.error('Error fetching user info:', err);
      }
    };

    const fetchStaff = async (): Promise<void> => {
      try {
        setLoading(true);
        const staffData = await getStaff();
        setStaffUsers(staffData);
      } catch (err: any) {
        setError(err.message || 'Failed to fetch staff users');
      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
    fetchStaff();
  }, []);

  if (loading) {
    return <div className="loading-message">Loading staff data...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="manage-staff-container">
      <h2 className="staff-list-title">Manage Staff</h2>

      {isOwner && (
        <div className="owner-section">
          <button className="btn-add-staff" onClick={handleManageStaffClick}>
            Add Staff
          </button>
        </div>
      )}

      <div className="staff-list">
        {staffUsers.length === 0 ? (
          <div className="empty-message">No staff users found.</div>
        ) : (
          staffUsers.map(user => (
            <div key={user.userId} className="staff-item">
              <img
                src={`https://ui-avatars.com/api/?name=${user.firstName}+${user.lastName}`}
                alt={`${user.firstName} ${user.lastName}`}
                className="staff-avatar"
              />
              <div className="staff-info">
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
                <p>
                  <strong>Permissions:</strong>{' '}
                  {user.permissions
                    ? user.permissions.join(', ')
                    : 'No permissions assigned'}
                </p>
              </div>
              {isOwner && (
                <div className="staff-actions">
                  <button
                    className="btn-delete-staff"
                    onClick={() => handleDeleteStaff(user.userId)}
                  >
                    Delete
                  </button>
                  <button
                    className="btn-edit-staff"
                    onClick={() => navigate(`${user.userId}/update`)}
                  >
                    Edit
                  </button>
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ManageStaff;
