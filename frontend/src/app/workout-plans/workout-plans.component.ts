import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { WorkoutPlanService, WorkoutPlan as ServiceWorkoutPlan, WorkoutPlanRequest } from '../services/workout-plan.service';
import { ExerciseService, Exercise as ServiceExercise } from '../services/exercise.service';

interface Exercise {
  id: string;
  name: string;
  muscleGroups: string[];
  equipment: string[];
  description: string;
  instructions: string[];
  imageUrl?: string;
  videoUrl?: string;
  difficulty: 'beginner' | 'intermediate' | 'advanced';
}

interface WorkoutPlanExercise {
  exerciseId: string;
  exercise?: Exercise; // For display purposes
  sets: number;
  reps: string; // e.g., "8-10", "12", "max"
  weight?: number;
  duration?: number; // for cardio exercises in seconds
  restTime: number; // rest time in seconds
  notes?: string;
  order: number;
}

interface WorkoutPlan {
  id: string;
  name: string;
  description: string;
  category: 'strength' | 'cardio' | 'flexibility' | 'mixed';
  difficulty: 'beginner' | 'intermediate' | 'advanced';
  duration: number; // estimated duration in minutes
  targetMuscleGroups: string[];
  exercises: WorkoutPlanExercise[];
  createdDate: string;
  lastModified: string;
  isPublic: boolean;
  createdBy: string;
  tags: string[];
  equipment: string[];
  clientAssignments?: string[]; // client IDs
}

interface WorkoutPlanTemplate {
  id: string;
  name: string;
  description: string;
  category: 'strength' | 'cardio' | 'flexibility' | 'mixed';
  exercises: Omit<WorkoutPlanExercise, 'exerciseId' | 'exercise'>[];
  isSystem: boolean;
}

@Component({
  selector: 'app-workout-plans',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './workout-plans.component.html',
  styleUrls: ['./workout-plans.component.css']
})
export class WorkoutPlansComponent implements OnInit {
  // View modes
  viewMode: 'list' | 'grid' | 'detailed' = 'grid';
  activeTab: 'plans' | 'exercises' | 'templates' = 'plans';

  // Filters and search
  searchTerm = '';
  categoryFilter = '';
  difficultyFilter = '';
  muscleGroupFilter = '';
  equipmentFilter = '';
  showOnlyMyPlans = false;

  // Modal states
  showCreatePlanModal = false;
  showExerciseModal = false;
  showAssignModal = false;
  showPlanDetailsModal = false;
  showCreateExerciseModal = false;
  editingPlan: WorkoutPlan | null = null;
  selectedPlan: WorkoutPlan | null = null;
  selectedExercise: Exercise | null = null;

  // Form data
  newPlan: Partial<WorkoutPlan> = {
    name: '',
    description: '',
    category: 'strength',
    difficulty: 'beginner',
    duration: 60,
    targetMuscleGroups: [],
    exercises: [],
    isPublic: false,
    tags: [],
    equipment: []
  };

  newExercise: WorkoutPlanExercise = {
    exerciseId: '',
    sets: 3,
    reps: '8-10',
    weight: 0,
    duration: 0,
    restTime: 60,
    notes: '',
    order: 0
  };

  // Exercise creation form
  newExerciseForm: any = {
    name: '',
    description: '',
    category: 'STRENGTH',
    difficulty: 'BEGINNER',
    muscleGroups: [],
    equipment: [],
    instructions: '',
    tips: '',
    warnings: ''
  };

  // Text fields for muscle groups and equipment
  muscleGroupsText = '';
  equipmentText = '';

  // Available options
  categories = [
    { value: 'strength', label: 'Siłowy', icon: '💪' },
    { value: 'cardio', label: 'Kardio', icon: '🏃' },
    { value: 'flexibility', label: 'Rozciąganie', icon: '🧘' },
    { value: 'mixed', label: 'Mieszany', icon: '🔄' }
  ];

  difficulties = [
    { value: 'beginner', label: 'Początkujący', color: '#10b981' },
    { value: 'intermediate', label: 'Średniozaawansowany', color: '#f59e0b' },
    { value: 'advanced', label: 'Zaawansowany', color: '#ef4444' }
  ];

  muscleGroups = [
    'Klatka piersiowa', 'Plecy', 'Ramiona', 'Biceps', 'Triceps',
    'Nogi', 'Pośladki', 'Brzuch', 'Core', 'Łydki', 'Przedramiona'
  ];

  equipmentList = [
    'Sztanga', 'Hantle', 'Kettlebell', 'Maszyna', 'Guma oporowa',
    'Własny ciężar ciała', 'TRX', 'Bieżnia', 'Rower', 'Orbitrek'
  ];

  validationErrors: string[] = [];
  isLoading = false;

  // Workout plans - will be loaded from API
  workoutPlans: WorkoutPlan[] = [];

  // Exercises - will be loaded from API
  exercises: Exercise[] = [];

  // Templates - will be loaded from API
  templates: WorkoutPlanTemplate[] = [];

  // Clients for assignment - will be loaded from API
  clients: any[] = [];

  selectedClients: string[] = [];

  constructor(
    private router: Router,
    private workoutPlanService: WorkoutPlanService,
    private exerciseService: ExerciseService
  ) {}

  ngOnInit(): void {
    this.loadWorkoutPlans();
    this.loadExercises();
  }

  // Filtering and search
  get filteredPlans(): WorkoutPlan[] {
    return this.workoutPlans.filter(plan => {
      const matchesSearch = !this.searchTerm ||
        plan.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        plan.description.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        plan.tags.some(tag => tag.toLowerCase().includes(this.searchTerm.toLowerCase()));

      const matchesCategory = !this.categoryFilter || plan.category === this.categoryFilter;
      const matchesDifficulty = !this.difficultyFilter || plan.difficulty === this.difficultyFilter;
      const matchesMuscleGroup = !this.muscleGroupFilter ||
        plan.targetMuscleGroups.includes(this.muscleGroupFilter);
      const matchesEquipment = !this.equipmentFilter ||
        plan.equipment.includes(this.equipmentFilter);
      const matchesOwnership = !this.showOnlyMyPlans || plan.createdBy === 'current_user'; // current user

      return matchesSearch && matchesCategory && matchesDifficulty &&
        matchesMuscleGroup && matchesEquipment && matchesOwnership;
    });
  }

  get filteredExercises(): Exercise[] {
    return this.exercises.filter(exercise => {
      const matchesSearch = !this.searchTerm ||
        exercise.name.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesMuscleGroup = !this.muscleGroupFilter ||
        exercise.muscleGroups.includes(this.muscleGroupFilter);
      const matchesEquipment = !this.equipmentFilter ||
        exercise.equipment.includes(this.equipmentFilter);
      const matchesDifficulty = !this.difficultyFilter || exercise.difficulty === this.difficultyFilter;

      return matchesSearch && matchesMuscleGroup && matchesEquipment && matchesDifficulty;
    });
  }

  // Modal management
  openCreatePlanModal(): void {
    this.editingPlan = null;
    this.newPlan = {
      name: '',
      description: '',
      category: 'strength',
      difficulty: 'beginner',
      duration: 60,
      targetMuscleGroups: [],
      exercises: [],
      isPublic: false,
      tags: [],
      equipment: []
    };
    this.validationErrors = [];
    this.showCreatePlanModal = true;
  }

  openEditPlanModal(plan: WorkoutPlan): void {
    this.editingPlan = plan;
    this.newPlan = { ...plan };
    this.validationErrors = [];
    this.showCreatePlanModal = true;
  }

  openPlanDetails(plan: WorkoutPlan): void {
    this.selectedPlan = plan;
    // Enrich exercises with full exercise data
    this.selectedPlan.exercises.forEach(planExercise => {
      planExercise.exercise = this.exercises.find(ex => ex.id === planExercise.exerciseId);
    });
    this.showPlanDetailsModal = true;
  }

  openExerciseModal(exercise: Exercise): void {
    this.selectedExercise = exercise;
    this.showExerciseModal = true;
  }

  openAssignModal(plan: WorkoutPlan): void {
    this.selectedPlan = plan;
    this.selectedClients = [...(plan.clientAssignments || [])];
    this.showAssignModal = true;
  }

  closeModals(): void {
    this.showCreatePlanModal = false;
    this.showExerciseModal = false;
    this.showAssignModal = false;
    this.showPlanDetailsModal = false;
    this.showCreateExerciseModal = false;
    this.selectedPlan = null;
    this.selectedExercise = null;
    this.editingPlan = null;
    this.validationErrors = [];
  }

  // Plan management
  validatePlan(): boolean {
    this.validationErrors = [];

    if (!this.newPlan.name?.trim()) {
      this.validationErrors.push('Nazwa planu jest wymagana');
    }

    if (!this.newPlan.description?.trim()) {
      this.validationErrors.push('Opis planu jest wymagany');
    }

    if (!this.newPlan.targetMuscleGroups?.length) {
      this.validationErrors.push('Wybierz co najmniej jedną grupę mięśniową');
    }

    if (!this.newPlan.exercises?.length) {
      this.validationErrors.push('Dodaj co najmniej jedno ćwiczenie');
    }

    if (this.newPlan.duration && this.newPlan.duration < 15) {
      this.validationErrors.push('Czas trwania musi wynosić co najmniej 15 minut');
    }

    return this.validationErrors.length === 0;
  }

  savePlan(): void {
    if (!this.validatePlan()) {
      return;
    }

    const now = new Date().toISOString().split('T')[0];

    if (this.editingPlan) {
      // Update existing plan
      const updatedPlan: WorkoutPlan = {
        ...this.editingPlan,
        ...this.newPlan as WorkoutPlan,
        lastModified: now
      };
      this.savePlanToAPI(updatedPlan);
    } else {
      // Create new plan
      const newId = (Date.now() + Math.random()).toString();
      const plan: WorkoutPlan = {
        id: newId,
        name: this.newPlan.name!,
        description: this.newPlan.description!,
        category: this.newPlan.category!,
        difficulty: this.newPlan.difficulty!,
        duration: this.newPlan.duration!,
        targetMuscleGroups: this.newPlan.targetMuscleGroups!,
        exercises: this.newPlan.exercises!,
        createdDate: now,
        lastModified: now,
        isPublic: this.newPlan.isPublic!,
        createdBy: 'current_user', // current user
        tags: this.newPlan.tags!,
        equipment: this.newPlan.equipment!
      };

      this.savePlanToAPI(plan);
    }

    this.closeModals();
  }

  deletePlan(planId: string): void {
    if (confirm('Czy na pewno chcesz usunąć ten plan treningowy?')) {
      this.deletePlanFromAPI(planId);
      this.closeModals();
    }
  }

  duplicatePlan(plan: WorkoutPlan): void {
    const newId = (Date.now() + Math.random()).toString();
    const now = new Date().toISOString().split('T')[0];

    const duplicatedPlan: WorkoutPlan = {
      ...plan,
      id: newId,
      name: `${plan.name} (kopia)`,
      createdDate: now,
      lastModified: now,
      createdBy: 'current_user', // current user
      isPublic: false,
      clientAssignments: []
    };

    this.workoutPlans.push(duplicatedPlan);
    this.showSuccess('Plan został zduplikowany!');
  }

  // Exercise management in plan
  addExerciseToPlan(): void {
    if (!this.newExercise.exerciseId) {
      this.showError('Wybierz ćwiczenie');
      return;
    }

    const exercise = this.exercises.find(ex => ex.id === this.newExercise.exerciseId);
    if (!exercise) {
      this.showError('Nie znaleziono ćwiczenia');
      return;
    }

    if (!this.newPlan.exercises) {
      this.newPlan.exercises = [];
    }

    const planExercise: WorkoutPlanExercise = {
      ...this.newExercise,
      order: this.newPlan.exercises.length + 1,
      exercise: exercise
    };

    this.newPlan.exercises.push(planExercise);

    // Update equipment and muscle groups
    this.updatePlanMetadata();

    // Reset form
    this.newExercise = {
      exerciseId: '',
      sets: 3,
      reps: '8-10',
      weight: 0,
      duration: 0,
      restTime: 60,
      notes: '',
      order: 0
    };
  }

  removeExerciseFromPlan(index: number): void {
    if (this.newPlan.exercises) {
      this.newPlan.exercises.splice(index, 1);
      // Reorder remaining exercises
      this.newPlan.exercises.forEach((exercise, i) => {
        exercise.order = i + 1;
      });
      this.updatePlanMetadata();
    }
  }

  moveExerciseUp(index: number): void {
    if (this.newPlan.exercises && this.newPlan.exercises.length > 0 && index > 0) {
      const temp = this.newPlan.exercises[index];
      this.newPlan.exercises[index] = this.newPlan.exercises[index - 1];
      this.newPlan.exercises[index - 1] = temp;
      // Update order
      this.newPlan.exercises[index].order = index + 1;
      this.newPlan.exercises[index - 1].order = index;
    }
  }

  moveExerciseDown(index: number): void {
    if (this.newPlan.exercises && this.newPlan.exercises.length > 0 && index < this.newPlan.exercises.length - 1) {
      const temp = this.newPlan.exercises[index];
      this.newPlan.exercises[index] = this.newPlan.exercises[index + 1];
      this.newPlan.exercises[index + 1] = temp;
      // Update order
      this.newPlan.exercises[index].order = index + 1;
      this.newPlan.exercises[index + 1].order = index + 2;
    }
  }

  updatePlanMetadata(): void {
    if (!this.newPlan.exercises) return;

    // Update equipment
    const equipment = new Set<string>();
    const muscleGroups = new Set<string>();

    this.newPlan.exercises.forEach(planExercise => {
      const exercise = this.exercises.find(ex => ex.id === planExercise.exerciseId);
      if (exercise) {
        exercise.equipment.forEach(eq => equipment.add(eq));
        exercise.muscleGroups.forEach(mg => muscleGroups.add(mg));
      }
    });

    this.newPlan.equipment = Array.from(equipment);
    if (!this.newPlan.targetMuscleGroups?.length) {
      this.newPlan.targetMuscleGroups = Array.from(muscleGroups);
    }
  }

  // Client assignment
  toggleClientSelection(clientId: string): void {
    const index = this.selectedClients.indexOf(clientId);
    if (index > -1) {
      this.selectedClients.splice(index, 1);
    } else {
      this.selectedClients.push(clientId);
    }
  }

  assignPlanToClients(): void {
    if (this.selectedPlan) {
      this.selectedPlan.clientAssignments = [...this.selectedClients];

      // Update in main array
      const index = this.workoutPlans.findIndex(p => p.id === this.selectedPlan!.id);
      if (index !== -1) {
        this.workoutPlans[index] = this.selectedPlan;
      }

      this.showSuccess(`Plan przypisany do ${this.selectedClients.length} klientów`);
      this.closeModals();
    }
  }

  // Form handling methods
  onSubmitPlan(event: Event): void {
    event.preventDefault();
    this.savePlan();
  }

  // Helper methods for form handling
  toggleMuscleGroup(group: string, event: any): void {
    const isChecked = event.target.checked;
    if (!this.newPlan.targetMuscleGroups) {
      this.newPlan.targetMuscleGroups = [];
    }

    if (isChecked) {
      this.newPlan.targetMuscleGroups = [...this.newPlan.targetMuscleGroups, group];
    } else {
      this.newPlan.targetMuscleGroups = this.newPlan.targetMuscleGroups.filter(g => g !== group);
    }
  }

  getTagsAsString(): string {
    return this.newPlan.tags?.join(', ') || '';
  }

  updateTags(event: any): void {
    const value = event.target.value;
    this.newPlan.tags = value.split(',').map((t: string) => t.trim()).filter((t: string) => t);
  }

  validateTraining(): boolean {
    this.validatePlan();
    return this.validationErrors && this.validationErrors.length === 0;
  }

  // Helper methods
  getCategoryIcon(category: string): string {
    const cat = this.categories.find(c => c.value === category);
    return cat?.icon || '📋';
  }

  getCategoryLabel(category: string): string {
    const cat = this.categories.find(c => c.value === category);
    return cat?.label || category;
  }

  getDifficultyColor(difficulty: string): string {
    const diff = this.difficulties.find(d => d.value === difficulty);
    return diff?.color || '#64748b';
  }

  getDifficultyLabel(difficulty: string): string {
    const diff = this.difficulties.find(d => d.value === difficulty);
    return diff?.label || difficulty;
  }

  formatDuration(minutes: number): string {
    if (minutes < 60) {
      return `${minutes} min`;
    } else {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return mins > 0 ? `${hours}h ${mins}min` : `${hours}h`;
    }
  }

  getExerciseName(exerciseId: string): string {
    const exercise = this.exercises.find(ex => ex.id === exerciseId);
    return exercise?.name || 'Nieznane ćwiczenie';
  }

  showSuccess(message: string): void {
    console.log('✅', message);
    // Implement toast notification
  }

  showError(message: string): void {
    console.error('❌', message);
    alert(message); // Temporary - replace with proper notification
  }

  // Exercise creation methods
  openCreateExerciseModal(): void {
    this.newExerciseForm = {
      name: '',
      description: '',
      category: 'STRENGTH',
      difficulty: 'BEGINNER',
      muscleGroups: [],
      equipment: [],
      instructions: '',
      tips: '',
      warnings: ''
    };
    this.muscleGroupsText = '';
    this.equipmentText = '';
    this.showCreateExerciseModal = true;
  }

  saveExercise(): void {
    if (!this.newExerciseForm.name?.trim()) {
      this.showError('Nazwa ćwiczenia jest wymagana');
      return;
    }

    this.isLoading = true;

    // Convert text fields to arrays
    const muscleGroups = this.muscleGroupsText
      .split(',')
      .map(mg => mg.trim())
      .filter(mg => mg.length > 0);
    
    const equipment = this.equipmentText
      .split(',')
      .map(eq => eq.trim())
      .filter(eq => eq.length > 0);

    const exerciseRequest = {
      ...this.newExerciseForm,
      muscleGroups: muscleGroups,
      equipment: equipment
    };

    this.exerciseService.createExercise(exerciseRequest).subscribe({
      next: (createdExercise) => {
        this.isLoading = false;
        this.showCreateExerciseModal = false;
        this.loadExercises();
        this.showSuccess('Ćwiczenie zostało utworzone pomyślnie!');
      },
      error: (error) => {
        this.isLoading = false;
        this.showError('Błąd podczas tworzenia ćwiczenia: ' + (error.message || 'Nieznany błąd'));
        console.error('Error creating exercise:', error);
      }
    });
  }

  // Navigation
  goToHomepage(): void {
    this.router.navigate(['/homepage']);
  }

  goToCalendar(): void {
    this.router.navigate(['/calendar']);
  }

  logout(): void {
    localStorage.removeItem('gymbucket_user');
    localStorage.removeItem('gymbucket_token');
    sessionStorage.removeItem('gymbucket_user');
    sessionStorage.removeItem('gymbucket_token');
    this.router.navigate(['/login']);
  }

  // API methods - connected to backend
  async loadWorkoutPlans(): Promise<void> {
    try {
      this.isLoading = true;
      const response = await this.workoutPlanService.getAllWorkoutPlansList().toPromise();
      // Convert service WorkoutPlan to component WorkoutPlan
      this.workoutPlans = (response || []).map(servicePlan => this.convertServiceToComponentWorkoutPlan(servicePlan));
    } catch (error) {
      console.error('Error loading workout plans:', error);
      this.showError('Błąd podczas ładowania planów treningowych');
    } finally {
      this.isLoading = false;
    }
  }

  async loadExercises(): Promise<void> {
    try {
      this.isLoading = true;
      const response = await this.exerciseService.getAllExercisesList().toPromise();
      // Convert service Exercise to component Exercise
      this.exercises = (response || []).map(serviceExercise => this.convertServiceToComponentExercise(serviceExercise));
    } catch (error) {
      console.error('Error loading exercises:', error);
      this.showError('Błąd podczas ładowania ćwiczeń');
    } finally {
      this.isLoading = false;
    }
  }

  async savePlanToAPI(plan: WorkoutPlan): Promise<void> {
    try {
      this.isLoading = true;
      if (plan.id && this.editingPlan) {
        // Update existing plan
        const planRequest: WorkoutPlanRequest = this.convertToWorkoutPlanRequest(plan);
        await this.workoutPlanService.updateWorkoutPlan(Number(plan.id), planRequest).toPromise();
      } else {
        // Create new plan
        const planRequest: WorkoutPlanRequest = this.convertToWorkoutPlanRequest(plan);
        await this.workoutPlanService.createWorkoutPlan(planRequest).toPromise();
      }
      this.loadWorkoutPlans();
    } catch (error) {
      console.error('Error saving workout plan:', error);
      this.showError('Błąd podczas zapisywania planu treningowego');
    } finally {
      this.isLoading = false;
    }
  }

  async deletePlanFromAPI(planId: string): Promise<void> {
    try {
      this.isLoading = true;
      await this.workoutPlanService.deleteWorkoutPlan(Number(planId)).toPromise();
      this.loadWorkoutPlans();
    } catch (error) {
      console.error('Error deleting workout plan:', error);
      this.showError('Błąd podczas usuwania planu treningowego');
    } finally {
      this.isLoading = false;
    }
  }

  async assignPlanToClientsAPI(planId: string, clientIds: string[]): Promise<void> {
    try {
      // POST /api/workout-plans/:id/assign
      // await this.http.post(`/api/workout-plans/${planId}/assign`, { clientIds }).toPromise();
    } catch (error) {
      this.showError('Błąd podczas przypisywania planu');
    }
  }

  // Helper method to convert component WorkoutPlan to service WorkoutPlanRequest
  private convertToWorkoutPlanRequest(plan: WorkoutPlan): WorkoutPlanRequest {
    return {
      name: plan.name,
      description: plan.description,
      category: plan.category.toUpperCase(),
      difficulty: plan.difficulty.toUpperCase(),
      estimatedDuration: plan.duration,
      targetMuscleGroups: plan.targetMuscleGroups,
      requiredEquipment: plan.equipment,
      tags: plan.tags,
      isPublic: plan.isPublic,
      exercises: plan.exercises.map(ex => ({
        exerciseId: Number(ex.exerciseId),
        sets: ex.sets,
        reps: ex.reps,
        weight: ex.weight || 0,
        duration: ex.duration || 0,
        restTime: ex.restTime,
        notes: ex.notes || '',
        order: ex.order
      }))
    };
  }

  // Helper method to convert service WorkoutPlan to component WorkoutPlan
  private convertServiceToComponentWorkoutPlan(servicePlan: ServiceWorkoutPlan): WorkoutPlan {
    return {
      id: servicePlan.id.toString(),
      name: servicePlan.name,
      description: servicePlan.description,
      category: servicePlan.category.toLowerCase() as 'strength' | 'cardio' | 'flexibility' | 'mixed',
      difficulty: servicePlan.difficulty.toLowerCase() as 'beginner' | 'intermediate' | 'advanced',
      duration: servicePlan.estimatedDuration,
      targetMuscleGroups: servicePlan.targetMuscleGroups,
      equipment: servicePlan.requiredEquipment,
      exercises: servicePlan.exercises.map(ex => ({
        exerciseId: ex.exerciseId.toString(),
        exerciseName: ex.exerciseName,
        sets: ex.sets,
        reps: ex.reps,
        weight: ex.weight,
        duration: ex.duration,
        restTime: ex.restTime,
        notes: ex.notes,
        order: ex.order
      })),
      createdDate: servicePlan.createdAt,
      lastModified: servicePlan.updatedAt,
      isPublic: servicePlan.isPublic,
      createdBy: servicePlan.createdBy,
      tags: servicePlan.tags
    };
  }

  // Helper method to convert service Exercise to component Exercise
  private convertServiceToComponentExercise(serviceExercise: ServiceExercise): Exercise {
    return {
      id: serviceExercise.id.toString(),
      name: serviceExercise.name,
      muscleGroups: serviceExercise.muscleGroups,
      equipment: serviceExercise.equipment,
      description: serviceExercise.description,
      instructions: [serviceExercise.instructions], // Convert string to array
      difficulty: serviceExercise.difficulty.toLowerCase() as 'beginner' | 'intermediate' | 'advanced'
    };
  }
}
