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
  const [currentLanguage, setCurrentLanguage] = useState('EN'); // State to track current language

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleLoginRedirect = () => {
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

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  const handleLanguageChange = (lang: string) => {
    const googleTranslateElement = document.querySelector(
      '.goog-te-combo'
    ) as HTMLSelectElement;

    if (googleTranslateElement) {
      googleTranslateElement.value = lang;
      googleTranslateElement.dispatchEvent(new Event('change'));
      setCurrentLanguage(lang === 'en' ? 'EN' : 'FR'); // Update the language state
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
      <div id="google_translate_element" style={{ display: 'none' }}></div>{' '}
      {/* Hidden Google Translate element */}
      <div className={styles.navContainer}>
        <div className={`${styles.logo} notranslate`}>Noodle Star</div>
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
            <button className={styles.navItem}>
              {currentLanguage} {/* Display current language */}
            </button>
            <div className={styles.dropdownContent}>
              <button onClick={() => handleLanguageChange('en')}>
                English
              </button>
              <button onClick={() => handleLanguageChange('fr')}>French</button>
            </div>
          </div>
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
