import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
// import { environment } from '../../environments/environment';

export interface WorkoutPlan {
  id: number;
  name: string;
  description: string;
  category: string;
  difficulty: string;
  estimatedDuration: number;
  targetMuscleGroups: string[];
  requiredEquipment: string[];
  tags: string[];
  createdBy: string;
  isPublic: boolean;
  createdAt: string;
  updatedAt: string;
  exercises: ExerciseInPlan[];
}

export interface ExerciseInPlan {
  exerciseId: number;
  exerciseName: string;
  sets: number;
  reps: string;
  weight: number;
  duration: number;
  restTime: number;
  notes: string;
  order: number;
}

export interface WorkoutPlanRequest {
  name: string;
  description: string;
  category: string;
  difficulty: string;
  estimatedDuration: number;
  targetMuscleGroups: string[];
  requiredEquipment: string[];
  tags: string[];
  isPublic: boolean;
  exercises: ExerciseInPlanRequest[];
}

export interface ExerciseInPlanRequest {
  exerciseId: number;
  sets: number;
  reps: string;
  weight: number;
  duration: number;
  restTime: number;
  notes: string;
  order: number;
}

export interface WorkoutPlanSearchParams {
  search?: string;
  category?: string;
  difficulty?: string;
  muscleGroup?: string;
  equipment?: string;
  isPublic?: boolean;
  page?: number;
  size?: number;
}

@Injectable({
  providedIn: 'root'
})
export class WorkoutPlanService {
  private apiUrl = 'http://localhost:8080/api/workout-plans';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  // Get authentication headers
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // Handle errors
  private handleError(error: any): Observable<never> {
    console.error('WorkoutPlanService error:', error);
    let errorMessage = 'Wystąpił nieoczekiwany błąd';
    
    if (error.error?.message) {
      errorMessage = error.error.message;
    } else if (error.message) {
      errorMessage = error.message;
    }
    
    return throwError(() => ({ message: errorMessage }));
  }

  // Get all workout plans with pagination and filters
  getAllWorkoutPlans(params: WorkoutPlanSearchParams = {}): Observable<any> {
    let httpParams = new HttpParams();
    
    if (params.page !== undefined) httpParams = httpParams.set('page', params.page.toString());
    if (params.size !== undefined) httpParams = httpParams.set('size', params.size.toString());
    if (params.search) httpParams = httpParams.set('search', params.search);
    if (params.category) httpParams = httpParams.set('category', params.category);
    if (params.difficulty) httpParams = httpParams.set('difficulty', params.difficulty);
    if (params.muscleGroup) httpParams = httpParams.set('muscleGroup', params.muscleGroup);
    if (params.equipment) httpParams = httpParams.set('equipment', params.equipment);
    if (params.isPublic !== undefined) httpParams = httpParams.set('isPublic', params.isPublic.toString());

    return this.http.get<any>(this.apiUrl, { 
      params: httpParams,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get all workout plans without pagination (for dropdowns, etc.)
  getAllWorkoutPlansList(params: Omit<WorkoutPlanSearchParams, 'page' | 'size'> = {}): Observable<WorkoutPlan[]> {
    let httpParams = new HttpParams();
    
    if (params.search) httpParams = httpParams.set('search', params.search);
    if (params.category) httpParams = httpParams.set('category', params.category);
    if (params.difficulty) httpParams = httpParams.set('difficulty', params.difficulty);
    if (params.muscleGroup) httpParams = httpParams.set('muscleGroup', params.muscleGroup);
    if (params.equipment) httpParams = httpParams.set('equipment', params.equipment);
    if (params.isPublic !== undefined) httpParams = httpParams.set('isPublic', params.isPublic.toString());

    return this.http.get<WorkoutPlan[]>(`${this.apiUrl}/all`, { 
      params: httpParams,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get workout plan by ID
  getWorkoutPlanById(id: number): Observable<WorkoutPlan> {
    return this.http.get<WorkoutPlan>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Create new workout plan
  createWorkoutPlan(workoutPlan: WorkoutPlanRequest): Observable<WorkoutPlan> {
    return this.http.post<WorkoutPlan>(this.apiUrl, workoutPlan, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Update workout plan
  updateWorkoutPlan(id: number, workoutPlan: WorkoutPlanRequest): Observable<WorkoutPlan> {
    return this.http.put<WorkoutPlan>(`${this.apiUrl}/${id}`, workoutPlan, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Delete workout plan
  deleteWorkoutPlan(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Duplicate workout plan
  duplicateWorkoutPlan(id: number, newName?: string): Observable<WorkoutPlan> {
    let params = new HttpParams();
    if (newName) {
      params = params.set('newName', newName);
    }
    return this.http.post<WorkoutPlan>(`${this.apiUrl}/${id}/duplicate`, null, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Search workout plans
  searchWorkoutPlans(query: string, page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/search`, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get workout plans by category
  getWorkoutPlansByCategory(category: string, page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('category', category)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/by-category`, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get workout plans by difficulty
  getWorkoutPlansByDifficulty(difficulty: string, page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('difficulty', difficulty)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/by-difficulty`, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get public workout plans
  getPublicWorkoutPlans(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/public`, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get user's workout plans
  getMyWorkoutPlans(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/my`, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Helper methods for UI
  getDifficultyLabel(difficulty: string): string {
    const labels: { [key: string]: string } = {
      'BEGINNER': 'Początkujący',
      'INTERMEDIATE': 'Średniozaawansowany',
      'ADVANCED': 'Zaawansowany',
      'EXPERT': 'Ekspert'
    };
    return labels[difficulty] || difficulty;
  }

  getCategoryLabel(category: string): string {
    const labels: { [key: string]: string } = {
      'STRENGTH': 'Siłowy',
      'CARDIO': 'Kardio',
      'FLEXIBILITY': 'Rozciąganie',
      'BALANCE': 'Równowaga',
      'FUNCTIONAL': 'Funkcjonalny',
      'SPORTS': 'Sportowy',
      'REHABILITATION': 'Rehabilitacyjny',
      'WEIGHT_LOSS': 'Odchudzanie',
      'MUSCLE_GAIN': 'Budowanie masy',
      'ENDURANCE': 'Wytrzymałość',
      'POWER': 'Moc',
      'AGILITY': 'Zwinność'
    };
    return labels[category] || category;
  }

  getDifficultyColor(difficulty: string): string {
    const colors: { [key: string]: string } = {
      'BEGINNER': 'green',
      'INTERMEDIATE': 'orange',
      'ADVANCED': 'red',
      'EXPERT': 'purple'
    };
    return colors[difficulty] || 'gray';
  }

  getCategoryColor(category: string): string {
    const colors: { [key: string]: string } = {
      'STRENGTH': 'blue',
      'CARDIO': 'red',
      'FLEXIBILITY': 'green',
      'BALANCE': 'purple',
      'FUNCTIONAL': 'orange',
      'SPORTS': 'teal',
      'REHABILITATION': 'pink',
      'WEIGHT_LOSS': 'yellow',
      'MUSCLE_GAIN': 'indigo',
      'ENDURANCE': 'cyan',
      'POWER': 'amber',
      'AGILITY': 'lime'
    };
    return colors[category] || 'gray';
  }

  // Format duration in minutes to readable format
  formatDuration(minutes: number): string {
    if (minutes < 60) {
      return `${minutes} min`;
    } else {
      const hours = Math.floor(minutes / 60);
      const remainingMinutes = minutes % 60;
      if (remainingMinutes === 0) {
        return `${hours}h`;
      } else {
        return `${hours}h ${remainingMinutes}min`;
      }
    }
  }

  // Get estimated calories burned (rough estimate)
  getEstimatedCalories(duration: number, difficulty: string): number {
    const baseCaloriesPerMinute: { [key: string]: number } = {
      'BEGINNER': 5,
      'INTERMEDIATE': 7,
      'ADVANCED': 10,
      'EXPERT': 12
    };
    
    const caloriesPerMinute = baseCaloriesPerMinute[difficulty] || 7;
    return Math.round(duration * caloriesPerMinute);
  }
}
