import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

export interface NutritionPlan {
  id: number;
  name: string;
  description: string;
  category: string;
  difficulty: string;
  targetCalories: number;
  targetProtein: number;
  targetCarbs: number;
  targetFat: number;
  duration: number;
  createdBy: string;
  isPublic: boolean;
  isTemplate: boolean;
  usageCount: number;
  rating: number;
  ratingCount: number;
  averageRating: number;
  createdAt: string;
  updatedAt: string;
  meals: NutritionPlanMeal[];
}

export interface NutritionPlanMeal {
  id: number;
  mealId: number;
  mealName: string;
  dayNumber: number;
  mealOrder: number;
  mealOrderName: string;
  portionSize: number;
  notes: string;
  isOptional: boolean;
}

export interface NutritionPlanRequest {
  name: string;
  description: string;
  category: string;
  difficulty: string;
  targetCalories: number;
  targetProtein: number;
  targetCarbs: number;
  targetFat: number;
  duration: number;
  isPublic: boolean;
  isTemplate: boolean;
  meals: NutritionPlanMealRequest[];
}

export interface NutritionPlanMealRequest {
  mealId: number;
  dayNumber: number;
  mealOrder: number;
  portionSize: number;
  notes: string;
  isOptional: boolean;
}

export interface Meal {
  id: number;
  name: string;
  description: string;
  category: string;
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
  fiber: number;
  sugar: number;
  sodium: number;
  instructions: string;
  prepTime: number;
  cookTime: number;
  servings: number;
  isCustom: boolean;
  isPublic: boolean;
  usageCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface MealRequest {
  name: string;
  description: string;
  category: string;
  calories: number;
  protein: number;
  carbs: number;
  fat: number;
  fiber: number;
  sugar: number;
  sodium: number;
  instructions: string;
  prepTime: number;
  cookTime: number;
  servings: number;
  isCustom: boolean;
  isPublic: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class NutritionPlanService {
  private readonly API_URL = 'http://localhost:8080/api/nutrition-plans';
  private readonly MEALS_API_URL = 'http://localhost:8080/api/meals';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  // Get authentication headers
  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    const userId = this.authService.getCurrentUser()?.id;
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      'X-User-Id': userId?.toString() || ''
    });
  }

  // Handle errors
  private handleError(error: any): Observable<never> {
    console.error('NutritionPlanService error:', error);
    let errorMessage = 'Wystąpił nieoczekiwany błąd';
    
    if (error.error?.message) {
      errorMessage = error.error.message;
    } else if (error.message) {
      errorMessage = error.message;
    }
    
    return throwError(() => ({ message: errorMessage }));
  }

  // Nutrition Plans Methods
  getAllNutritionPlans(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}?page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data;
        }
        return response;
      }),
      catchError(this.handleError)
    );
  }

  getAllNutritionPlansList(): Observable<NutritionPlan[]> {
    return this.http.get<any>(`${this.API_URL}/list`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as NutritionPlan[];
        }
        return response as NutritionPlan[];
      }),
      catchError(this.handleError)
    );
  }

  getNutritionPlanById(id: number): Observable<NutritionPlan> {
    return this.http.get<any>(`${this.API_URL}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as NutritionPlan;
        }
        return response as NutritionPlan;
      }),
      catchError(this.handleError)
    );
  }

  createNutritionPlan(plan: NutritionPlanRequest): Observable<NutritionPlan> {
    return this.http.post<any>(this.API_URL, plan, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as NutritionPlan;
        }
        return response as NutritionPlan;
      }),
      catchError(this.handleError)
    );
  }

  updateNutritionPlan(id: number, plan: NutritionPlanRequest): Observable<NutritionPlan> {
    return this.http.put<any>(`${this.API_URL}/${id}`, plan, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as NutritionPlan;
        }
        return response as NutritionPlan;
      }),
      catchError(this.handleError)
    );
  }

  deleteNutritionPlan(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<any>(`${this.API_URL}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success) {
          return { success: true, message: response.message || 'Plan został usunięty pomyślnie' };
        }
        return response;
      }),
      catchError(this.handleError)
    );
  }

  duplicateNutritionPlan(id: number): Observable<NutritionPlan> {
    return this.http.post<any>(`${this.API_URL}/${id}/duplicate`, {}, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as NutritionPlan;
        }
        return response as NutritionPlan;
      }),
      catchError(this.handleError)
    );
  }

  searchNutritionPlans(searchTerm: string, page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/search?searchTerm=${encodeURIComponent(searchTerm)}&page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data;
        }
        return response;
      }),
      catchError(this.handleError)
    );
  }

  getPublicNutritionPlans(page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/public?page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data;
        }
        return response;
      }),
      catchError(this.handleError)
    );
  }

  getUserNutritionPlans(userId: number, page: number = 0, size: number = 10): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/user/${userId}?page=${page}&size=${size}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data;
        }
        return response;
      }),
      catchError(this.handleError)
    );
  }

  // Meals Methods
  getAllMeals(): Observable<Meal[]> {
    return this.http.get<any>(`${this.MEALS_API_URL}/list`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as Meal[];
        }
        return response as Meal[];
      }),
      catchError(this.handleError)
    );
  }

  getMealById(id: number): Observable<Meal> {
    return this.http.get<any>(`${this.MEALS_API_URL}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as Meal;
        }
        return response as Meal;
      }),
      catchError(this.handleError)
    );
  }

  createMeal(meal: MealRequest): Observable<Meal> {
    return this.http.post<any>(this.MEALS_API_URL, meal, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as Meal;
        }
        return response as Meal;
      }),
      catchError(this.handleError)
    );
  }

  updateMeal(id: number, meal: MealRequest): Observable<Meal> {
    return this.http.put<any>(`${this.MEALS_API_URL}/${id}`, meal, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success && response.data) {
          return response.data as Meal;
        }
        return response as Meal;
      }),
      catchError(this.handleError)
    );
  }

  deleteMeal(id: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<any>(`${this.MEALS_API_URL}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        if (response.success) {
          return { success: true, message: response.message || 'Posiłek został usunięty pomyślnie' };
        }
        return response;
      }),
      catchError(this.handleError)
    );
  }

  // Helper methods
  getCategoryLabel(category: string): string {
    const labels: { [key: string]: string } = {
      'WEIGHT_LOSS': 'Redukcja',
      'MUSCLE_GAIN': 'Budowanie masy',
      'MAINTENANCE': 'Utrzymanie',
      'PERFORMANCE': 'Wydajność',
      'HEALTH': 'Zdrowie',
      'DETOX': 'Detoks',
      'VEGETARIAN': 'Wegetariański',
      'VEGAN': 'Wegański',
      'KETO': 'Keto',
      'PALEO': 'Paleo'
    };
    return labels[category] || category;
  }

  getDifficultyLabel(difficulty: string): string {
    const labels: { [key: string]: string } = {
      'BEGINNER': 'Początkujący',
      'INTERMEDIATE': 'Średniozaawansowany',
      'ADVANCED': 'Zaawansowany',
      'EXPERT': 'Ekspert'
    };
    return labels[difficulty] || difficulty;
  }

  getMealCategoryLabel(category: string): string {
    const labels: { [key: string]: string } = {
      'BREAKFAST': 'Śniadanie',
      'LUNCH': 'Obiad',
      'DINNER': 'Kolacja',
      'SNACK': 'Przekąska'
    };
    return labels[category] || category;
  }

  formatCalories(calories: number): string {
    return calories ? `${calories} kcal` : 'N/A';
  }

  formatDuration(duration: number): string {
    if (!duration) return 'N/A';
    if (duration < 7) return `${duration} dni`;
    if (duration < 30) {
      const weeks = Math.floor(duration / 7);
      const days = duration % 7;
      return days === 0 ? `${weeks} tygodni` : `${weeks} tygodni ${days} dni`;
    }
    const months = Math.floor(duration / 30);
    const days = duration % 30;
    return days === 0 ? `${months} miesięcy` : `${months} miesięcy ${days} dni`;
  }
}
