import React from 'react';
import styles from './css/Footer.module.css';
import { useNavigate } from 'react-router-dom';

export const Footer: React.FC = () => {
  const navigate = useNavigate();

  return (
    <footer className={styles.footerBackground}>
      <div className={styles.footerContainer}>
        {/* Logo and tagline */}
        <div className={styles.footerSection}>
          <h2 className={styles.logo}>Nouilles Star</h2>
          <p className={styles.tagline}>
            Humble eatery dishing out familiar Chinese fare, with a focus on
            noodle & rice dishes. 🍜
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

        {/* Review Button */}
        <div className={styles.footerSection}>
          <button
            className={styles.reviewButton}
            onClick={() => navigate('/review/add')}
            aria-label="Leave a review"
          >
            Leave a Review
          </button>
        </div>
      </div>
    </footer>
  );
};
