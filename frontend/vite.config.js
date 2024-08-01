import { fileURLToPath, URL } from 'node:url'

import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vitejs.dev/config/
export default defineConfig({
  // plugins: [vue(), vueDevTools()],
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 3000
  },

  define: {
    global: 'window',
    'process.env': process.env
  },

  optimizeDeps: {
    exclude: ['@ffmpeg/ffmpeg']
  }
})


