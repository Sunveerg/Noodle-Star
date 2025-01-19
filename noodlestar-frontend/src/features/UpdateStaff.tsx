import * as React from 'react';
import { FormEvent, JSX, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { UserRequestModel } from './model/userRequestModel';
import { getStaff, updateStaff } from './api/updateStaff';
import '../components/css/UpdateStaff.css';

interface ApiError {
  message: string;
}

const UpdateStaff: React.FC = (): JSX.Element => {
  const { staffId } = useParams<{ staffId: string }>();
  const [staff, setStaff] = useState<UserRequestModel>({
    firstName: '',
    lastName: '',
    email: '',
    roles: [],
    permissions: [],
  });
  const [error, setError] = useState<{ [key: string]: string }>({});
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [showNotification, setShowNotification] = useState<boolean>(false);

  const navigate = useNavigate();

  useEffect(() => {
    // eslint-disable-next-line no-console
    console.log('Component mounted. userId from useParams:', staffId);
    const fetchStaff = async (): Promise<void> => {
      if (staffId) {
        // eslint-disable-next-line no-console
        console.log('Fetching staff data for userId:', staffId);
        try {
          const response = await getStaff(staffId);
          // eslint-disable-next-line no-console
          console.log('Staff data fetched successfully:', response);
          setStaff({
            firstName: response.firstName,
            lastName: response.lastName,
            email: response.email,
            roles: response.roles,
            permissions: response.permissions,
          });
        } catch (error) {
          console.error(`Error fetching staff with id ${staffId}`, error);
        }
      }
    };
    fetchStaff().catch(error => console.error('Error fetching staff', error));
  }, [staffId]);

  const validate = (): boolean => {
    const newError: { [key: string]: string } = {};
    if (!staff.firstName) {
      newError.firstName = 'First name is required';
    }
    if (!staff.lastName) {
      newError.lastName = 'Last name is required';
    }
    if (!staff.email) {
      newError.email = 'Email is required';
    }
    if (!staff.roles.length) {
      newError.roles = 'At least one role is required';
    }
    if (!staff.permissions.length) {
      newError.permissions = 'At least one permission is required';
    }
    setError(newError);
    return Object.keys(newError).length === 0;
  };

  const handleSubmit = async (
    event: FormEvent<HTMLFormElement>
  ): Promise<void> => {
    event.preventDefault();
    if (!validate()) {
      return;
    }
    setLoading(true);
    setErrorMessage('');
    setSuccessMessage('');
    setShowNotification(false);

    try {
      if (staffId) {
        await updateStaff(staffId, staff);
        setSuccessMessage('Staff updated successfully');
        setShowNotification(true);
        setTimeout(() => {
          navigate('/manageStaff');
        }, 2000);
      }
    } catch (error) {
      const apiError = error as ApiError;
      setErrorMessage(`Error updating staff: ${apiError.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="edit-staff-form">
      <h3 className="text-center">
        Staff &nbsp;
        <small className="text-muted">Edit Form</small>
      </h3>
      {loading && <div className="loader">Loading...</div>}
      <br />
      <div className="container">
        <form onSubmit={handleSubmit} className="text-center">
          <div className="row">
            <div className="col-4">
              <div className="form-group">
                <label>First Name</label>
                <input
                  type={'text'}
                  name={'firstName'}
                  placeholder={'Enter first name'}
                  className={'form-control'}
                  value={staff.firstName}
                  onChange={e =>
                    setStaff(prev => ({
                      ...prev,
                      firstName: e.target.value,
                    }))
                  }
                  required
                />
                {error.firstName && (
                  <div className="text-danger">{error.firstName}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Last Name</label>
                <input
                  type={'text'}
                  name={'lastName'}
                  placeholder={'Enter last name'}
                  className={'form-control'}
                  value={staff.lastName}
                  onChange={e =>
                    setStaff(prev => ({
                      ...prev,
                      lastName: e.target.value,
                    }))
                  }
                  required
                />
                {error.lastName && (
                  <div className="text-danger">{error.lastName}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Email</label>
                <input
                  type={'email'}
                  name={'email'}
                  placeholder={'Enter email'}
                  className={'form-control'}
                  value={staff.email}
                  onChange={e =>
                    setStaff(prev => ({
                      ...prev,
                      email: e.target.value,
                    }))
                  }
                  required
                />
                {error.email && (
                  <div className="text-danger">{error.email}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Roles</label>
                <input
                  type={'text'}
                  name={'roles'}
                  placeholder={'Enter roles (comma-separated)'}
                  className={'form-control'}
                  value={staff.roles.join(', ')}
                  onChange={e =>
                    setStaff(prev => ({
                      ...prev,
                      roles: e.target.value.split(',').map(role => role.trim()),
                    }))
                  }
                  required
                />
                {error.roles && (
                  <div className="text-danger">{error.roles}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Permissions</label>
                <input
                  type={'text'}
                  name={'permissions'}
                  placeholder={'Enter permissions (comma-separated)'}
                  className={'form-control'}
                  value={staff.permissions.join(', ')}
                  onChange={e =>
                    setStaff(prev => ({
                      ...prev,
                      permissions: e.target.value
                        .split(',')
                        .map(permission => permission.trim()),
                    }))
                  }
                  required
                />
                {error.permissions && (
                  <div className="text-danger">{error.permissions}</div>
                )}
              </div>
            </div>
          </div>
          <br />
          <div className={'row'}>
            <button type={'submit'} className={'btn btn-primary'}>
              Update
            </button>
          </div>
        </form>
      </div>
      {errorMessage && <p className="error-message">{errorMessage}</p>}
      {showNotification && (
        <div className="notification">
          <p>{successMessage}</p>
        </div>
      )}
    </div>
  );
};

export default UpdateStaff;
