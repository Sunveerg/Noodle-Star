import React, { useState } from 'react';
import styles from './css/Navigation.module.css';
import { PathRoutes } from '../path.routes';
import { NavLink } from 'react-router-dom';

const navigationItems = [
  { label: 'Home', path: PathRoutes.HomePage },
  { label: 'About Us', path: PathRoutes.AboutUsPage },
  { label: 'Menu', path: '/menu' },
  { label: 'Order', path: '/menuOrder' },
  { label: 'Contact', path: '/contact' },
  { label: 'Account', path: '/profile' },
];

export const NavBar: React.FC = () => {
  const [loading, setLoading] = useState(false);

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleLoginRedirect = () => {
    setLoading(true);
    const audience = 'https://dev-5kbvxb8zgblo1by3.us.auth0.com/api/v2/';
    const clientId = process.env.REACT_APP_AUTH0_CLIENT_ID;

    // Redirect the user to the Auth0 Universal Login page
    window.location.href =
      `https://dev-5kbvxb8zgblo1by3.us.auth0.com/authorize?` +
      `response_type=token&` +
      `client_id=${clientId}&` + // Your Auth0 Client ID
      `redirect_uri=http://localhost:3000/callback&` + // The redirect URL after login
      `scope=openid profile email read:current_user read:roles&` + // Scope to get user information
      `audience=${audience}&` + // Specify the audience for access token
      `prompt=login`; // Force the login page to prompt user credentials
  };

  return (
    <nav
      className={styles.navBackground}
      role="navigation"
      aria-label="Main navigation"
    >
      <div className={styles.navContainer}>
        <div className={styles.logo}>Noodle Star</div>
        <div className={styles.navItems} role="menubar">
          {navigationItems.map(item => (
            <NavLink
              key={item.label}
              to={item.path}
              className={({ isActive }) =>
                `${styles.navItem} ${isActive ? styles.activeNavItem : ''}`
              }
              role="menuitem"
            >
              {item.label}
            </NavLink>
          ))}
          {/* Add the Login item as a button */}
          <button
            onClick={handleLoginRedirect}
            disabled={loading}
            className={styles.navItem}
          >
            {loading ? 'Redirecting to Auth0...' : 'Login'}
          </button>
        </div>
      </div>
    </nav>
  );
};
