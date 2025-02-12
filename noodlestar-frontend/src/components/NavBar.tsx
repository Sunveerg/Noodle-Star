import React, { useState, useEffect } from 'react';
import styles from './css/Navigation.module.css';
import { PathRoutes } from '../path.routes';
import { NavLink } from 'react-router-dom';

const navigationItems = [
  { label: 'Home', path: PathRoutes.HomePage },
  { label: 'About Us', path: PathRoutes.AboutUsPage },
  { label: 'Menu', path: '/menu' },
  { label: 'Order', path: '/menuOrder' },
  { label: 'Account', path: '/profile' },
];

export const NavBar: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [currentLanguage, setCurrentLanguage] = useState('EN');

  useEffect(() => {
    const token = localStorage.getItem('access_token');
    setIsAuthenticated(!!token);
  }, []);

  const handleLoginRedirect = (): void => {
    setLoading(true);
    const audience = 'https://dev-5kbvxb8zgblo1by3.us.auth0.com/api/v2/';
    const clientId = process.env.REACT_APP_AUTH0_CLIENT_ID;
    const redirectUri = process.env.REACT_APP_AUTH0_CALLBACK_URL;

    window.location.href =
      `https://dev-5kbvxb8zgblo1by3.us.auth0.com/authorize?` +
      `response_type=token&` +
      `client_id=${clientId}&` +
      `redirect_uri=${redirectUri}&` +
      `scope=openid profile email read:current_user read:roles&` +
      `audience=${audience}&` +
      `prompt=login`;
  };

  const handleLogout = (): void => {
    localStorage.removeItem('access_token');
    sessionStorage.removeItem('access_token');
    setIsAuthenticated(false);
    window.location.reload();
  };

  const handleLanguageChange = (lang: string): void => {
    const googleTranslateElement = document.querySelector(
      '.goog-te-combo'
    ) as HTMLSelectElement;

    if (googleTranslateElement) {
      googleTranslateElement.value = lang;
      googleTranslateElement.dispatchEvent(new Event('change'));
      setCurrentLanguage(lang === 'en' ? 'EN' : 'FR');
    } else {
      console.error('Google Translate dropdown not found');
    }
  };

  return (
    <nav
      className={styles.navBackground}
      role="navigation"
      aria-label="Main navigation"
    >
      <div id="google_translate_element" style={{ display: 'none' }}></div>
      <div className={styles.navContainer}>
        <div className={`${styles.logo} notranslate`}>Nouilles Star</div>
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
          <div className={styles.dropdown}>
            <button className={styles.navItem}>{currentLanguage}</button>
            <div className={styles.dropdownContent}>
              <button onClick={() => handleLanguageChange('en')}>
                English
              </button>
              <button onClick={() => handleLanguageChange('fr')}>French</button>
            </div>
          </div>
          {loading ? (
            <button className={styles.navItem} disabled>
              Redirecting to Auth0...
            </button>
          ) : isAuthenticated ? (
            <button onClick={handleLogout} className={styles.navItem}>
              Logout
            </button>
          ) : (
            <button onClick={handleLoginRedirect} className={styles.navItem}>
              Login
            </button>
          )}
        </div>
      </div>
    </nav>
  );
};
