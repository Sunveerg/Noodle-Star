import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserRequestModel } from './model/userRequestModel';
import { getUserById, updateUser } from './api/updateUser';
import './UpdateUserInfo.css';

const UpdateUser: React.FC = () => {
  const [userId, setUserId] = useState<string>('');
  const [user, setUser] = useState<UserRequestModel>({
    email: '',
    firstName: '',
    lastName: '',
    roles: [],
    permissions: [],
  });
  const [error, setError] = useState<{ [key: string]: string }>({});
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(true);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserData = async (): Promise<void> => {
      try {
        const accessToken = localStorage.getItem('access_token');
        if (!accessToken) throw new Error('Access token not found');

        const base64Url = accessToken.split('.')[1];
        const decodedPayload = JSON.parse(atob(base64Url));
        const userIdFromToken = decodedPayload['sub'];
        if (!userIdFromToken) throw new Error('User ID not found in token');

        setUserId(userIdFromToken);

        const userInfo = await getUserById(userIdFromToken);
        setUser({
          email: userInfo.email,
          firstName: userInfo.firstName,
          lastName: userInfo.lastName,
          roles: userInfo.roles,
          permissions: userInfo.permissions,
        });
      } catch (error) {
        setErrorMessage('Failed to fetch user data.');
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, []);

  const validate = (): boolean => {
    const newError: { [key: string]: string } = {};
    if (!user.firstName) newError.firstName = 'First name is required';
    if (!user.lastName) newError.lastName = 'Last name is required';
    if (!user.email) newError.email = 'Email is required';
    setError(newError);
    return Object.keys(newError).length === 0;
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    const { name, value } = e.target;
    setUser(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (
    e: React.FormEvent<HTMLFormElement>
  ): Promise<void> => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    setErrorMessage('');
    setSuccessMessage('');

    try {
      await updateUser(userId, user);
      setSuccessMessage('User updated successfully!');
      setTimeout(() => navigate('/profile'), 2000);
    } catch {
      setErrorMessage('Failed to update user.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (errorMessage) return <div>Error: {errorMessage}</div>;

  return (
    <div className="update-user-form">
      <h3>Update User</h3>
      {successMessage && (
        <div className="alert alert-success">{successMessage}</div>
      )}
      {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={user.email}
            onChange={handleInputChange}
            className="form-control"
          />
          {error.email && <span className="text-danger">{error.email}</span>}
        </div>

        <div className="form-group">
          <label>First Name</label>
          <input
            type="text"
            name="firstName"
            value={user.firstName}
            onChange={handleInputChange}
            className="form-control"
          />
          {error.firstName && (
            <span className="text-danger">{error.firstName}</span>
          )}
        </div>

        <div className="form-group">
          <label>Last Name</label>
          <input
            type="text"
            name="lastName"
            value={user.lastName}
            onChange={handleInputChange}
            className="form-control"
          />
          {error.lastName && (
            <span className="text-danger">{error.lastName}</span>
          )}
        </div>

        <button type="submit" className="btn btn-primary">
          Update User
        </button>
      </form>
    </div>
  );
};

export default UpdateUser;
