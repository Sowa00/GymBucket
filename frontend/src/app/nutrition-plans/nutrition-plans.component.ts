import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NutritionPlanService, NutritionPlan, NutritionPlanRequest, Meal, MealRequest } from '../services/nutrition-plan.service';
import { ClientService } from '../services/client.service';

@Component({
  selector: 'app-nutrition-plans',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nutrition-plans.component.html',
  styleUrls: ['./nutrition-plans.component.css']
})
export class NutritionPlansComponent implements OnInit {
  selectedTab = 'plans';
  searchQuery = '';
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  // Nutrition Plans
  nutritionPlans: NutritionPlan[] = [];
  selectedPlan: NutritionPlan | null = null;
  showPlanModal = false;
  showMealModal = false;

  // Meals
  meals: Meal[] = [];
  selectedMeal: Meal | null = null;
  availableMeals: Meal[] = [];

  // Client Assignment
  clients: any[] = [];
  showAssignModal = false;
  selectedClientForAssignment: string = '';
  selectedClients: string[] = [];

  // Meal Assignment
  selectedMealForAssignment: string = '';
  selectedMealDay: number = 1;
  selectedMealOrder: number = 1;
  
  mealOrderOptions = [
    { value: 1, label: 'Śniadanie' },
    { value: 2, label: 'Obiad' },
    { value: 3, label: 'Kolacja' },
    { value: 4, label: 'Przekąska' }
  ];

  // Form data
  planForm = {
    name: '',
    description: '',
    category: 'WEIGHT_LOSS',
    difficulty: 'INTERMEDIATE',
    targetCalories: 2000,
    targetProtein: 150,
    targetCarbs: 200,
    targetFat: 80,
    duration: 30,
    isPublic: false,
    isTemplate: false,
    meals: [] as any[]
  };

  mealForm = {
    name: '',
    description: '',
    category: 'BREAKFAST',
    calories: 0,
    protein: 0,
    carbs: 0,
    fat: 0,
    fiber: 0,
    sugar: 0,
    sodium: 0,
    instructions: '',
    prepTime: 0,
    cookTime: 0,
    servings: 1,
    isPublic: false
  };

  // Categories and difficulties
  planCategories = [
    { value: 'WEIGHT_LOSS', label: 'Redukcja' },
    { value: 'MUSCLE_GAIN', label: 'Budowanie masy' },
    { value: 'MAINTENANCE', label: 'Utrzymanie' },
    { value: 'PERFORMANCE', label: 'Wydajność' },
    { value: 'HEALTH', label: 'Zdrowie' },
    { value: 'DETOX', label: 'Detoks' },
    { value: 'VEGETARIAN', label: 'Wegetariański' },
    { value: 'VEGAN', label: 'Wegański' },
    { value: 'KETO', label: 'Keto' },
    { value: 'PALEO', label: 'Paleo' }
  ];

  difficulties = [
    { value: 'BEGINNER', label: 'Początkujący' },
    { value: 'INTERMEDIATE', label: 'Średniozaawansowany' },
    { value: 'ADVANCED', label: 'Zaawansowany' },
    { value: 'EXPERT', label: 'Ekspert' }
  ];

  mealCategories = [
    { value: 'BREAKFAST', label: 'Śniadanie' },
    { value: 'LUNCH', label: 'Obiad' },
    { value: 'DINNER', label: 'Kolacja' },
    { value: 'SNACK', label: 'Przekąska' }
  ];

  constructor(
    private router: Router,
    private nutritionPlanService: NutritionPlanService,
    private clientService: ClientService
  ) {}

  ngOnInit(): void {
    this.loadNutritionPlans();
    this.loadMeals();
    this.loadAvailableMeals();
  }

  selectTab(tab: string): void {
    this.selectedTab = tab;
    this.errorMessage = '';
  }

  // Nutrition Plans Methods
  loadNutritionPlans(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.nutritionPlanService.getAllNutritionPlansList().subscribe({
      next: (plans) => {
        this.nutritionPlans = plans;
        this.isLoading = false;
      },
      error: (error: any) => {
        this.errorMessage = 'Błąd podczas ładowania planów żywieniowych';
        this.isLoading = false;
        console.error('Error loading nutrition plans:', error);
      }
    });
  }

  createPlan(): void {
    this.selectedPlan = null;
    this.resetPlanForm();
    this.showPlanModal = true;
  }

  editPlan(plan: NutritionPlan): void {
    this.selectedPlan = plan;
    this.planForm = { 
      ...plan,
      meals: plan.meals ? [...plan.meals] : []
    };
    this.showPlanModal = true;
  }

  savePlan(): void {
    if (!this.planForm.name.trim()) {
      this.errorMessage = 'Nazwa planu jest wymagana';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const planRequest: NutritionPlanRequest = {
      name: this.planForm.name,
      description: this.planForm.description,
      category: this.planForm.category,
      difficulty: this.planForm.difficulty,
      targetCalories: this.planForm.targetCalories,
      targetProtein: this.planForm.targetProtein,
      targetCarbs: this.planForm.targetCarbs,
      targetFat: this.planForm.targetFat,
      duration: this.planForm.duration,
      isPublic: this.planForm.isPublic,
      isTemplate: this.planForm.isTemplate,
      meals: this.planForm.meals || []
    };

    if (this.selectedPlan) {
      // Update existing plan
      this.nutritionPlanService.updateNutritionPlan(this.selectedPlan.id, planRequest).subscribe({
        next: (updatedPlan) => {
          this.isLoading = false;
          this.showPlanModal = false;
          this.loadNutritionPlans();
          console.log('Plan updated successfully:', updatedPlan);
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas aktualizacji planu';
          this.isLoading = false;
          console.error('Error updating plan:', error);
        }
      });
    } else {
      // Create new plan
      this.nutritionPlanService.createNutritionPlan(planRequest).subscribe({
        next: (newPlan) => {
          this.isLoading = false;
          this.showPlanModal = false;
          this.loadNutritionPlans();
          console.log('Plan created successfully:', newPlan);
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas tworzenia planu';
          this.isLoading = false;
          console.error('Error creating plan:', error);
        }
      });
    }
  }

  deletePlan(plan: NutritionPlan): void {
    if (confirm(`Czy na pewno chcesz usunąć plan "${plan.name}"?`)) {
      this.isLoading = true;
      this.errorMessage = '';
      
      this.nutritionPlanService.deleteNutritionPlan(plan.id).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.loadNutritionPlans();
          console.log('Plan deleted successfully:', response);
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas usuwania planu';
          this.isLoading = false;
          console.error('Error deleting plan:', error);
        }
      });
    }
  }

  duplicatePlan(plan: NutritionPlan): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.nutritionPlanService.duplicateNutritionPlan(plan.id).subscribe({
      next: (duplicatedPlan) => {
        this.isLoading = false;
        this.loadNutritionPlans();
        console.log('Plan duplicated successfully:', duplicatedPlan);
      },
      error: (error: any) => {
        this.errorMessage = 'Błąd podczas duplikowania planu';
        this.isLoading = false;
        console.error('Error duplicating plan:', error);
      }
    });
  }

  // Meals Methods
  loadMeals(): void {
    this.isLoading = true;
    this.errorMessage = '';
    
    this.nutritionPlanService.getAllMeals().subscribe({
      next: (meals) => {
        this.meals = meals;
        this.isLoading = false;
      },
      error: (error: any) => {
        this.errorMessage = 'Błąd podczas ładowania posiłków';
        this.isLoading = false;
        console.error('Error loading meals:', error);
      }
    });
  }

  loadAvailableMeals(): void {
    this.nutritionPlanService.getAllMeals().subscribe({
      next: (meals) => {
        this.availableMeals = meals;
      },
      error: (error: any) => {
        console.error('Error loading available meals:', error);
      }
    });
  }

  createMeal(): void {
    this.selectedMeal = null;
    this.resetMealForm();
    this.showMealModal = true;
  }

  editMeal(meal: Meal): void {
    this.selectedMeal = meal;
    this.mealForm = { ...meal };
    this.showMealModal = true;
  }

  saveMeal(): void {
    if (!this.mealForm.name.trim()) {
      this.errorMessage = 'Nazwa posiłku jest wymagana';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const mealRequest: MealRequest = {
      name: this.mealForm.name,
      description: this.mealForm.description,
      category: this.mealForm.category,
      calories: this.mealForm.calories,
      protein: this.mealForm.protein,
      carbs: this.mealForm.carbs,
      fat: this.mealForm.fat,
      fiber: this.mealForm.fiber,
      sugar: this.mealForm.sugar,
      sodium: this.mealForm.sodium,
      instructions: this.mealForm.instructions,
      prepTime: this.mealForm.prepTime,
      cookTime: this.mealForm.cookTime,
      servings: this.mealForm.servings,
      isCustom: true,
      isPublic: this.mealForm.isPublic
    };

    if (this.selectedMeal) {
      // Update existing meal
      this.nutritionPlanService.updateMeal(this.selectedMeal.id, mealRequest).subscribe({
        next: (updatedMeal) => {
          this.isLoading = false;
          this.showMealModal = false;
          this.loadMeals();
          console.log('Meal updated successfully:', updatedMeal);
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas aktualizacji posiłku';
          this.isLoading = false;
          console.error('Error updating meal:', error);
        }
      });
    } else {
      // Create new meal
      this.nutritionPlanService.createMeal(mealRequest).subscribe({
        next: (newMeal) => {
          this.isLoading = false;
          this.showMealModal = false;
          this.loadMeals();
          console.log('Meal created successfully:', newMeal);
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas tworzenia posiłku';
          this.isLoading = false;
          console.error('Error creating meal:', error);
        }
      });
    }
  }

  deleteMeal(meal: Meal): void {
    if (confirm(`Czy na pewno chcesz usunąć posiłek "${meal.name}"?`)) {
      this.isLoading = true;
      this.errorMessage = '';
      
      this.nutritionPlanService.deleteMeal(meal.id).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.loadMeals();
          console.log('Meal deleted successfully:', response);
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas usuwania posiłku';
          this.isLoading = false;
          console.error('Error deleting meal:', error);
        }
      });
    }
  }

  // Client Assignment Methods
  openAssignModal(plan: NutritionPlan): void {
    this.selectedPlan = plan;
    this.selectedClients = [];
    this.selectedClientForAssignment = '';
    this.loadClients();
    this.loadPlanAssignments(plan.id);
    this.showAssignModal = true;
  }

  closeAssignModal(): void {
    this.showAssignModal = false;
    this.selectedClientForAssignment = '';
  }

  closeModals(): void {
    this.showPlanModal = false;
    this.showMealModal = false;
    this.showAssignModal = false;
    this.selectedPlan = null;
    this.selectedMeal = null;
    this.selectedClientForAssignment = '';
    this.selectedClients = [];
  }

  loadPlanAssignments(planId: number): void {
    this.clientService.getNutritionPlanAssignments(planId).subscribe({
      next: (assignments: any[]) => {
        this.selectedClients = assignments.map(assignment => assignment.clientId.toString());
      },
      error: (error: any) => {
        console.error('Error loading plan assignments:', error);
      }
    });
  }

  loadClients(): void {
    this.clientService.getAllClients().subscribe({
      next: (clients: any[]) => {
        this.clients = clients;
      },
      error: (error: any) => {
        console.error('Error loading clients:', error);
        this.errorMessage = 'Błąd podczas ładowania klientów';
      }
    });
  }

  onClientSelectionChange(): void {
    // This method is called when the client selection dropdown changes
    // The actual logic will be handled in the template
  }

  isClientAssigned(clientId: string): boolean {
    return this.selectedClients.includes(clientId);
  }

  assignPlanToSelectedClient(): void {
    if (!this.selectedPlan || !this.selectedClientForAssignment) return;

    this.clientService.assignNutritionPlan(
      parseInt(this.selectedClientForAssignment),
      this.selectedPlan.id
    ).subscribe({
      next: () => {
        this.selectedClients.push(this.selectedClientForAssignment);
        this.selectedClientForAssignment = '';
        this.successMessage = 'Plan żywieniowy został przypisany';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (error: any) => {
        this.errorMessage = 'Błąd podczas przypisywania planu żywieniowego';
        console.error('Error assigning nutrition plan:', error);
      }
    });
  }

  unassignClientFromPlan(clientId: string): void {
    if (!this.selectedPlan) return;

    // Find the assignment ID and unassign
    this.clientService.getNutritionPlanAssignments(this.selectedPlan.id).subscribe({
      next: (assignments: any[]) => {
        const assignment = assignments.find(a => a.clientId.toString() === clientId);
        if (assignment) {
          this.clientService.unassignNutritionPlan(assignment.id).subscribe({
            next: () => {
              this.selectedClients = this.selectedClients.filter(id => id !== clientId);
              this.successMessage = 'Klient został odłączony od planu';
              setTimeout(() => this.successMessage = '', 3000);
            },
            error: (error: any) => {
              this.errorMessage = 'Błąd podczas odłączania klienta od planu';
              console.error('Error unassigning nutrition plan:', error);
            }
          });
        }
      },
      error: (error: any) => {
        this.errorMessage = 'Błąd podczas ładowania przypisań';
        console.error('Error loading assignments:', error);
      }
    });
  }

  getClientName(clientId: string): string {
    const client = this.clients.find(c => c.id.toString() === clientId);
    return client ? `${client.firstName} ${client.lastName}` : 'Nieznany klient';
  }

  showSuccess(message: string): void {
    this.successMessage = message;
    this.errorMessage = '';
    setTimeout(() => {
      this.successMessage = '';
    }, 3000);
  }

  getDaysArray(): number[] {
    return Array.from({ length: 30 }, (_, i) => i + 1);
  }

  addMealToPlan(): void {
    if (!this.selectedMealForAssignment || !this.selectedMealDay || !this.selectedMealOrder) {
      this.errorMessage = 'Proszę wybrać posiłek, dzień i porę posiłku';
      return;
    }

    const newMeal = {
      mealId: parseInt(this.selectedMealForAssignment),
      dayNumber: this.selectedMealDay,
      mealOrder: this.selectedMealOrder
    };

    this.planForm.meals.push(newMeal);
    
    // Reset form
    this.selectedMealForAssignment = '';
    this.selectedMealDay = 1;
    this.selectedMealOrder = 1;
    
    this.showSuccess('Posiłek został dodany do planu');
  }

  removeMealFromPlan(index: number): void {
    if (confirm('Czy na pewno chcesz usunąć ten posiłek z planu?')) {
      this.planForm.meals.splice(index, 1);
      this.showSuccess('Posiłek został usunięty z planu');
    }
  }

  getMealName(mealId: number): string {
    const meal = this.meals.find(m => m.id === mealId);
    return meal ? meal.name : 'Nieznany posiłek';
  }

  getMealCalories(mealId: number): number {
    const meal = this.meals.find(m => m.id === mealId);
    return meal ? meal.calories : 0;
  }

  getMealOrderLabel(mealOrder: number): string {
    const order = this.mealOrderOptions.find(o => o.value === mealOrder);
    return order ? order.label : mealOrder.toString();
  }


  // Helper Methods
  resetPlanForm(): void {
    this.planForm = {
      name: '',
      description: '',
      category: 'WEIGHT_LOSS',
      difficulty: 'INTERMEDIATE',
      targetCalories: 2000,
      targetProtein: 150,
      targetCarbs: 200,
      targetFat: 80,
      duration: 30,
      isPublic: false,
      isTemplate: false,
      meals: []
    };
  }

  resetMealForm(): void {
    this.mealForm = {
      name: '',
      description: '',
      category: 'BREAKFAST',
      calories: 0,
      protein: 0,
      carbs: 0,
      fat: 0,
      fiber: 0,
      sugar: 0,
      sodium: 0,
      instructions: '',
      prepTime: 0,
      cookTime: 0,
      servings: 1,
      isPublic: false
    };
  }

  getCategoryLabel(category: string): string {
    return this.nutritionPlanService.getCategoryLabel(category);
  }

  getDifficultyLabel(difficulty: string): string {
    return this.nutritionPlanService.getDifficultyLabel(difficulty);
  }

  getMealCategoryLabel(category: string): string {
    return this.nutritionPlanService.getMealCategoryLabel(category);
  }

  formatCalories(calories: number): string {
    return this.nutritionPlanService.formatCalories(calories);
  }

  formatDuration(duration: number): string {
    return this.nutritionPlanService.formatDuration(duration);
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.isLoading = true;
      this.errorMessage = '';
      
      this.nutritionPlanService.searchNutritionPlans(this.searchQuery.trim()).subscribe({
        next: (response) => {
          this.nutritionPlans = response.content || response;
          this.isLoading = false;
        },
        error: (error: any) => {
          this.errorMessage = 'Błąd podczas wyszukiwania';
          this.isLoading = false;
          console.error('Error searching:', error);
        }
      });
    } else {
      this.loadNutritionPlans();
    }
  }

  goBack(): void {
    this.router.navigate(['/homepage']);
  }
}
