import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
// import { environment } from '../../environments/environment';

export interface Exercise {
  id: number;
  name: string;
  description: string;
  category: string;
  difficulty: string;
  muscleGroups: string[];
  equipment: string[];
  instructions: string;
  tips: string;
  warnings: string;
  createdAt: string;
  updatedAt: string;
}

export interface ExerciseRequest {
  name: string;
  description: string;
  category: string;
  difficulty: string;
  muscleGroups: string[];
  equipment: string[];
  instructions: string;
  tips: string;
  warnings: string;
}

export interface ExerciseSearchParams {
  search?: string;
  muscleGroup?: string;
  equipment?: string;
  difficulty?: string;
  page?: number;
  size?: number;
}

@Injectable({
  providedIn: 'root'
})
export class ExerciseService {
  private apiUrl = 'http://localhost:8080/api/exercises';

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
    console.error('ExerciseService error:', error);
    let errorMessage = 'Wystąpił nieoczekiwany błąd';
    
    if (error.error?.message) {
      errorMessage = error.error.message;
    } else if (error.message) {
      errorMessage = error.message;
    }
    
    return throwError(() => ({ message: errorMessage }));
  }

  // Get all exercises with pagination and filters
  getAllExercises(params: ExerciseSearchParams = {}): Observable<any> {
    let httpParams = new HttpParams();
    
    if (params.page !== undefined) httpParams = httpParams.set('page', params.page.toString());
    if (params.size !== undefined) httpParams = httpParams.set('size', params.size.toString());
    if (params.search) httpParams = httpParams.set('search', params.search);
    if (params.muscleGroup) httpParams = httpParams.set('muscleGroup', params.muscleGroup);
    if (params.equipment) httpParams = httpParams.set('equipment', params.equipment);
    if (params.difficulty) httpParams = httpParams.set('difficulty', params.difficulty);

    return this.http.get<any>(this.apiUrl, { 
      params: httpParams,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get all exercises without pagination (for dropdowns, etc.)
  getAllExercisesList(params: Omit<ExerciseSearchParams, 'page' | 'size'> = {}): Observable<Exercise[]> {
    let httpParams = new HttpParams();
    
    if (params.search) httpParams = httpParams.set('search', params.search);
    if (params.muscleGroup) httpParams = httpParams.set('muscleGroup', params.muscleGroup);
    if (params.equipment) httpParams = httpParams.set('equipment', params.equipment);
    if (params.difficulty) httpParams = httpParams.set('difficulty', params.difficulty);

    return this.http.get<Exercise[]>(`${this.apiUrl}/all`, { 
      params: httpParams,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get exercise by ID
  getExerciseById(id: number): Observable<Exercise> {
    return this.http.get<Exercise>(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Create new exercise
  createExercise(exercise: ExerciseRequest): Observable<Exercise> {
    return this.http.post<Exercise>(this.apiUrl, exercise, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Update exercise
  updateExercise(id: number, exercise: ExerciseRequest): Observable<Exercise> {
    return this.http.put<Exercise>(`${this.apiUrl}/${id}`, exercise, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Delete exercise
  deleteExercise(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Search exercises
  searchExercises(query: string, page: number = 0, size: number = 10): Observable<any> {
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

  // Get exercises by muscle group
  getExercisesByMuscleGroup(muscleGroup: string, page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('muscleGroup', muscleGroup)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/by-muscle-group`, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get exercises by equipment
  getExercisesByEquipment(equipment: string, page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('equipment', equipment)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<any>(`${this.apiUrl}/by-equipment`, { 
      params,
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get exercises by difficulty
  getExercisesByDifficulty(difficulty: string, page: number = 0, size: number = 10): Observable<any> {
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
      'REHABILITATION': 'Rehabilitacyjny'
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
      'REHABILITATION': 'pink'
    };
    return colors[category] || 'gray';
  }
}
