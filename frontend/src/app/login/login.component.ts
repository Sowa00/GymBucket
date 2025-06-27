import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subject, takeUntil } from 'rxjs';
import { AuthService, LoginRequest } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy {
  loginForm: FormGroup;
  showPassword = false;
  isLoading = false;
  loginError = '';
  showForgotPassword = false;
  forgotPasswordEmail = '';
  forgotPasswordSent = false;

  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      rememberMe: [false]
    });

    // Redirect if already authenticated
    this.authService.isAuthenticated$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isAuth => {
        if (isAuth) {
          this.router.navigate(['/homepage']);
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get email() { return this.loginForm.get('email'); }
  get password() { return this.loginForm.get('password'); }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    this.loginError = '';

    const credentials: LoginRequest = {
      email: this.loginForm.get('email')?.value,
      password: this.loginForm.get('password')?.value,
      rememberMe: this.loginForm.get('rememberMe')?.value || false
    };

    this.authService.login(credentials)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            // Navigate to homepage or return URL
            const returnUrl = this.getReturnUrl();
            this.router.navigate([returnUrl]);
          } else {
            this.loginError = response.message || 'Błąd logowania';
            this.isLoading = false;
          }
        },
        error: (error) => {
          console.error('Login error:', error);
          this.loginError = error.message || 'Wystąpił błąd podczas logowania. Spróbuj ponownie.';
          this.isLoading = false;
        }
      });
  }

  onForgotPassword(): void {
    if (!this.forgotPasswordEmail || !this.isValidEmail(this.forgotPasswordEmail)) {
      this.loginError = 'Wprowadź prawidłowy adres email';
      return;
    }

    this.isLoading = true;
    this.loginError = '';

    this.authService.forgotPassword({ email: this.forgotPasswordEmail })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.forgotPasswordSent = true;
            this.showForgotPassword = false;
          } else {
            this.loginError = response.message;
          }
          this.isLoading = false;
        },
        error: (error) => {
          this.loginError = error.message || 'Błąd podczas wysyłania emaila';
          this.isLoading = false;
        }
      });
  }

  toggleForgotPassword(): void {
    this.showForgotPassword = !this.showForgotPassword;
    this.loginError = '';
    this.forgotPasswordSent = false;

    if (this.showForgotPassword) {
      this.forgotPasswordEmail = this.loginForm.get('email')?.value || '';
    }
  }

  goToRegister(): void {
    this.router.navigate(['/register']);
  }

  private getReturnUrl(): string {
    // Get return URL from query params or default to homepage
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('returnUrl') || '/homepage';
  }

  private isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  hasError(fieldName: string): boolean {
    const field = this.loginForm.get(fieldName);
    return !!(field?.invalid && field?.touched);
  }

  getErrorMessage(fieldName: string): string {
    const field = this.loginForm.get(fieldName);

    if (field?.hasError('required')) {
      return fieldName === 'email' ? 'Email jest wymagany' : 'Hasło jest wymagane';
    }

    if (field?.hasError('email')) {
      return 'Nieprawidłowy format email';
    }

    if (field?.hasError('minlength')) {
      return 'Hasło musi mieć co najmniej 4 znaki';
    }

    return '';
  }

  isFieldValid(fieldName: string): boolean {
    const field = this.loginForm.get(fieldName);
    return !!(field?.valid && field?.value && field?.touched);
  }

  // Demo credentials helper
  fillDemoCredentials(): void {
    this.loginForm.patchValue({
      email: 'a@a.com',
      password: '2137'
    });
  }
}
