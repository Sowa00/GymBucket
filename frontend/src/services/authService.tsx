import { API_BASE_URL, API_TIMEOUT } from '../config/config.tsx'

// Types
export interface LoginRequest {
  email: string
  password: string
  rememberMe?: boolean
}

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  role: 'trainer' | 'admin'
  profilePicture?: string
}

export interface LoginResponse {
  success: boolean
  message: string
  data?: {
    token: string
    refreshToken?: string
    user: User
    expiresIn: number
  }
  errors?: string[]
}

export interface ApiError {
  message: string
  status: number
  code?: string
}

// Custom error class
export class AuthError extends Error {
  status: number
  code?: string

  constructor(message: string, status: number, code?: string) {
    super(message)
    this.name = 'AuthError'
    this.status = status
    this.code = code
  }
}

// Auth Service Class
class AuthService {
  private readonly tokenKey = 'gymbucket_token'
  private readonly refreshTokenKey = 'gymbucket_refresh_token'
  private readonly userKey = 'gymbucket_user'

  /**
   * Login user with email and password
   */
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    try {
      const controller = new AbortController()
      const timeoutId = setTimeout(() => controller.abort(), API_TIMEOUT)

      const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify(credentials),
        signal: controller.signal,
      })

      clearTimeout(timeoutId)
      const data: LoginResponse = await response.json()

      if (!response.ok) {
        throw new AuthError(data.message || 'Login failed', response.status, data.errors?.[0])
      }

      // Store tokens and user data if login successful
      if (data.success && data.data) {
        this.storeAuthData(data.data, credentials.rememberMe)
      }

      return data
    } catch (error) {
      if (error instanceof AuthError) {
        throw error
      }

      // Handle network errors
      if (error instanceof TypeError && error.message.includes('fetch')) {
        throw new AuthError(
          'Unable to connect to server. Please check your internet connection.',
          0,
          'NETWORK_ERROR',
        )
      }

      // Handle other errors
      throw new AuthError('An unexpected error occurred. Please try again.', 500, 'UNKNOWN_ERROR')
    }
  }

  /**
   * Logout user and clear stored data
   */
  async logout(): Promise<void> {
    try {
      const token = this.getToken()

      if (token) {
        // Call logout endpoint to invalidate token on server
        await fetch(`${API_BASE_URL}/auth/logout`, {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        })
      }
    } catch (error) {
      // Continue with logout even if server call fails
      console.warn('Server logout failed:', error)
    } finally {
      // Always clear local storage
      this.clearAuthData()
    }
  }

  /**
   * Refresh access token using refresh token
   */
  async refreshToken(): Promise<string | null> {
    try {
      const refreshToken = this.getRefreshToken()

      if (!refreshToken) {
        throw new AuthError('No refresh token available', 401)
      }

      const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ refreshToken }),
      })

      const data = await response.json()

      if (!response.ok) {
        throw new AuthError(data.message || 'Token refresh failed', response.status)
      }

      // Update stored token
      if (data.token) {
        this.storeToken(data.token)
        return data.token
      }

      return null
    } catch (error) {
      // Clear auth data if refresh fails
      this.clearAuthData()
      throw error
    }
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): boolean {
    const token = this.getToken()
    const user = this.getCurrentUser()

    if (!token || !user) {
      return false
    }

    // Check if token is expired (basic check)
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      const currentTime = Date.now() / 1000

      return payload.exp > currentTime
    } catch {
      return false
    }
  }

  /**
   * Get current user from storage
   */
  getCurrentUser(): User | null {
    try {
      const userStr = localStorage.getItem(this.userKey) || sessionStorage.getItem(this.userKey)
      return userStr ? JSON.parse(userStr) : null
    } catch {
      return null
    }
  }

  /**
   * Get stored token
   */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey) || sessionStorage.getItem(this.tokenKey)
  }

  /**
   * Get authorization header for API calls
   */
  getAuthHeader(): Record<string, string> {
    const token = this.getToken()
    return token ? { Authorization: `Bearer ${token}` } : ({} as Record<string, string>)
  }

  /**
   * Create authenticated fetch wrapper
   */
  async authenticatedFetch(url: string, options: RequestInit = {}): Promise<Response> {
    const token = this.getToken()

    if (!token) {
      throw new AuthError('No authentication token available', 401)
    }

    // Create headers object properly
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...this.getAuthHeader(),
    }

    // Add any existing headers from options
    if (options.headers) {
      if (options.headers instanceof Headers) {
        options.headers.forEach((value, key) => {
          headers[key] = value
        })
      } else if (Array.isArray(options.headers)) {
        options.headers.forEach(([key, value]) => {
          headers[key] = value
        })
      } else {
        Object.assign(headers, options.headers)
      }
    }

    const response = await fetch(url, {
      ...options,
      headers,
    })

    // Handle token expiration
    if (response.status === 401) {
      try {
        // Try to refresh token
        const newToken = await this.refreshToken()
        if (newToken) {
          // Create new headers with refreshed token
          const newHeaders: Record<string, string> = {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${newToken}`,
          }

          // Add any existing headers from options again
          if (options.headers) {
            if (options.headers instanceof Headers) {
              options.headers.forEach((value, key) => {
                newHeaders[key] = value
              })
            } else if (Array.isArray(options.headers)) {
              options.headers.forEach(([key, value]) => {
                newHeaders[key] = value
              })
            } else {
              Object.assign(newHeaders, options.headers)
            }
          }

          // Retry request with new token
          return fetch(url, {
            ...options,
            headers: newHeaders,
          })
        }
      } catch {
        // Refresh failed, redirect to login
        this.clearAuthData()
        window.location.href = '/login'
      }
    }

    return response
  }

  /**
   * Get stored refresh token
   */
  private getRefreshToken(): string | null {
    return (
      localStorage.getItem(this.refreshTokenKey) || sessionStorage.getItem(this.refreshTokenKey)
    )
  }

  /**
   * Store authentication data
   */
  private storeAuthData(authData: NonNullable<LoginResponse['data']>, rememberMe?: boolean): void {
    const storage = rememberMe ? localStorage : sessionStorage

    storage.setItem(this.tokenKey, authData.token)
    storage.setItem(this.userKey, JSON.stringify(authData.user))

    if (authData.refreshToken) {
      storage.setItem(this.refreshTokenKey, authData.refreshToken)
    }
  }

  /**
   * Store just the token (for refresh)
   */
  private storeToken(token: string): void {
    const hasLocalStorage = localStorage.getItem(this.tokenKey)
    const storage = hasLocalStorage ? localStorage : sessionStorage
    storage.setItem(this.tokenKey, token)
  }

  /**
   * Clear all authentication data
   */
  private clearAuthData(): void {
    // Clear from both storages
    ;[localStorage, sessionStorage].forEach((storage) => {
      storage.removeItem(this.tokenKey)
      storage.removeItem(this.refreshTokenKey)
      storage.removeItem(this.userKey)
    })
  }
}

// Export singleton instance
export const authService = new AuthService()

// Export default
export default authService
