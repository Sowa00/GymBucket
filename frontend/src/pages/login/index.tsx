import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import {
  AlertCircle,
  ArrowRight,
  CheckCircle,
  Dumbbell,
  Eye,
  EyeOff,
  Lock,
  Mail,
} from 'lucide-react'
import { AuthError, authService } from '../../services/authService'
import styles from './styles.module.scss'

type FormData = {
  email: string
  password: string
  rememberMe?: boolean
}

function LoginPage() {
  console.log('LoginPage is rendering!') // ← Add this line

  const [showPassword, setShowPassword] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [loginError, setLoginError] = useState('')
  const [isOfflineMode, setIsOfflineMode] = useState(false)
  const navigate = useNavigate()

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm()

  // Alternative way to get values without type issues
  const [emailValue, setEmailValue] = useState('')
  const [passwordValue, setPasswordValue] = useState('')

  // Use these instead of watch
  const watchEmail = emailValue
  const watchPassword = passwordValue

  const handleLogin = async (data: FormData) => {
    setIsLoading(true)
    setLoginError('')

    try {
      // Try real API first
      const response = await authService.login({
        email: data.email,
        password: data.password,
        rememberMe: data.rememberMe,
      })

      if (response.success) {
        // Success! Navigate to homepage
        navigate('/homepage')
      } else {
        setLoginError(response.message || 'Login failed. Please try again.')
      }
    } catch (error) {
      if (error instanceof AuthError) {
        // Handle specific auth errors
        switch (error.code) {
          case 'NETWORK_ERROR':
            // Fallback to demo mode for development
            setIsOfflineMode(true)
            handleOfflineLogin(data)
            break
          case 'INVALID_CREDENTIALS':
            setLoginError('Invalid email or password. Please check your credentials.')
            break
          case 'ACCOUNT_LOCKED':
            setLoginError('Your account has been locked. Please contact support.')
            break
          default:
            setLoginError(error.message)
        }
      } else {
        // Unknown error - fallback to demo mode
        console.warn('Login API failed, falling back to demo mode:', error)
        setIsOfflineMode(true)
        handleOfflineLogin(data)
      }
    } finally {
      setIsLoading(false)
    }
  }

  // Fallback demo login for development
  const handleOfflineLogin = (data: FormData) => {
    if (data.email === 'a@a.com' && data.password === '2137') {
      // Store demo user data
      const demoUser = {
        id: 'demo-user-1',
        email: 'a@a.com',
        firstName: 'Demo',
        lastName: 'Trainer',
        role: 'trainer' as const,
        profilePicture: undefined,
      }

      // Simulate storing auth data
      if (data.rememberMe) {
        localStorage.setItem('gymbucket_user', JSON.stringify(demoUser))
        localStorage.setItem('gymbucket_token', 'demo-token-123')
      } else {
        sessionStorage.setItem('gymbucket_user', JSON.stringify(demoUser))
        sessionStorage.setItem('gymbucket_token', 'demo-token-123')
      }

      navigate('/homepage')
    } else {
      setLoginError('Demo credentials: a@a.com / 2137')
    }
  }

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword)
  }

  return (
    <div className={styles.loginContainer}>
      {/* Background Elements */}
      <div className={styles.backgroundShapes}>
        <div className={styles.shape1}></div>
        <div className={styles.shape2}></div>
        <div className={styles.shape3}></div>
      </div>

      {/* Login Card */}
      <div className={styles.loginCard}>
        {/* Offline Mode Indicator */}
        {isOfflineMode && (
          <div className={styles.offlineIndicator}>
            <AlertCircle size={16} />
            <span>Offline Demo Mode</span>
          </div>
        )}

        {/* Header */}
        <div className={styles.header}>
          <div className={styles.logo}>
            <div className={styles.logoIcon}>
              <Dumbbell size={32} />
            </div>
            <div className={styles.logoText}>
              <h1>GymBucket</h1>
              <p>Personal Training Management</p>
            </div>
          </div>
        </div>

        {/* Welcome Message */}
        <div className={styles.welcome}>
          <h2>Welcome Back, Coach!</h2>
          <p>Sign in to manage your clients and grow your fitness business</p>
        </div>

        {/* Login Form */}
        <form
          onSubmit={handleSubmit(handleLogin)}
          className={styles.form}
        >
          {/* Email Field */}
          <div className={styles.inputGroup}>
            <label
              htmlFor='email'
              className={styles.label}
            >
              Email Address
            </label>
            <div className={styles.inputWrapper}>
              <Mail
                className={styles.inputIcon}
                size={20}
              />
              <input
                id='email'
                type='email'
                placeholder='Enter your email'
                className={`${styles.input} ${errors.email ? styles.inputError : ''} ${
                  watchEmail && !errors.email ? styles.inputValid : ''
                }`}
                {...register('email', {
                  required: 'Email is required',
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: 'Invalid email address',
                  },
                  onChange: (e) => setEmailValue(e.target.value),
                })}
              />
              {watchEmail && !errors.email && (
                <CheckCircle
                  className={styles.validIcon}
                  size={20}
                />
              )}
            </div>
            {errors.email && <span className={styles.errorMessage}>{errors.email.message}</span>}
          </div>

          {/* Password Field */}
          <div className={styles.inputGroup}>
            <label
              htmlFor='password'
              className={styles.label}
            >
              Password
            </label>
            <div className={styles.inputWrapper}>
              <Lock
                className={styles.inputIcon}
                size={20}
              />
              <input
                id='password'
                type={showPassword ? 'text' : 'password'}
                placeholder='Enter your password'
                className={`${styles.input} ${errors.password ? styles.inputError : ''} ${
                  watchPassword && !errors.password ? styles.inputValid : ''
                }`}
                {...register('password', {
                  required: 'Password is required',
                  minLength: {
                    value: 4,
                    message: 'Password must be at least 4 characters',
                  },
                  onChange: (e) => setPasswordValue(e.target.value),
                })}
              />
              <button
                type='button'
                className={styles.passwordToggle}
                onClick={togglePasswordVisibility}
              >
                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
              </button>
              {watchPassword && !errors.password && (
                <CheckCircle
                  className={styles.validIcon}
                  size={20}
                />
              )}
            </div>
            {errors.password && (
              <span className={styles.errorMessage}>{errors.password.message}</span>
            )}
          </div>

          {/* Remember Me & Forgot Password */}
          <div className={styles.formOptions}>
            <label className={styles.checkbox}>
              <input
                type='checkbox'
                {...register('rememberMe')}
              />
              <span className={styles.checkmark}></span>
              Remember me
            </label>
            <button
              type='button'
              className={styles.forgotPassword}
            >
              Forgot Password?
            </button>
          </div>

          {/* Error Message */}
          {loginError && <div className={styles.errorAlert}>{loginError}</div>}

          {/* Submit Button */}
          <button
            type='submit'
            className={`${styles.submitButton} ${isLoading ? styles.loading : ''}`}
            disabled={isLoading}
          >
            {isLoading ? (
              <div className={styles.loadingSpinner}></div>
            ) : (
              <>
                Sign In
                <ArrowRight size={20} />
              </>
            )}
          </button>
        </form>

        {/* Demo Credentials */}
        <div className={styles.demoCredentials}>
          <p>Demo Credentials {isOfflineMode && '(Offline Mode)'}:</p>
          <div className={styles.credentials}>
            <span>Email: a@a.com</span>
            <span>Password: 2137</span>
          </div>
        </div>

        {/* API Status */}
        <div className={styles.apiStatus}>
          <p>
            API Status:{' '}
            <span className={isOfflineMode ? styles.offline : styles.online}>
              {isOfflineMode ? 'Offline (Demo Mode)' : 'Online'}
            </span>
          </p>
        </div>

        {/* Footer */}
        <div className={styles.footer}>
          <p>
            Don't have an account?{' '}
            <button
              type='button'
              className={styles.signupLink}
            >
              Sign up for free
            </button>
          </p>
        </div>
      </div>

      {/* Features Panel */}
      <div className={styles.featuresPanel}>
        <div className={styles.featuresContent}>
          <h3>Manage Your Fitness Business</h3>
          <div className={styles.featuresList}>
            <div className={styles.feature}>
              <div className={styles.featureIcon}>📊</div>
              <div>
                <h4>Client Management</h4>
                <p>Track progress, manage schedules, and grow your client base</p>
              </div>
            </div>
            <div className={styles.feature}>
              <div className={styles.featureIcon}>💪</div>
              <div>
                <h4>Workout Plans</h4>
                <p>Create custom workout routines tailored to each client</p>
              </div>
            </div>
            <div className={styles.feature}>
              <div className={styles.featureIcon}>🥗</div>
              <div>
                <h4>Nutrition Tracking</h4>
                <p>Design meal plans and monitor nutritional goals</p>
              </div>
            </div>
            <div className={styles.feature}>
              <div className={styles.featureIcon}>📅</div>
              <div>
                <h4>Smart Scheduling</h4>
                <p>Effortlessly manage appointments and sessions</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default LoginPage
