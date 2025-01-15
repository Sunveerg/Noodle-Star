import * as React from 'react';
import { FormEvent, JSX, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { getMenu, updateMenu } from '../features/api/updateMenu.ts';
import { menuRequestModel } from '@/features/model/menuRequestModel.ts';
import { Status } from '../features/model/Status.ts';
import '../components/css/UpdateMenu.css';

interface ApiError {
  message: string;
}

const UpdateMenu: React.FC = (): JSX.Element => {
  const { menuId } = useParams<{ menuId: string }>();
  const [menu, setMenu] = useState<menuRequestModel>({
    name: '',
    description: '',
    price: 0,
    category: '',
    itemImage: '',
    status: Status.NOT_AVAILABLE,
  });
  const [error, setError] = useState<{ [key: string]: string }>({});
  const [successMessage, setSuccessMessage] = useState<string>('');
  const [errorMessage, setErrorMessage] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const [showNotification, setShowNotification] = useState<boolean>(false);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchMenu = async () => {
      if (menuId) {
        try {
          const response = await getMenu(menuId);
          setMenu({
            name: response.name,
            description: response.description,
            price: response.price,
            category: response.category,
            itemImage: response.itemImage,
            status: response.status,
          });
        } catch (error) {
          console.error(`Error fetching menu with id ${menuId}`, error);
        }
      }
    };
    fetchMenu().catch(error => console.error('Error fetching menu', error));
  }, [menuId]);

  const validate = (): boolean => {
    const newError: { [key: string]: string } = {};
    if (!menu.name) {
      newError.name = 'Name is required';
    }
    if (!menu.description) {
      newError.description = 'Description is required';
    }
    if (!menu.price) {
      newError.price = 'Price is required';
    }
    if (!menu.category) {
      newError.category = 'Category is required';
    }
    if (!menu.itemImage) {
      newError.itemImage = 'Image is required';
    }
    if (!menu.status) {
      newError.status = 'Status is required';
    }
    setError(newError);
    return Object.keys(newError).length === 0;
  };

  const handleSubmit = async (
    event: FormEvent<HTMLFormElement>
  ): Promise<void> => {
    event.preventDefault();
    console.log('Menu being submitted:', menu);
    if (!validate()) {
      return;
    }
    setLoading(true);
    setErrorMessage('');
    setSuccessMessage('');
    setShowNotification(false);

    try {
      if (menuId) {
        await updateMenu(menuId, menu);
        setSuccessMessage('Menu updated successfully');
        setShowNotification(true);
        setTimeout(() => {
          navigate('/menu');
        }, 2000);
      }
    } catch (error) {
      const apiError = error as ApiError;
      setErrorMessage(`Error updating menu: ${apiError.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="edit-menu-form">
      <h3 className="text-center">
        Menu &nbsp;
        <small className="text-muted">Edit Form</small>
      </h3>
      {loading && <div className="loader">Loading...</div>}
      <br />
      <div className="container">
        <form onSubmit={handleSubmit} className="text-center">
          <div className="row">
            <div className="col-4">
              <div className="form-group">
                <label>Name</label>
                <input
                  type={'text'}
                  name={'dishName'}
                  placeholder={'Enter dish name'}
                  className={'form-control'}
                  value={menu.name}
                  onChange={e =>
                    setMenu(prev => ({
                      ...prev,
                      name: e.target.value,
                    }))
                  }
                  required
                />
                {error.name && <div className="text-danger">{error.name}</div>}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Description</label>
                <input
                  type={'text'}
                  name={'dishDescription'}
                  placeholder={'Enter dish description'}
                  className={'form-control'}
                  value={menu.description}
                  onChange={e =>
                    setMenu(prev => ({
                      ...prev,
                      description: e.target.value,
                    }))
                  }
                  required
                />
                {error.description && (
                  <div className="text-danger">{error.description}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Price</label>
                <input
                  type={'number'}
                  name={'dishPrice'}
                  placeholder={'Enter dish price'}
                  className={'form-control'}
                  value={menu.price}
                  onChange={e =>
                    setMenu(prev => ({
                      ...prev,
                      price: parseInt(e.target.value, 10),
                    }))
                  }
                  required
                />
                {error.price && (
                  <div className="text-danger">{error.price}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Category</label>
                <input
                  type={'text'}
                  name={'dishCategory'}
                  placeholder={'Enter dish category'}
                  className={'form-control'}
                  value={menu.category}
                  onChange={e =>
                    setMenu(prev => ({
                      ...prev,
                      category: e.target.value,
                    }))
                  }
                  required
                />
                {error.category && (
                  <div className="text-danger">{error.category}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Image</label>
                <input
                  type={'text'}
                  name={'dishImage'}
                  placeholder={'Enter dish image'}
                  className={'form-control'}
                  value={menu.itemImage}
                  onChange={e =>
                    setMenu(prev => ({
                      ...prev,
                      itemImage: e.target.value,
                    }))
                  }
                  required
                />
                {error.itemImage && (
                  <div className="text-danger">{error.itemImage}</div>
                )}
              </div>
            </div>
            <div className="col-4">
              <div className="form-group">
                <label>Status</label>
                <select
                  name="status"
                  className="form-control"
                  value={menu.status}
                  onChange={e =>
                    setMenu(prev => ({
                      ...prev,
                      status: e.target.value as Status,
                    }))
                  }
                  required
                >
                  <option value={Status.AVAILABLE}>Available</option>
                  <option value={Status.NOT_AVAILABLE}>Not Available</option>
                </select>
                {error.status && (
                  <div className="text-danger">{error.status}</div>
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

export default UpdateMenu;
