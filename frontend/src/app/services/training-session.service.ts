import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface TrainingSession {
  id: number;
  sessionDate: string;
  startTime: string;
  endTime: string;
  sessionType: string;
  location?: string;
  notes?: string;
  status: string;
  clientId: number;
  clientName: string;
  trainerId: number;
  trainerName: string;
  price?: number;
  isPaid: boolean;
  feedback?: string;
  rating?: number;
  createdAt: string;
  updatedAt: string;
}

export interface TrainingSessionRequest {
  sessionDate: string;
  startTime: string;
  endTime: string;
  sessionType: string;
  location?: string;
  notes?: string;
  clientId: number;
  price?: number;
  isPaid?: boolean;
  feedback?: string;
  rating?: number;
}

@Injectable({
  providedIn: 'root'
})
export class TrainingSessionService {
  private readonly API_URL = 'http://localhost:8080/api/training-sessions';

  constructor(private http: HttpClient) {}

  // Get auth headers
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('gymbucket_token') || sessionStorage.getItem('gymbucket_token');
    
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  // Create a new training session
  createSession(sessionData: TrainingSessionRequest): Observable<TrainingSession> {
    return this.http.post<TrainingSession>(this.API_URL, sessionData, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get all sessions
  getAllSessions(type?: 'today' | 'upcoming'): Observable<TrainingSession[]> {
    let params: any = {};
    if (type) {
      params.type = type;
    }

    return this.http.get<TrainingSession[]>(this.API_URL, {
      headers: this.getAuthHeaders(),
      params: params
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get today's sessions
  getTodaysSessions(): Observable<TrainingSession[]> {
    return this.getAllSessions('today');
  }

  // Get upcoming sessions
  getUpcomingSessions(): Observable<TrainingSession[]> {
    return this.getAllSessions('upcoming');
  }

  // Update session
  updateSession(sessionId: number, sessionData: TrainingSessionRequest): Observable<TrainingSession> {
    return this.http.put<TrainingSession>(`${this.API_URL}/${sessionId}`, sessionData, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Delete session
  deleteSession(sessionId: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.API_URL}/${sessionId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Helper methods
  getSessionStatusColor(status: string): string {
    switch (status?.toUpperCase()) {
      case 'SCHEDULED': return '#3b82f6'; // blue
      case 'CONFIRMED': return '#10b981'; // green
      case 'IN_PROGRESS': return '#f59e0b'; // yellow
      case 'COMPLETED': return '#6b7280'; // gray
      case 'CANCELLED': return '#ef4444'; // red
      case 'NO_SHOW': return '#dc2626'; // dark red
      default: return '#6b7280'; // gray
    }
  }

  getSessionStatusLabel(status: string): string {
    switch (status?.toUpperCase()) {
      case 'SCHEDULED': return 'Zaplanowana';
      case 'CONFIRMED': return 'Potwierdzona';
      case 'IN_PROGRESS': return 'W trakcie';
      case 'COMPLETED': return 'Zakończona';
      case 'CANCELLED': return 'Anulowana';
      case 'NO_SHOW': return 'Nie stawił się';
      default: return 'Nieznany';
    }
  }

  getSessionTypeIcon(sessionType: string): string {
    switch (sessionType?.toLowerCase()) {
      case 'trening siłowy': return '💪';
      case 'cardio': return '🏃';
      case 'konsultacja żywieniowa': return '🥗';
      case 'joga': return '🧘';
      case 'pilates': return '🤸';
      case 'crossfit': return '⚡';
      default: return '🏋️';
    }
  }

  formatSessionTime(startTime: string, endTime: string): string {
    const start = new Date(`2000-01-01T${startTime}`);
    const end = new Date(`2000-01-01T${endTime}`);
    
    const startFormatted = start.toLocaleTimeString('pl-PL', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
    const endFormatted = end.toLocaleTimeString('pl-PL', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
    
    return `${startFormatted} - ${endFormatted}`;
  }

  formatSessionDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('pl-PL', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  isSessionToday(dateString: string): boolean {
    const today = new Date();
    const sessionDate = new Date(dateString);
    return today.toDateString() === sessionDate.toDateString();
  }

  isSessionUpcoming(dateString: string, startTime: string): boolean {
    const now = new Date();
    const sessionDateTime = new Date(`${dateString}T${startTime}`);
    return sessionDateTime > now;
  }

  isSessionPast(dateString: string, startTime: string): boolean {
    const now = new Date();
    const sessionDateTime = new Date(`${dateString}T${startTime}`);
    return sessionDateTime < now;
  }

  // Error handling
  private handleError = (error: any): Observable<never> => {
    let errorMessage = 'Wystąpił nieoczekiwany błąd';

    if (error.error) {
      if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error.message) {
        errorMessage = error.error.message;
      } else if (error.error.errors) {
        const validationErrors = error.error.errors;
        errorMessage = Object.values(validationErrors).flat().join(', ');
      }
    } else if (error.message) {
      errorMessage = error.message;
    }

    console.error('Training Session Service Error:', error);
    return throwError({ message: errorMessage, originalError: error });
  };

  // Get training sessions by client ID
  getTrainingSessionsByClient(clientId: number): Observable<TrainingSession[]> {
    return this.http.get<TrainingSession[]>(`${this.API_URL}/client/${clientId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }
}
