import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { menuResponseModel } from './model/menuResponseModel';
import { getMenuItemById } from './api/getMenuItemById';
import './MenuDetails.css';

const MenuDetails: React.FC = (): JSX.Element => {
  const { menuId } = useParams<{ menuId: string }>();
  const [menuItem, setMenuItem] = useState<menuResponseModel | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMenuItem = async (): Promise<void> => {
      try {
        if (!menuId) {
          throw new Error('Menu ID is missing');
        }
        setLoading(true);
        const response = await getMenuItemById(menuId);
        setMenuItem(response);
      } catch (err: any) {
        setError(err.message || 'Failed to fetch menu item');
      } finally {
        setLoading(false);
      }
    };

    fetchMenuItem();
  }, [menuId]);

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p>Error: {error}</p>;
  }

  if (!menuItem) {
    return <p>No menu item found.</p>;
  }

  return (
    <div className="menu-details-container">
      <button className="btn-back" onClick={() => navigate(-1)}>
        Go Back
      </button>
      <h2 className="menu-title">{menuItem.name}</h2>
      <div className="menu-item-details">
        <img
          src={menuItem.itemImage}
          alt={menuItem.name}
          className="menu-item-image"
        />
        <div className="menu-item-info">
          <p>
            <strong>Price:</strong> ${menuItem.price}
          </p>
          <p>
            <strong>Description:</strong> {menuItem.description}
          </p>
          <p>
            <strong>Category:</strong> {menuItem.category}
          </p>
          <p className={`menu-status ${menuItem.status.toLowerCase()}`}>
            <strong>Status:</strong> {menuItem.status}
          </p>
        </div>
      </div>
    </div>
  );
};

export default MenuDetails;
