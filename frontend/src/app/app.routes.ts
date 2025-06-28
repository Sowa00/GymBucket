import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomepageComponent } from './homepage/homepage.component';
import { CalendarComponent } from './calendar/calendar.component';
import { WorkoutPlansComponent } from './workout-plans/workout-plans.component';
// import { AuthGuard, GuestGuard, RoleGuard } from './guards/auth.guard'; // Tymczasowo wyłączone

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
    // canActivate: [GuestGuard] // Tymczasowo wyłączone
  },
  {
    path: 'register',
    component: RegisterComponent
    // canActivate: [GuestGuard] // Tymczasowo wyłączone
  },
  {
    path: 'homepage',
    component: HomepageComponent
    // canActivate: [AuthGuard] // Tymczasowo wyłączone
  },
  {
    path: 'calendar',
    component: CalendarComponent
    // canActivate: [AuthGuard] // Tymczasowo wyłączone
  },
  {
    path: 'workout-plans',
    component: WorkoutPlansComponent
    // canActivate: [AuthGuard] // Tymczasowo wyłączone
  },
  {
    path: '**',
    redirectTo: '/login' // Fallback route
  }
];
