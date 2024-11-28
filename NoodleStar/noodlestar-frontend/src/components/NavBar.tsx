import React from 'react';
import styles from './css/Navigation.module.css';
import {PathRoutes} from "../path.routes";
import {NavLink} from "react-router-dom";

const navigationItems = [
    { label: 'Home', path: PathRoutes.HomePage },
    { label: 'About Us', path: PathRoutes.AboutUsPage },
    { label: 'Menu', path: '/menu' },
    { label: 'Order', path: '/order' },
    { label: 'Contact', path: '/contact' },
    { label: 'Account', path: '/account' }
];

export const NavBar: React.FC = () => {
    return (
        <nav className={styles.navBackground} role="navigation" aria-label="Main navigation">
            <div className={styles.navContainer}>
                <div className={styles.logo}>Noodle Star</div>
                <div className={styles.navItems} role="menubar">
                    {navigationItems.map((item) => (
                        <NavLink
                            key={item.label}
                            to={item.path}
                            className={({isActive}) =>
                                `${styles.navItem} ${isActive ? styles.activeNavItem : ''}`
                            }
                            role="menuitem"
                        >
                            {item.label}
                        </NavLink>
                    ))}
                </div>
            </div>
        </nav>
    );
};
