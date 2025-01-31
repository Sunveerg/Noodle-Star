/* eslint-disable no-console */
import { useEffect } from 'react';

export default function GoogleTranslateLoader(): null {
  useEffect(() => {
    // Add Google Translate script
    const addScript = document.createElement('script');
    addScript.setAttribute(
      'src',
      'https://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit'
    );
    addScript.async = true;
    addScript.defer = true;
    document.body.appendChild(addScript);

    // Define the Google Translate initialization function globally
    window.googleTranslateElementInit = () => {
      console.log('Initializing Google Translate');
      new window.google.translate.TranslateElement(
        {
          pageLanguage: 'en',
          autoDisplay: false,
          includedLanguages: 'en,fr', // Specify supported languages
        },
        'google_translate_element'
      );
    };

    // Add CSS to hide the toolbar
    const style = document.createElement('style');
    style.innerHTML = `
      .goog-te-banner-frame {
          display: none !important;
      }
      iframe.goog-te-banner-frame {
          display: none !important;
      }
      body {
          top: 0 !important;
      }
      body > .skiptranslate {
        display: none;
    }
    `;
    document.head.appendChild(style);

    // Use MutationObserver to dynamically hide the iframe
    const observer = new MutationObserver(() => {
      const iframe = document.querySelector(
        'iframe.goog-te-banner-frame'
      ) as HTMLIFrameElement; // Type assertion to ensure TypeScript recognizes iframe
      if (iframe) {
        iframe.style.display = 'none'; // Safely access 'style'
        console.log('Google Translate toolbar iframe hidden.');
      }
    });

    // Observe DOM changes to handle dynamic iframe rendering
    observer.observe(document.body, { childList: true, subtree: true });

    // Cleanup observer on component unmount
    return () => {
      observer.disconnect();
    };
  }, []);

  return null; // This component doesn't render anything
}
