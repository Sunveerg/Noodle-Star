import * as React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { menuResponseModel } from './model/menuResponseModel';
import { getAllmenu } from './api/getAllMenu';
import noodleImg from '../components/assets/noodle.png';
import './Menu.css';
import AddDish from '../components/AddDish'; // Ensure AddDish is properly defined
import { Button } from 'react-bootstrap';

const MenuList: React.FC = (): JSX.Element => {
    const [menuItems, setMenuItems] = useState<menuResponseModel[]>([]);
    const [formVisible, setFormVisible] = useState<boolean>(false);
    const navigate = useNavigate();

    // Fetch menu data on component mount
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

        fetchMenuData().catch((error) =>
            console.error('Error in fetchMenuData:', error)
        );
    }, []);

    return (
        <div className="titleSection">
            <h2 className="mainTitle">
                Our Menu{' '}
                <img src={noodleImg} alt="Noodle" style={{width: '50px', height: '50px'}}/>
            </h2>


            <div className="vet-details-container" style={{marginBottom: '20px', textAlign: 'right'}}>
                <button
                    onClick={() => setFormVisible(prev => !prev)}
                    style={{
                        backgroundColor: formVisible ? '#ff6347' : '#4CAF50',
                        padding: '10px 20px',
                        borderRadius: '5px',
                        border: 'none',
                        color: 'white',
                    }}
                >
                    {formVisible ? 'Cancel' : 'Add Dish'}
                </button>

                {formVisible && (
                    <div onClick={() => setFormVisible(false)}>
                        <div onClick={(e) => e.stopPropagation()}>
                            <button
                                onClick={() => setFormVisible(false)}
                            >
                                &times;
                            </button>

                            <h2>Add a New Dish</h2>
                            <AddDish onClose={() => setFormVisible(false)}/>
                        </div>
                    </div>
                )}
            </div>
            <div className="menu-list">
                {menuItems.length > 0 ? (
                    menuItems.map((item) => (
                        <div className="menu-item" key={item.menuId}>
                            <div className="menu-item-content">
                                <div className="menu-image">
                                    <img src={item.itemImage} alt={item.name}/>
                                    <h3 className="menu-name">{item.name}</h3>
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
                                </div>
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