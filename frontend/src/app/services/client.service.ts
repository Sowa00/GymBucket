import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';

export interface Client {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  dateOfBirth?: string;
  gender?: string;
  height?: number;
  weight?: number;
  medicalConditions?: string;
  fitnessGoals?: string;
  notes?: string;
  monthlyFee?: number;
  paymentStatus?: string;
  emergencyContacts?: string[];
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ClientRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  dateOfBirth?: string;
  gender?: string;
  height?: number;
  weight?: number;
  medicalConditions?: string;
  fitnessGoals?: string;
  notes?: string;
  monthlyFee?: number;
  paymentStatus?: string;
  emergencyContacts?: string[];
}

export interface ClientStats {
  totalClients: number;
  activeClients: number;
  averageSessions: number;
  monthlyRevenue: number;
  paidClients: number;
  overdueClients: number;
}

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly API_URL = 'http://localhost:8080/api/clients';

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
    console.error('ClientService error:', error);
    let errorMessage = 'Wystąpił nieoczekiwany błąd';
    
    if (error.error?.message) {
      errorMessage = error.error.message;
    } else if (error.message) {
      errorMessage = error.message;
    } else if (error.status === 401) {
      errorMessage = 'Brak autoryzacji. Zaloguj się ponownie.';
    } else if (error.status === 403) {
      errorMessage = 'Brak uprawnień do wykonania tej operacji.';
    } else if (error.status === 404) {
      errorMessage = 'Klient nie został znaleziony.';
    } else if (error.status === 409) {
      errorMessage = 'Klient o podanym adresie email już istnieje.';
    } else if (error.status >= 500) {
      errorMessage = 'Błąd serwera. Spróbuj ponownie później.';
    }
    
    return throwError(() => ({ message: errorMessage }));
  }

  // Get all clients (with optional filtering)
  getAllClients(active?: boolean, search?: string): Observable<Client[]> {
    let params: any = {};
    if (active !== undefined) {
      params.active = active.toString();
    }
    if (search) {
      params.search = search;
    }
    // Disable pagination by setting page size to 0
    params.size = '0';

    return this.http.get<any>(this.API_URL, {
      headers: this.getAuthHeaders(),
      params: params
    }).pipe(
      map((response: any) => {
        // Handle both paginated and non-paginated responses
        if (response.content && Array.isArray(response.content)) {
          // Paginated response
          return response.content as Client[];
        } else if (Array.isArray(response)) {
          // Non-paginated response
          return response as Client[];
        } else {
          // Fallback
          return [];
        }
      }),
      catchError(this.handleError)
    );
  }

  // Get only active clients
  getActiveClients(): Observable<Client[]> {
    return this.getAllClients(true);
  }

  // Get client by ID
  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.API_URL}/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Create new client
  createClient(clientData: ClientRequest): Observable<Client> {
    return this.http.post<Client>(this.API_URL, clientData, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Update client
  updateClient(clientId: number, clientData: ClientRequest): Observable<Client> {
    return this.http.put<Client>(`${this.API_URL}/${clientId}`, clientData, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Delete client (soft delete)
  deleteClient(clientId: number): Observable<{ success: boolean; message: string }> {
    console.log('ClientService.deleteClient() called with ID:', clientId);
    console.log('Delete URL:', `${this.API_URL}/${clientId}`);
    
    return this.http.delete<{ success: boolean; message: string }>(`${this.API_URL}/${clientId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      map((response: any) => {
        console.log('Delete response from service:', response);
        return response;
      }),
      catchError(this.handleError)
    );
  }

  // Permanently delete client
  permanentlyDeleteClient(clientId: number): Observable<{ success: boolean; message: string }> {
    return this.http.delete<{ success: boolean; message: string }>(`${this.API_URL}/${clientId}/permanent`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Get client statistics
  getClientStats(): Observable<ClientStats> {
    return this.http.get<ClientStats>(`${this.API_URL}/stats`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Helper method to format client name
  getClientFullName(client: Client): string {
    return `${client.firstName} ${client.lastName}`;
  }

  // Helper method to calculate age
  calculateAge(dateOfBirth: string): number {
    if (!dateOfBirth) return 0;
    const today = new Date();
    const birthDate = new Date(dateOfBirth);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    
    return age;
  }

  // Helper method to format date
  formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('pl-PL');
  }

  // Helper method to get payment status color
  getPaymentStatusColor(status: string): string {
    switch (status) {
      case 'PAID': return '#28a745';
      case 'PENDING': return '#ffc107';
      case 'OVERDUE': return '#dc3545';
      default: return '#6c757d';
    }
  }

  // Helper method to get payment status label
  getPaymentStatusLabel(status: string): string {
    switch (status) {
      case 'PAID': return 'Opłacone';
      case 'PENDING': return 'Oczekujące';
      case 'OVERDUE': return 'Przeterminowane';
      default: return 'Nieznany';
    }
  }

  // Helper method to get gender label
  getGenderLabel(gender: string): string {
    switch (gender) {
      case 'MALE': return 'Mężczyzna';
      case 'FEMALE': return 'Kobieta';
      case 'OTHER': return 'Inne';
      default: return 'Nie podano';
    }
  }

  // Helper method to convert emergency contacts array to string
  convertEmergencyContactsToString(contacts: string[] | undefined): string {
    if (!contacts || contacts.length === 0) {
      return '';
    }
    return contacts.join('\n');
  }

  // Helper method to convert emergency contacts string to array
  convertEmergencyContactsFromString(text: string): string[] {
    if (!text || text.trim() === '') {
      return [];
    }
    return text.split('\n')
      .map(line => line.trim())
      .filter(line => line.length > 0);
  }
}
