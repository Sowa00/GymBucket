import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ClientService } from '../services/client.service';
import { Client } from '../services/client.service';
import { WorkoutPlanService } from '../services/workout-plan.service';
import { NutritionPlanService } from '../services/nutrition-plan.service';
import { TrainingSessionService } from '../services/training-session.service';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class ClientsComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();
  
  // Data
  clients: Client[] = [];
  filteredClients: Client[] = [];
  
  // UI State
  isLoading = false;
  showAddModal = false;
  showEditModal = false;
  showDeleteModal = false;
  
  // Messages
  successMessage = '';
  errorMessage = '';
  
  // Form Data
  newClient: any = {
    emergencyContactsText: ''
  };
  selectedClient: Client | null = null;
  
  // Search
  searchTerm = '';

  // Client Detail View
  selectedClientForDetail: Client | null = null;
  clientMeetings: any[] = [];
  assignedWorkoutPlans: any[] = [];
  assignedNutritionPlans: any[] = [];
  expandedExercises: Set<number> = new Set();
  expandedMeals: Set<number> = new Set();

  constructor(
    private clientService: ClientService,
    private router: Router,
    private workoutPlanService: WorkoutPlanService,
    private nutritionPlanService: NutritionPlanService,
    private trainingSessionService: TrainingSessionService
  ) {}

  ngOnInit(): void {
    this.loadClients();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Load clients from server
  loadClients(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.clientService.getActiveClients()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (clients: any[]) => {
          this.clients = clients;
          this.filteredClients = clients;
          this.isLoading = false;
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas ładowania klientów';
          this.isLoading = false;
          console.error('Error loading clients:', error);
        }
      });
  }

  // Filter clients based on search term
  filterClients(): void {
    if (!this.searchTerm.trim()) {
      this.filteredClients = this.clients;
    } else {
      const term = this.searchTerm.toLowerCase();
      this.filteredClients = this.clients.filter(client =>
        client.firstName.toLowerCase().includes(term) ||
        client.lastName.toLowerCase().includes(term) ||
        client.email.toLowerCase().includes(term)
      );
    }
  }

  // Open add client modal
  openAddModal(): void {
    this.newClient = {
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      dateOfBirth: '',
      gender: '',
      height: null,
      weight: null,
      medicalConditions: '',
      fitnessGoals: '',
      notes: '',
      monthlyFee: null,
      paymentStatus: 'PENDING',
      emergencyContacts: [],
      emergencyContactsText: ''
    };
    this.showAddModal = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  // Open edit client modal
  openEditModal(client: Client): void {
    this.selectedClient = client;
    this.newClient = {
      firstName: client.firstName,
      lastName: client.lastName,
      email: client.email,
      phone: client.phone || '',
      dateOfBirth: client.dateOfBirth || '',
      gender: client.gender || '',
      height: client.height || null,
      weight: client.weight || null,
      medicalConditions: client.medicalConditions || '',
      fitnessGoals: client.fitnessGoals || '',
      notes: client.notes || '',
      monthlyFee: client.monthlyFee || null,
      paymentStatus: client.paymentStatus || 'PENDING',
      emergencyContacts: client.emergencyContacts || [],
      emergencyContactsText: this.convertEmergencyContactsToText(client.emergencyContacts)
    };
    this.showEditModal = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  // Open delete confirmation modal
  openDeleteModal(client: Client): void {
    this.selectedClient = client;
    this.showDeleteModal = true;
    this.errorMessage = '';
    this.successMessage = '';
  }

  // Close all modals
  closeModals(): void {
    this.showAddModal = false;
    this.showEditModal = false;
    this.showDeleteModal = false;
    this.selectedClient = null;
    this.errorMessage = '';
    this.successMessage = '';
  }

  // Add new client
  addClient(): void {
    if (!this.validateForm()) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Convert emergency contacts text to array
    const emergencyContacts = this.convertEmergencyContactsFromText(this.newClient.emergencyContactsText);

    const clientData = {
      ...this.newClient,
      emergencyContacts: emergencyContacts
    };

    // Remove the text field from the data sent to backend
    delete clientData.emergencyContactsText;

    this.clientService.createClient(clientData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: any) => {
          this.successMessage = 'Klient został dodany pomyślnie';
          this.isLoading = false;
          this.closeModals();
          this.loadClients(); // Reload the list
          this.clearSuccessMessage();
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Błąd podczas dodawania klienta';
          this.isLoading = false;
          console.error('Error adding client:', error);
        }
      });
  }

  // Update client
  updateClient(): void {
    if (!this.selectedClient || !this.validateForm()) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    // Convert emergency contacts text to array
    const emergencyContacts = this.convertEmergencyContactsFromText(this.newClient.emergencyContactsText);

    const clientData = {
      ...this.newClient,
      emergencyContacts: emergencyContacts
    };

    // Remove the text field from the data sent to backend
    delete clientData.emergencyContactsText;

    this.clientService.updateClient(this.selectedClient.id, clientData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: any) => {
          this.successMessage = 'Klient został zaktualizowany pomyślnie';
          this.isLoading = false;
          this.closeModals();
          this.loadClients(); // Reload the list
          this.clearSuccessMessage();
        },
        error: (error: any) => {
          this.errorMessage = error.message || 'Błąd podczas aktualizacji klienta';
          this.isLoading = false;
          console.error('Error updating client:', error);
        }
      });
  }

  // Delete client
  deleteClient(): void {
    if (!this.selectedClient) {
      console.log('No client selected for deletion');
      return;
    }

    console.log('Deleting client with ID:', this.selectedClient.id);
    this.isLoading = true;
    this.errorMessage = '';

    this.clientService.deleteClient(this.selectedClient.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: any) => {
          console.log('Delete response received:', response);
          this.successMessage = 'Klient został usunięty pomyślnie';
          this.isLoading = false;
          this.closeModals();
          this.loadClients(); // Reload the list
          this.clearSuccessMessage();
        },
        error: (error: any) => {
          console.error('Delete error:', error);
          this.errorMessage = error.message || 'Błąd podczas usuwania klienta';
          this.isLoading = false;
        }
      });
  }

  // Validate form
  validateForm(): boolean {
    if (!this.newClient.firstName?.trim()) {
      this.errorMessage = 'Imię jest wymagane';
      return false;
    }
    if (!this.newClient.lastName?.trim()) {
      this.errorMessage = 'Nazwisko jest wymagane';
      return false;
    }
    if (!this.newClient.email?.trim()) {
      this.errorMessage = 'Email jest wymagany';
      return false;
    }
    if (!this.isValidEmail(this.newClient.email)) {
      this.errorMessage = 'Nieprawidłowy format email';
      return false;
    }
    this.errorMessage = '';
    return true;
  }

  // Email validation
  isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  // Clear success message after delay
  clearSuccessMessage(): void {
    setTimeout(() => {
      this.successMessage = '';
    }, 3000);
  }

  // Helper methods
  getPaymentStatusColor(status?: string): string {
    if (!status) return 'gray';
    return this.clientService.getPaymentStatusColor(status);
  }

  getPaymentStatusLabel(status?: string): string {
    if (!status) return 'Nieznany';
    return this.clientService.getPaymentStatusLabel(status);
  }

  getPaymentStatusClass(status?: string): string {
    switch (status) {
      case 'PAID': return 'paid';
      case 'PENDING': return 'pending';
      case 'OVERDUE': return 'overdue';
      default: return 'unknown';
    }
  }

  getInitials(firstName: string, lastName: string): string {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  }

  getTotalMonthlyRevenue(): number {
    return this.filteredClients
      .filter(client => client.paymentStatus === 'PAID')
      .reduce((total, client) => total + (client.monthlyFee || 0), 0);
  }

  getPaidClientsCount(): number {
    return this.filteredClients.filter(client => client.paymentStatus === 'PAID').length;
  }

  getPendingClientsCount(): number {
    return this.filteredClients.filter(client => client.paymentStatus === 'PENDING').length;
  }

  // Navigation
  goBack(): void {
    this.router.navigate(['/homepage']);
  }

  // Helper methods for emergency contacts
  private convertEmergencyContactsToText(contacts: string[] | undefined): string {
    return this.clientService.convertEmergencyContactsToString(contacts);
  }

  private convertEmergencyContactsFromText(text: string): string[] {
    return this.clientService.convertEmergencyContactsFromString(text);
  }

  getGenderLabel(gender: string): string {
    return this.clientService.getGenderLabel(gender);
  }

  // Client Detail View Methods
  selectClientForDetail(client: Client): void {
    this.selectedClientForDetail = client;
    this.loadClientDetailData();
  }

  closeClientDetail(): void {
    this.selectedClientForDetail = null;
    this.clientMeetings = [];
    this.assignedWorkoutPlans = [];
    this.assignedNutritionPlans = [];
    this.expandedExercises.clear();
    this.expandedMeals.clear();
  }

  loadClientDetailData(): void {
    if (!this.selectedClientForDetail) return;
    
    this.loadClientMeetings();
    this.loadAssignedWorkoutPlans();
    this.loadAssignedNutritionPlans();
  }

  loadClientMeetings(): void {
    if (!this.selectedClientForDetail) return;
    
    this.trainingSessionService.getTrainingSessionsByClient(this.selectedClientForDetail.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (sessions: any[]) => {
          this.clientMeetings = sessions;
        },
        error: (error: any) => {
          console.error('Error loading client meetings:', error);
        }
      });
  }

  loadAssignedWorkoutPlans(): void {
    if (!this.selectedClientForDetail) return;
    
    this.clientService.getAssignedWorkoutPlans(this.selectedClientForDetail.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (assignments: any[]) => {
          this.assignedWorkoutPlans = assignments;
        },
        error: (error: any) => {
          console.error('Error loading assigned workout plans:', error);
        }
      });
  }

  loadAssignedNutritionPlans(): void {
    if (!this.selectedClientForDetail) return;
    
    this.clientService.getAssignedNutritionPlans(this.selectedClientForDetail.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (assignments: any[]) => {
          this.assignedNutritionPlans = assignments;
        },
        error: (error: any) => {
          console.error('Error loading assigned nutrition plans:', error);
        }
      });
  }


  toggleExercises(planId: number): void {
    if (this.expandedExercises.has(planId)) {
      this.expandedExercises.delete(planId);
    } else {
      this.expandedExercises.add(planId);
      // Fetch workout plan details if not already loaded
      this.loadWorkoutPlanDetails(planId);
    }
  }

  toggleMeals(planId: number): void {
    if (this.expandedMeals.has(planId)) {
      this.expandedMeals.delete(planId);
    } else {
      this.expandedMeals.add(planId);
      // Fetch nutrition plan details if not already loaded
      this.loadNutritionPlanDetails(planId);
    }
  }

  isExercisesExpanded(planId: number): boolean {
    return this.expandedExercises.has(planId);
  }

  isMealsExpanded(planId: number): boolean {
    return this.expandedMeals.has(planId);
  }

  loadWorkoutPlanDetails(planId: number): void {
    // Check if we already have the details for this plan
    const assignment = this.assignedWorkoutPlans.find(a => a.workoutPlanId === planId);
    if (assignment && assignment.exercises) {
      return; // Already loaded
    }

    this.workoutPlanService.getWorkoutPlanById(planId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (plan: any) => {
          // Update the assignment with exercise details
          const assignmentIndex = this.assignedWorkoutPlans.findIndex(a => a.workoutPlanId === planId);
          if (assignmentIndex !== -1) {
            this.assignedWorkoutPlans[assignmentIndex].exercises = plan.exercises || [];
          }
        },
        error: (error: any) => {
          console.error('Error loading workout plan details:', error);
        }
      });
  }

  loadNutritionPlanDetails(planId: number): void {
    // Check if we already have the details for this plan
    const assignment = this.assignedNutritionPlans.find(a => a.nutritionPlanId === planId);
    if (assignment && assignment.meals) {
      return; // Already loaded
    }

    this.nutritionPlanService.getNutritionPlanById(planId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (plan: any) => {
          // Update the assignment with meal details
          const assignmentIndex = this.assignedNutritionPlans.findIndex(a => a.nutritionPlanId === planId);
          if (assignmentIndex !== -1) {
            this.assignedNutritionPlans[assignmentIndex].meals = plan.meals || [];
          }
        },
        error: (error: any) => {
          console.error('Error loading nutrition plan details:', error);
        }
      });
  }
}