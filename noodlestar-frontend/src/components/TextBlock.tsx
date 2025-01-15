import React from 'react';
import styles from './css/AboutUs.module.css';
import { TextBlockProps } from './types';

export const TextBlock: React.FC<TextBlockProps> = ({ title, content }) => {
  return (
    <section className={styles.textBlock}>
      <h2 className={styles.blockTitle}>{title}</h2>
      <p className={styles.blockContent}>{content}</p>
    </section>
  );
};
