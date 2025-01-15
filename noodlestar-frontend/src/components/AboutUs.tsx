import React from 'react';
import styles from './css/AboutUs.module.css';
import { TextBlock } from './TextBlock';
import noodleImg from './assets/noodle.png';


const textBlocks = [
    {
        title: 'History',
        content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
    },
    {
        title: 'Team members',
        content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
    },
    {
        title: 'Mission & Vision Statements',
        content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.'
    }
];

export const AboutUs: React.FC = () => {
    return (
        <main className={styles.aboutUs}>

            <div className={styles.cloud3}></div>
            <div className={styles.cloud4}></div>
            <div className={styles.cloud5}></div>
            <div className={styles.cloud6}></div>

           <div className={styles.topRightImage}></div>

            <section className={styles.content}>
                <div className={styles.titlePart}>
                    <h2 className={styles.mainTitle}>About Us <img src={noodleImg} alt="Noodle"
                                                                   style={{width: '100px', height: '100px'}}/>
                    </h2>
                </div>


                <article className={styles.textBlocks}>
                {textBlocks.map((block) => (
                        <TextBlock
                            key={block.title}
                            title={block.title}
                            content={block.content}
                        />
                    ))}
                </article>

            </section>
        </main>
    );
};
