import React from 'react';
import styles from './css/HomePage.module.css';
import dragonImg from './assets/dragon.png';
import {useNavigate} from "react-router-dom";

export const Home: React.FC = () => {
    const navigate = useNavigate();  

    const handleClick = () => {
        navigate('/menuOrder');
    };

    const handleReviewClick = () => {
        navigate('/review');
    };

    return (
        <main className={styles.homePage}>
            {/* Left Section: Text Content */}
            <div className={styles.welcomeText}>
                <h4>Welcome</h4>
                <h1>
                    Noodle <br /> Star
                </h1>
                <p className={styles.paragraph}>
                    Delve into the rich flavors of authentic Chinese cuisine, ready for you to savor and enjoy.
                </p>
                <button className={styles.button} onClick={handleClick}>
                    Order Now
                </button>
            </div>

            {/* Right Section: Dragon Image */}
            <img className={styles.dragonImg} src={dragonImg} alt="Dragon" />

            <div className={styles.cloudThree}></div>
            <div className={styles.cloudFour}></div>
            <div className={styles.cloudFive}></div>
            <div className={styles.cloudSix}></div>
            <div className={styles.topRightImage}></div>

            <button
                className={styles.reviewButton}
                onClick={handleReviewClick}
            >
                Go to Reviews
            </button>
        </main>
    );
};
