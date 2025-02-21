/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React from 'react';
import styles from './css/HomePage.module.css';
import dragonImg from './assets/dragon.png';
import { useNavigate } from 'react-router-dom';

export const Home: React.FC = () => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate('/menuOrder');
  };

  return (
    <main className={styles.homePage}>
      {/* Logo Section */}
      <div className={styles.logoContainer}>
        <img
          src="https://i.postimg.cc/WbzK9ttF/PART-1738873837223-removebg-preview.png"
          alt="Restaurant Logo"
        />
      </div>

      {/* Content Wrapper for Center Alignment */}
      <div className={styles.contentWrapper}>
        {/* Left Section: Text Content */}
        <div className={styles.welcomeText}>
          <h4 translate="yes">Welcome to our restaurant</h4>

          <h1 className="notranslate">
            Nouilles <br /> Star
          </h1>

          <p className={styles.paragraph} translate="yes">
            Delve into the rich flavors of authentic Chinese cuisine, ready for
            you to savor and enjoy.
          </p>
          <button className={styles.button} onClick={handleClick}>
            Order Now
          </button>
        </div>

        {/* Right Section: Dragon Image */}
        <img className={styles.dragonImg} src={dragonImg} alt="Dragon" />
        {/* Image Gallery Section */}
        <div className={styles.imageGallery}>
          <img src="https://i.postimg.cc/ryjvwYf5/348s.jpg" alt="Dish 1" />
          <img
            src="https://i.postimg.cc/2ykMx4Sq/2017-01-21.jpg"
            alt="Dish 2"
          />
          <img
            src="https://i.postimg.cc/vHmCgLKk/432787672-3532334483745465-8964064027290058302-n.jpg"
            alt="Dish 3"
          />
          <img src="https://i.postimg.cc/GhSVkTw2/DSC-0104.jpg" alt="Dish 4" />
        </div>
      </div>
    </main>
  );
};
