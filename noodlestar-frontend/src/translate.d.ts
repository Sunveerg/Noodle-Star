declare global {
  interface GoogleTranslateOptions {
    pageLanguage: string;
    autoDisplay: boolean;
    includedLanguages: string;
    layout?: number;
  }

  interface TranslateElement {
    new (
      options: GoogleTranslateOptions,
      elementId: string
    ): TranslateElementInstance;
  }

  // eslint-disable-next-line @typescript-eslint/no-empty-interface
  interface TranslateElementInstance {}

  interface GoogleTranslate {
    TranslateElement: TranslateElement;
    InlineLayout: {
      SIMPLE: number;
      HORIZONTAL: number;
      VERTICAL: number;
    };
  }

  interface Window {
    google: {
      translate: GoogleTranslate;
    };
    googleTranslateElementInit: () => void;
  }
}

export {};
