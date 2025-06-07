import { defineConfig } from 'vite'
import tsconfigPaths from 'vite-tsconfig-paths'
import viteTsconfigPaths from 'vite-tsconfig-paths'
import path from 'path'

import { fileURLToPath } from 'node:url'
import { dirname } from 'node:path'

const __filename = fileURLToPath(import.meta.url)
const __dirname = dirname(__filename)
export default defineConfig({
  plugins: [tsconfigPaths(), viteTsconfigPaths()],
  server: { port: 3000 },
  resolve: {
    alias: [
      {
        find: '@',
        replacement: path.resolve(__dirname, './src'),
      },
    ],
  },
})
