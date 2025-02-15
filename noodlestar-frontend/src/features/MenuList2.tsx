/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { menuResponseModel } from './model/menuResponseModel';
import { getAllmenu } from './api/getAllMenu';
import noodleImg from '../components/assets/noodle.png';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Menu.css';
import AddDish from '../components/AddDish';
import { deleteMenuItem } from './api/deleteMenuItem';

const MenuList: React.FC = (): JSX.Element => {
  const [menuItems, setMenuItems] = useState<menuResponseModel[]>([]);
  const [filteredItems, setFilteredItems] = useState<menuResponseModel[]>([]); // for filtered items
  const [isStaff, setIsStaff] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null); // track the selected category
  const navigate = useNavigate();

  useEffect(() => {
    const fetchUserRoles = async () => {
      const accessToken = localStorage.getItem('access_token');
      if (!accessToken) {
        console.error('No access token found');
        setIsStaff(false);
        return;
      }

      try {
        const base64Url = accessToken.split('.')[1];
        const decodedPayload = JSON.parse(atob(base64Url));
        const roles = decodedPayload['https://noodlestar/roles'] || [];
        setIsStaff(roles.includes('Staff')); // Check if the user has the "Staff" role
      } catch (err) {
        console.error('Error decoding user roles:', err);
        setIsStaff(false);
      }
    };

    const fetchMenuData = async (): Promise<void> => {
      try {
        setLoading(true);
        const response = await getAllmenu();
        if (Array.isArray(response)) {
          setMenuItems(response);
          setFilteredItems(response); // initially show all items
        } else {
          console.error('Fetched data is not an array:', response);
        }
      } catch (error) {
        console.error('Error fetching menu items:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUserRoles();
    fetchMenuData();
  }, []);

  const handleMenuItemClick = (menuId: number): void => {
    navigate(`/menu/${menuId}`);
  };

  const handleDelete = async (menuId: number) => {
    const confirmDelete = window.confirm(
      'Are you sure you want to delete this menu item?'
    );
    if (!confirmDelete) return;

    try {
      await deleteMenuItem(menuId);
      setMenuItems(prevItems =>
        prevItems.filter(item => item.menuId !== menuId)
      );
      setFilteredItems(prevItems =>
        prevItems.filter(item => item.menuId !== menuId)
      ); // Remove from filtered list as well
      alert('Menu item deleted successfully!');
    } catch (error) {
      console.error('Error deleting menu item:', error);
      alert('Failed to delete the menu item. Please try again.');
    }
  };

  const handleCategoryClick = (category: string) => {
    if (selectedCategory === category) {
      setSelectedCategory(null); // Reset filter if the same category is clicked
      setFilteredItems(menuItems); // Show all items
    } else {
      setSelectedCategory(category); // Apply filter for the selected category
      setFilteredItems(menuItems.filter(item => item.category === category)); // Filter by category
    }
  };

  if (loading) {
    return <div>Loading menu items...</div>;
  }

  const categories = Array.from(new Set(menuItems.map(item => item.category))); // Extract unique categories

  return (
    <div className="titleSection">
      <h2 className="pageTitle">
        Our Menu{' '}
        <img
          src={noodleImg}
          alt="Noodle"
          style={{ width: '100px', height: '100px' }}
        />
      </h2>

      {/* Show the Add Dish button only if the user is a staff member */}
      {isStaff && <AddDish />}

      {/* Category filter section */}
      <div className="category-filters">
        {categories.map(category => (
          <button
            key={category}
            className={`category-button ${selectedCategory === category ? 'active' : ''}`}
            onClick={() => handleCategoryClick(category)}
          >
            {category}
          </button>
        ))}
      </div>

      <div className="menu-list">
        <div className="cloud-container">
          <div className="cloud4"></div>
          <div className="cloud5"></div>
          <div className="cloud6"></div>
        </div>

        <div className="topRightImage"></div>

        {filteredItems.length > 0 ? (
          filteredItems.map(item => (
            <div
              className="menu-item"
              key={item.menuId}
              onClick={e => {
                e.stopPropagation();
                handleMenuItemClick(item.menuId);
              }}
            >
              <div className="menu-item-content">
                <div className="menu-image">
                  <h3 className="menu-name">{item.name}</h3>
                  <img src={item.itemImage} alt={item.name} />
                </div>
                <div className="menu-details">
                  <p className="menu-price">{item.price}$</p>
                  <p className="menu-description">{item.description}</p>
                  <p
                    className={`menu-status ${
                      item.status === 'AVAILABLE'
                        ? 'available'
                        : 'not-available'
                    }`}
                  >
                    {item.status}
                  </p>

                  {/* Show the Delete button only if the user is a staff member */}
                  {isStaff && (
                    <button
                      className="btn-delete"
                      onClick={e => {
                        e.stopPropagation();
                        handleDelete(item.menuId);
                      }}
                    >
                      Delete
                    </button>
                  )}
                </div>

                {/* Show the Edit button only if the user is a staff member */}
                {isStaff && (
                  <button
                    onClick={e => {
                      e.stopPropagation();
                      navigate(`${item.menuId}/update`);
                    }}
                    className="btn-edit"
                  >
                    Edit
                  </button>
                )}
              </div>
            </div>
          ))
        ) : (
          <p className="no-items">No menu items available</p>
        )}
      </div>
    </div>
  );
};

export default MenuList;
