import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: 'trainer' | 'admin' | 'client';
  isActive: boolean;
  avatar?: string;
  phone?: string;
  specializations?: string[];
  certification?: string[];
  experience?: number;
  createdAt: string;
  lastLogin?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
  rememberMe: boolean;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  user: User;
  token: string;
  refreshToken: string;
  expiresIn: number;
}

export interface RegisterRequest {
  email: string;
  password: string;
  confirmPassword: string;
  firstName: string;
  lastName: string;
  phone?: string;
  specializations?: string[];
  acceptTerms: boolean;
  acceptNewsletter: boolean;
}

export interface RegisterResponse {
  success: boolean;
  message: string;
  user: User;
  requiresVerification: boolean;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  password: string;
  confirmPassword: string;
}

export interface ApiError {
  success: false;
  message: string;
  errors?: { [key: string]: string[] };
  code?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api'; // Poprawiony URL na backend
  private readonly TOKEN_KEY = 'gymbucket_token';
  private readonly REFRESH_TOKEN_KEY = 'gymbucket_refresh_token';
  private readonly USER_KEY = 'gymbucket_user';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  public currentUser$ = this.currentUserSubject.asObservable();
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.initializeAuth();
  }

  // Initialize authentication state from storage
  private initializeAuth(): void {
    const token = this.getStoredToken();
    const user = this.getStoredUser();

    if (token && user && !this.isTokenExpired(token)) {
      this.currentUserSubject.next(user);
      this.isAuthenticatedSubject.next(true);
    } else {
      this.clearAuthData();
    }
  }

  // Login method
  login(credentials: LoginRequest): Observable<LoginResponse> {
    // Mock implementation - replace with real API call
    return this.mockLogin(credentials).pipe(
      map(response => {
        if (response.success) {
          this.handleSuccessfulAuth(response, credentials.rememberMe);
        }
        return response;
      }),
      catchError(this.handleError)
    );

    // Real API implementation:
    // return this.http.post<LoginResponse>(`${this.API_URL}/auth/login`, credentials)
    //   .pipe(
    //     map(response => {
    //       if (response.success) {
    //         this.handleSuccessfulAuth(response, credentials.rememberMe);
    //       }
    //       return response;
    //     }),
    //     catchError(this.handleError)
    //   );
  }

  // Register method
  register(userData: RegisterRequest): Observable<RegisterResponse> {
    // Mock implementation - replace with real API call
    return this.mockRegister(userData).pipe(
      catchError(this.handleError)
    );

    // Real API implementation:
    // return this.http.post<RegisterResponse>(`${this.API_URL}/auth/register`, userData)
    //   .pipe(
    //     catchError(this.handleError)
    //   );
  }

  // Logout method
  logout(): void {
    // Optional: Call backend to invalidate token
    // this.http.post(`${this.API_URL}/auth/logout`, {}).subscribe();

    this.clearAuthData();
    this.router.navigate(['/login']);
  }

  // Forgot password
  forgotPassword(request: ForgotPasswordRequest): Observable<{ success: boolean; message: string }> {
    return this.http.post<{ success: boolean; message: string }>(`${this.API_URL}/auth/forgot-password`, request)
      .pipe(catchError(this.handleError));
  }

  // Reset password
  resetPassword(request: ResetPasswordRequest): Observable<{ success: boolean; message: string }> {
    return this.http.post<{ success: boolean; message: string }>(`${this.API_URL}/auth/reset-password`, request)
      .pipe(catchError(this.handleError));
  }

  // Refresh token
  refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.getStoredRefreshToken();
    if (!refreshToken) {
      return throwError('No refresh token available');
    }

    return this.http.post<LoginResponse>(`${this.API_URL}/auth/refresh`, { refreshToken })
      .pipe(
        map(response => {
          if (response.success) {
            this.storeAuthData(response, this.isRememberMeEnabled());
          }
          return response;
        }),
        catchError(this.handleError)
      );
  }

  // Get current user
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  // Check if user is authenticated
  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  // Get auth token
  getToken(): string | null {
    return this.getStoredToken();
  }

  // Get auth headers for HTTP requests
  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  // Check if email exists
  checkEmailExists(email: string): Observable<{ exists: boolean }> {
    return this.http.get<{ exists: boolean }>(`${this.API_URL}/auth/check-email?email=${email}`)
      .pipe(catchError(this.handleError));
  }

  // Verify email
  verifyEmail(token: string): Observable<{ success: boolean; message: string }> {
    return this.http.post<{ success: boolean; message: string }>(`${this.API_URL}/auth/verify-email`, { token })
      .pipe(catchError(this.handleError));
  }

  // Resend verification email
  resendVerification(email: string): Observable<{ success: boolean; message: string }> {
    return this.http.post<{ success: boolean; message: string }>(`${this.API_URL}/auth/resend-verification`, { email })
      .pipe(catchError(this.handleError));
  }

  // Private helper methods
  private handleSuccessfulAuth(response: LoginResponse, rememberMe: boolean): void {
    this.storeAuthData(response, rememberMe);
    this.currentUserSubject.next(response.user);
    this.isAuthenticatedSubject.next(true);
  }

  private storeAuthData(response: LoginResponse, rememberMe: boolean): void {
    const storage = rememberMe ? localStorage : sessionStorage;

    storage.setItem(this.TOKEN_KEY, response.token);
    storage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
    storage.setItem(this.USER_KEY, JSON.stringify(response.user));
    storage.setItem('gymbucket_remember_me', rememberMe.toString());
  }

  private clearAuthData(): void {
    // Clear from both storages
    [localStorage, sessionStorage].forEach(storage => {
      storage.removeItem(this.TOKEN_KEY);
      storage.removeItem(this.REFRESH_TOKEN_KEY);
      storage.removeItem(this.USER_KEY);
      storage.removeItem('gymbucket_remember_me');
    });

    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  private getStoredToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY) || sessionStorage.getItem(this.TOKEN_KEY);
  }

  private getStoredRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY) || sessionStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  private getStoredUser(): User | null {
    const userStr = localStorage.getItem(this.USER_KEY) || sessionStorage.getItem(this.USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }

  private isRememberMeEnabled(): boolean {
    return localStorage.getItem('gymbucket_remember_me') === 'true';
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expirationTime = payload.exp * 1000;
      return Date.now() >= expirationTime;
    } catch {
      return true;
    }
  }

  private handleError = (error: any): Observable<never> => {
    let errorMessage = 'Wystąpił nieoczekiwany błąd';

    if (error.error) {
      if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error.message) {
        errorMessage = error.error.message;
      } else if (error.error.errors) {
        // Handle validation errors
        const validationErrors = error.error.errors;
        errorMessage = Object.values(validationErrors).flat().join(', ');
      }
    } else if (error.message) {
      errorMessage = error.message;
    }

    console.error('Auth Error:', error);
    return throwError({ message: errorMessage, originalError: error });
  };

  // Mock methods for development - remove in production
  private mockLogin(credentials: LoginRequest): Observable<LoginResponse> {
    return new Promise<LoginResponse>((resolve, reject) => {
      setTimeout(() => {
        // Mock validation
        if (credentials.email === 'a@a.com' && credentials.password === '2137') {
          resolve({
            success: true,
            message: 'Logowanie pomyślne',
            user: {
              id: '1',
              email: 'a@a.com',
              firstName: 'Jan',
              lastName: 'Kowalski',
              role: 'trainer',
              isActive: true,
              avatar: 'https://i.pravatar.cc/150?img=1',
              phone: '+48 123 456 789',
              specializations: ['Siłownia', 'Kardio', 'Dietetyka'],
              certification: ['ACE Personal Trainer', 'NASM-CPT'],
              experience: 5,
              createdAt: '2023-01-15T10:00:00Z',
              lastLogin: new Date().toISOString()
            },
            token: 'mock_jwt_token_' + Date.now(),
            refreshToken: 'mock_refresh_token_' + Date.now(),
            expiresIn: 3600
          });
        } else {
          reject({
            success: false,
            message: 'Nieprawidłowy email lub hasło',
            code: 'INVALID_CREDENTIALS'
          });
        }
      }, 1500); // Simulate network delay
    }) as any;
  }

  private mockRegister(userData: RegisterRequest): Observable<RegisterResponse> {
    return new Promise<RegisterResponse>((resolve, reject) => {
      setTimeout(() => {
        // Mock email validation
        if (userData.email === 'test@test.com') {
          reject({
            success: false,
            message: 'Email już istnieje w systemie',
            code: 'EMAIL_EXISTS'
          });
          return;
        }

        resolve({
          success: true,
          message: 'Konto zostało utworzone pomyślnie. Sprawdź swoją skrzynkę email w celu weryfikacji.',
          user: {
            id: '2',
            email: userData.email,
            firstName: userData.firstName,
            lastName: userData.lastName,
            role: 'trainer',
            isActive: false, // Requires email verification
            phone: userData.phone,
            specializations: userData.specializations,
            createdAt: new Date().toISOString()
          },
          requiresVerification: true
        });
      }, 2000); // Simulate network delay
    }) as any;
  }
}
