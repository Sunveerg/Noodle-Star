import * as React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { menuResponseModel } from './model/menuResponseModel';
import { getAllmenu } from './api/getAllMenu';
import noodleImg from '../components/assets/noodle.png';
import 'bootstrap/dist/css/bootstrap.min.css';
import './Menu.css';
import AddDish from "../components/AddDish.tsx";
import {deleteMenuItem} from "./api/deleteMenuItem.ts";

const MenuList: React.FC = (): JSX.Element => {
    const [menuItems, setMenuItems] = useState<menuResponseModel[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchMenuData = async (): Promise<void> => {
            try {
                const response = await getAllmenu();
                if (Array.isArray(response)) {
                    setMenuItems(response);
                } else {
                    console.error('Fetched data is not an array:', response);
                }
            } catch (error) {
                console.error('Error fetching menu items:', error);
            }
        };

        fetchMenuData().catch(error =>
            console.error('Error in fetchMenuData:', error)
        );
    }, []);

    const handleMenuItemClick = (menuId: number): void => {
        navigate(`/menu/${menuId}`);
    };

    const handleDelete = async (menuId: number) => {
        const confirmDelete = window.confirm(
            "Are you sure you want to delete this menu item?"
        );

        if (!confirmDelete) return;

        try {
            await deleteMenuItem(menuId);
            setMenuItems((prevItems) =>
                prevItems.filter((item) => item.menuId !== menuId)
            );
            alert("Menu item deleted successfully!");
        } catch (error) {
            console.error("Error deleting menu item:", error);
            alert("Failed to delete the menu item. Please try again.");
        }
    };

    return (
        <div className="titleSection">
            <h2 className="pageTitle">Our Menu <img
                src={noodleImg}
                alt="Noodle"
                style={{width: '100px', height: '100px'}}
            /></h2>

            <AddDish/>

            <div className="menu-list">
                <div className="cloud-container">
                    <div className="cloud4"></div>
                    <div className="cloud5"></div>
                    <div className="cloud6"></div>
                </div>

                <div className="topRightImage"></div>

                {menuItems.length > 0 ? (
                    menuItems.map(item => (
                        <div className="menu-item" key={item.menuId} onClick={e => {
                          e.stopPropagation();
                          handleMenuItemClick(item.menuId);
                        }}>
                            <div className="menu-item-content">
                                <div className="menu-image">
                                    <h3 className="menu-name">{item.name}</h3>
                                    <img src={item.itemImage} alt={item.name}/>
                                </div>
                                <div className="menu-details">
                                <p className="menu-price">{item.price}$</p>
                                    <p className="menu-description">{item.description}</p>
                                    <p
                                        className={`menu-status ${
                                            item.status === 'AVAILABLE' ? 'available' : 'not-available'
                                        }`}
                                    >
                                        {item.status}
                                    </p>
                                    <button
                                        className="btn-delete"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            handleDelete(item.menuId);
                                        }}
                                    >
                                        Delete
                                    </button>
                                </div>
                                <button
                                    onClick={e => {
                                        e.stopPropagation();
                                        navigate(`${item.menuId}/update`);
                                    }}
                                    className="btn-edit"
                                >
                                    Edit
                                </button>
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
