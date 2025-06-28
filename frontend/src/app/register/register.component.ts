import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Dodaj FormsModule
import { Subject, takeUntil, debounceTime, distinctUntilChanged } from 'rxjs';
import { AuthService, RegisterRequest } from '../services/auth.service';

// Custom validators
export function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.get('password');
  const confirmPassword = control.get('confirmPassword');

  if (!password || !confirmPassword) {
    return null;
  }

  return password.value === confirmPassword.value ? null : { passwordMismatch: true };
}

export function strongPasswordValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.value;
  if (!password) {
    return null;
  }

  const hasUpperCase = /[A-Z]/.test(password);
  const hasLowerCase = /[a-z]/.test(password);
  const hasNumeric = /[0-9]/.test(password);
  const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
  const isValidLength = password.length >= 8;

  const passwordValid = hasUpperCase && hasLowerCase && hasNumeric && hasSpecialChar && isValidLength;

  if (!passwordValid) {
    return {
      strongPassword: {
        hasUpperCase,
        hasLowerCase,
        hasNumeric,
        hasSpecialChar,
        isValidLength
      }
    };
  }

  return null;
}

export function phoneValidator(control: AbstractControl): ValidationErrors | null {
  const phone = control.value;
  if (!phone) {
    return null; // Optional field
  }

  const phoneRegex = /^(\+48\s?)?(\d{3}\s?\d{3}\s?\d{3}|\d{9})$/;
  return phoneRegex.test(phone) ? null : { invalidPhone: true };
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, FormsModule], // Dodaj FormsModule
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnDestroy {
  registerForm!: FormGroup; // Używamy definite assignment operator
  currentStep = 1;
  totalSteps = 3;
  showPassword = false;
  showConfirmPassword = false;
  isLoading = false;
  registerError = '';
  registerSuccess = false;
  emailExists = false;
  checkingEmail = false;

  // Password strength indicator
  passwordStrength = {
    score: 0,
    feedback: ''
  };

  // Available specializations
  availableSpecializations = [
    'Trening personalny',
    'Fitness grupowy',
    'Kulturystyka',
    'Powerlifting',
    'Crossfit',
    'Pilates',
    'Yoga',
    'Kardio',
    'Funkcjonalny',
    'Rehabilitacyjny',
    'Dla seniorów',
    'Dla dzieci',
    'Dietetyka sportowa',
    'Suplementacja'
  ];

  private destroy$ = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    this.createForm();
    this.setupEmailValidation();
    this.setupPasswordStrengthCheck();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private createForm(): void {
    this.registerForm = this.fb.group({
      // Step 1: Basic Info
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [phoneValidator]],

      // Step 2: Password
      password: ['', [Validators.required, strongPasswordValidator]],
      confirmPassword: ['', [Validators.required]],

      // Step 3: Professional Info & Terms
      specializations: [[]],
      experience: ['', [Validators.min(0), Validators.max(50)]],
      certification: [''],
      acceptTerms: [false, [Validators.requiredTrue]],
      acceptNewsletter: [false]
    }, {
      validators: [passwordMatchValidator]
    });
  }

  private setupEmailValidation(): void {
    const emailControl = this.registerForm.get('email');
    if (emailControl) {
      emailControl.valueChanges
        .pipe(
          debounceTime(500),
          distinctUntilChanged(),
          takeUntil(this.destroy$)
        )
        .subscribe(email => {
          if (email && emailControl.valid) {
            this.checkEmailAvailability(email);
          } else {
            this.emailExists = false;
            this.checkingEmail = false;
          }
        });
    }
  }

  private setupPasswordStrengthCheck(): void {
    const passwordControl = this.registerForm.get('password');
    if (passwordControl) {
      passwordControl.valueChanges
        .pipe(takeUntil(this.destroy$))
        .subscribe(password => {
          this.updatePasswordStrength(password);
        });
    }
  }

  private checkEmailAvailability(email: string): void {
    this.checkingEmail = true;
    this.authService.checkEmailExists(email)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.emailExists = response.exists;
          this.checkingEmail = false;
        },
        error: () => {
          this.checkingEmail = false;
        }
      });
  }

  private updatePasswordStrength(password: string): void {
    if (!password) {
      this.passwordStrength = { score: 0, feedback: '' };
      return;
    }

    let score = 0;
    let feedback = '';

    // Length check
    if (password.length >= 8) score += 1;
    if (password.length >= 12) score += 1;

    // Character variety
    if (/[a-z]/.test(password)) score += 1;
    if (/[A-Z]/.test(password)) score += 1;
    if (/[0-9]/.test(password)) score += 1;
    if (/[^A-Za-z0-9]/.test(password)) score += 1;

    // Feedback based on score
    if (score <= 2) {
      feedback = 'Słabe';
    } else if (score <= 4) {
      feedback = 'Średnie';
    } else if (score <= 5) {
      feedback = 'Dobre';
    } else {
      feedback = 'Bardzo silne';
    }

    this.passwordStrength = { score, feedback };
  }

  // Form navigation
  nextStep(): void {
    if (this.isStepValid(this.currentStep)) {
      this.currentStep++;
    } else {
      this.markStepFieldsTouched(this.currentStep);
    }
  }

  prevStep(): void {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  goToStep(step: number): void {
    if (step <= this.currentStep || this.isStepValid(step - 1)) {
      this.currentStep = step;
    }
  }

  private isStepValid(step: number): boolean {
    const stepFields = this.getStepFields(step);
    return stepFields.every(fieldName => {
      const field = this.registerForm.get(fieldName);
      return field?.valid && !this.hasStepSpecificErrors(step);
    });
  }

  private hasStepSpecificErrors(step: number): boolean {
    if (step === 1) {
      return this.emailExists;
    }
    if (step === 2) {
      return this.registerForm.hasError('passwordMismatch');
    }
    return false;
  }

  private getStepFields(step: number): string[] {
    switch (step) {
      case 1:
        return ['firstName', 'lastName', 'email'];
      case 2:
        return ['password', 'confirmPassword'];
      case 3:
        return ['acceptTerms'];
      default:
        return [];
    }
  }

  private markStepFieldsTouched(step: number): void {
    const stepFields = this.getStepFields(step);
    stepFields.forEach(fieldName => {
      const field = this.registerForm.get(fieldName);
      field?.markAsTouched();
    });
  }

  // Specializations management
  toggleSpecialization(specialization: string): void {
    const current = this.registerForm.get('specializations')?.value || [];
    const index = current.indexOf(specialization);

    if (index > -1) {
      current.splice(index, 1);
    } else {
      current.push(specialization);
    }

    this.registerForm.get('specializations')?.setValue([...current]);
  }

  isSpecializationSelected(specialization: string): boolean {
    const current = this.registerForm.get('specializations')?.value || [];
    return current.includes(specialization);
  }

  // Password visibility toggles
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  // Form submission
  onSubmit(): void {
    if (this.registerForm.invalid || this.emailExists) {
      this.markFormGroupTouched();
      return;
    }

    this.isLoading = true;
    this.registerError = '';

    const formValue = this.registerForm.value;
    const registerData: RegisterRequest = {
      email: formValue.email,
      password: formValue.password,
      confirmPassword: formValue.confirmPassword,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      phone: formValue.phone,
      specializations: formValue.specializations,
      acceptTerms: formValue.acceptTerms,
      acceptNewsletter: formValue.acceptNewsletter
    };

    this.authService.register(registerData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.registerSuccess = true;
            // Optionally redirect after a delay
            setTimeout(() => {
              this.router.navigate(['/login'], {
                queryParams: {
                  message: 'Konto utworzone. Sprawdź email w celu aktywacji.'
                }
              });
            }, 3000);
          } else {
            this.registerError = response.message || 'Błąd podczas rejestracji';
          }
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Registration error:', error);
          this.registerError = error.message || 'Wystąpił błąd podczas rejestracji. Spróbuj ponownie.';
          this.isLoading = false;
        }
      });
  }

  // Navigation
  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  // Helper methods
  private markFormGroupTouched(): void {
    Object.keys(this.registerForm.controls).forEach(key => {
      const control = this.registerForm.get(key);
      control?.markAsTouched();
    });
  }

  hasError(fieldName: string): boolean {
    const field = this.registerForm.get(fieldName);
    return !!(field?.invalid && field?.touched);
  }

  hasFormError(errorName: string): boolean {
    return this.registerForm.hasError(errorName);
  }

  getErrorMessage(fieldName: string): string {
    const field = this.registerForm.get(fieldName);

    if (field?.hasError('required')) {
      return this.getRequiredErrorMessage(fieldName);
    }

    if (field?.hasError('email')) {
      return 'Nieprawidłowy format email';
    }

    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength']?.requiredLength;
      return `Minimum ${minLength} znaków`;
    }

    if (field?.hasError('maxlength')) {
      const maxLength = field.errors?.['maxlength']?.requiredLength;
      return `Maksimum ${maxLength} znaków`;
    }

    if (field?.hasError('min')) {
      return 'Wartość musi być większa lub równa 0';
    }

    if (field?.hasError('max')) {
      return 'Wartość musi być mniejsza niż 50';
    }

    if (field?.hasError('invalidPhone')) {
      return 'Nieprawidłowy format numeru telefonu';
    }

    if (field?.hasError('strongPassword')) {
      return 'Hasło musi zawierać wielką literę, małą literę, cyfrę i znak specjalny';
    }

    return '';
  }

  private getRequiredErrorMessage(fieldName: string): string {
    const messages: { [key: string]: string } = {
      firstName: 'Imię jest wymagane',
      lastName: 'Nazwisko jest wymagane',
      email: 'Email jest wymagany',
      password: 'Hasło jest wymagane',
      confirmPassword: 'Potwierdzenie hasła jest wymagane',
      acceptTerms: 'Musisz zaakceptować regulamin'
    };
    return messages[fieldName] || 'To pole jest wymagane';
  }

  isFieldValid(fieldName: string): boolean {
    const field = this.registerForm.get(fieldName);
    return !!(field?.valid && field?.value && field?.touched);
  }

  getPasswordStrengthClass(): string {
    const score = this.passwordStrength.score;
    if (score <= 2) return 'weak';
    if (score <= 4) return 'medium';
    if (score <= 5) return 'strong';
    return 'very-strong';
  }

  getPasswordValidationClass(type: string): string {
    const password = this.registerForm.get('password')?.value || '';

    switch (type) {
      case 'length':
        return password.length >= 8 ? 'valid' : 'invalid';
      case 'uppercase':
        return /[A-Z]/.test(password) ? 'valid' : 'invalid';
      case 'lowercase':
        return /[a-z]/.test(password) ? 'valid' : 'invalid';
      case 'number':
        return /[0-9]/.test(password) ? 'valid' : 'invalid';
      case 'special':
        return /[!@#$%^&*(),.?":{}|<>]/.test(password) ? 'valid' : 'invalid';
      default:
        return 'invalid';
    }
  }

  getStepTitle(): string {
    switch (this.currentStep) {
      case 1:
        return 'Podstawowe informacje';
      case 2:
        return 'Bezpieczeństwo konta';
      case 3:
        return 'Informacje zawodowe';
      default:
        return '';
    }
  }

  getStepDescription(): string {
    switch (this.currentStep) {
      case 1:
        return 'Podaj swoje dane osobowe';
      case 2:
        return 'Utwórz bezpieczne hasło';
      case 3:
        return 'Uzupełnij profil zawodowy i zaakceptuj regulamin';
      default:
        return '';
    }
  }
}
