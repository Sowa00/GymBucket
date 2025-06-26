// src/config/config.ts
// Professional configuration management

export interface AppConfig {
  apiUrl: string
  environment: 'development' | 'staging' | 'production'
  apiTimeout: number
  features: {
    offlineMode: boolean
    debugMode: boolean
  }
}

// Environment-specific configurations
const configs: Record<string, AppConfig> = {
  development: {
    apiUrl: 'http://localhost:3001/api',
    environment: 'development',
    apiTimeout: 10000,
    features: {
      offlineMode: true,
      debugMode: true
    }
  },
  staging: {
    apiUrl: 'https://staging-api.gymbucket.com/api',
    environment: 'staging',
    apiTimeout: 8000,
    features: {
      offlineMode: false,
      debugMode: true
    }
  },
  production: {
    apiUrl: 'https://api.gymbucket.com/api',
    environment: 'production',
    apiTimeout: 5000,
    features: {
      offlineMode: false,
      debugMode: false
    }
  }
}

// Detect environment (you can change this logic)
const getCurrentEnvironment = (): string => {
  // You can set this based on hostname, build flags, etc.
  const hostname = typeof window !== 'undefined' ? window.location.hostname : 'localhost'

  if (hostname.includes('localhost') || hostname.includes('127.0.0.1')) {
    return 'development'
  } else if (hostname.includes('staging')) {
    return 'staging'
  } else {
    return 'production'
  }
}

// Export the current config
export const config: AppConfig = configs[getCurrentEnvironment()] || configs.development

// Export individual values for convenience
export const API_BASE_URL = config.apiUrl
export const ENVIRONMENT = config.environment
export const API_TIMEOUT = config.apiTimeout

// Debug helper
if (config.features.debugMode) {
  console.log('🔧 GymBucket Config:', config)
}