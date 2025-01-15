import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import federation from '@originjs/vite-plugin-federation';

export default defineConfig({
  plugins: [
    react(),
    federation({
      name: 'noodlestar-frontend',
      shared: ['react', 'react-dom'],
    }),
  ],
  resolve: {
    alias: {
      '@': '/src',
    },
  },
  envDir: 'src/environments/',
  build: {
    modulePreload: false,
    target: 'esnext',
    assetsDir: 'src/assets',
  },
});
