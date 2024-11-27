import React from 'react';
import styles from './css/AboutUs.module.css';
import { TextBlock } from './TextBlock';

const textBlocks = [
    {
        title: 'History',
        content: 'Lorem ipsum dolor sit amet...'
    },
    {
        title: 'Team members',
        content: 'Lorem ipsum dolor sit amet...'
    },
    {
        title: 'Mission & Vision Statements',
        content: 'Lorem ipsum dolor sit amet...'
    }
];

export const AboutUs: React.FC = () => {
    return (
        <main className={styles.aboutUs}>

            <section className={styles.content}>
                <div className={styles.titleSection}>
                    <h2 className={styles.mainTitle}>About Us</h2>
                    <img
                        src="https://png.pngtree.com/png-vector/20240322/ourmid/pngtree-instant-fried-noodles-png-image_12178744.png"
                        alt="About Us decorative icon"
                        className={styles.titleIcon}
                    />
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

                <aside className={styles.sideContent}>
                    <h3 className={styles.chineseTitle}>舌尖上的中国</h3>
                </aside>
            </section>
        </main>
    );
};
