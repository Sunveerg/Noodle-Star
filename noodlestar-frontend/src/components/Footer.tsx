/* eslint-disable prettier/prettier */
/* eslint-disable @typescript-eslint/explicit-function-return-type */
import React from 'react';
import styles from './css/Footer.module.css';
import { useNavigate } from 'react-router-dom';

export const Footer: React.FC = () => {
  const navigate = useNavigate();

  const handleUberEatsClick = () => {
    window.open(
      'https://www.ubereats.com/ca-fr/store/nouilles-star/4dgOlDWTUauXpap0GlatAg?srsltid=AfmBOopvtSQTJhwYru9IvwY9BfekLhvB7XNGKn9sPLoeu3P2QDJONgfB',
      '_blank'
    );
  };

  const handleFantuanClick = () => {
    window.open(
      'https://order.fantuan.ca/store/nouilles-star/ca-2545',
      '_blank'
    );
  };

  return (
    <footer className={styles.footerBackground}>
      <div className={styles.footerContainer}>
        {/* Logo and tagline */}
        <div className={styles.footerSection}>
          <h2 className={styles.logo}>Nouilles Star</h2>
          <p className={styles.tagline}>
            Humble eatery dishing out familiar Chinese fare, with a focus on noodle & rice dishes. 🍜
          </p>
          <p>1871 Sainte-Catherine O, Montreal, QC</p>
          <p>
            &copy; {new Date().getFullYear()} Noodle Star | All rights reserved
          </p>
        </div>

        {/* Contact Section */}
        <address className={styles.footerSection}>
          <h3>Contact Us</h3>
          <p>
            Phone: <a href="tel:+15149322888">(514) 932-2888</a>
          </p>
        </address>

        {/* Opening Hours */}
        <div className={styles.footerSection}>
          <h3>Opening Hours</h3>
          <p>Mon - Wed: 12PM - 11:30PM</p>
          <p>Thu - Sun: 12PM - 12AM</p>
        </div>

        {/* Grouping the Review and Delivery Buttons */}
        <div className={styles.deliveryButtonsGroup}>
          <div className={styles.deliveryButtons}>
            {/* Review Button */}
            <button
              className={styles.reviewButton}
              onClick={() => navigate('/review/add')}
              aria-label="Leave a review"
            >
              Leave a Review
            </button>

            {/* Delivery Buttons */}
            <button
              className={`${styles.deliveryButton} ${styles.ubereats}`}
              onClick={handleUberEatsClick}
            >
              UberEats
            </button>
            <button
              className={`${styles.deliveryButton} ${styles.fantuan}`}
              onClick={handleFantuanClick}
            >
              Fantuan
            </button>
          </div>
        </div>
      </div>
    </footer>
  );
};
