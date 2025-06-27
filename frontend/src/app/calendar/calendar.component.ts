import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

interface Training {
  id: string;
  date: string;
  startTime: string;
  duration: number; // in minutes
  clientName: string;
  location: string;
  notes: string;
  status: 'confirmed' | 'pending' | 'cancelled';
}

interface CalendarDay {
  date: Date;
  isCurrentMonth: boolean;
  isToday: boolean;
  isSelected: boolean;
  isPast: boolean;
  trainings: Training[];
}

interface WeekDay {
  date: Date;
  dayName: string;
  dayNumber: number;
  isToday: boolean;
  trainings: Training[];
}

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {
  currentDate = new Date();
  selectedDate: Date | null = null;
  viewMode: 'month' | 'week' | 'day' = 'month';
  viewModes: ('month' | 'week' | 'day')[] = ['month', 'week', 'day'];
  calendarDays: CalendarDay[] = [];
  weekDays: WeekDay[] = [];
  timeSlots: string[] = [];

  // Filters
  searchClient = '';
  statusFilter = '';

  // Modal states
  showAddModal = false;
  showDetailsModal = false;
  selectedTraining: Training | null = null;
  editingTraining: Training | null = null;

  // Form data with duration
  newTraining: Partial<Training> = {
    date: '',
    startTime: '',
    duration: 60, // default 1 hour
    clientName: '',
    location: '',
    notes: '',
    status: 'confirmed'
  };

  // Duration options
  durationOptions = [
    { label: '30 min', value: 30 },
    { label: '45 min', value: 45 },
    { label: '1h', value: 60 },
    { label: '1,5h', value: 90 },
    { label: '2h', value: 120 },
    { label: 'Custom', value: 0 }
  ];

  customDuration = 60;
  selectedDurationOption = 60;
  validationErrors: string[] = [];

  // Mock data - replace with real API
  trainings: Training[] = [
    {
      id: '1',
      date: '2025-06-27',
      startTime: '09:00',
      duration: 60,
      clientName: 'Anna Kowalska',
      location: 'Siłownia A',
      notes: 'Trening siłowy - nogi',
      status: 'confirmed'
    },
    {
      id: '2',
      date: '2025-06-27',
      startTime: '11:00',
      duration: 45,
      clientName: 'Michał Nowak',
      location: 'Siłownia B',
      notes: 'Cardio + stretching',
      status: 'confirmed'
    },
    {
      id: '3',
      date: '2025-06-28',
      startTime: '14:00',
      duration: 30,
      clientName: 'Ewa Wiśniewska',
      location: 'Sala fitness',
      notes: 'Konsultacja żywieniowa',
      status: 'pending'
    },
    {
      id: '4',
      date: '2025-06-30',
      startTime: '16:00',
      duration: 90,
      clientName: 'Tomasz Zieliński',
      location: 'Siłownia A',
      notes: 'Trening funkcjonalny',
      status: 'confirmed'
    }
  ];

  monthNames = [
    'Styczeń', 'Luty', 'Marzec', 'Kwiecień', 'Maj', 'Czerwiec',
    'Lipiec', 'Sierpień', 'Wrzesień', 'Październik', 'Listopad', 'Grudzień'
  ];

  dayNames = ['Pn', 'Wt', 'Śr', 'Cz', 'Pt', 'Sb', 'Nd'];
  dayNamesFull = ['Poniedziałek', 'Wtorek', 'Środa', 'Czwartek', 'Piątek', 'Sobota', 'Niedziela'];

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.generateTimeSlots();
    this.generateCalendar();
    this.generateWeekView();
  }

  // Generate time slots for day/week view
  generateTimeSlots(): void {
    this.timeSlots = [];
    for (let hour = 6; hour <= 22; hour++) {
      for (let minute = 0; minute < 60; minute += 30) {
        const timeStr = `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
        this.timeSlots.push(timeStr);
      }
    }
  }

  // Generate calendar for month view
  generateCalendar(): void {
    const year = this.currentDate.getFullYear();
    const month = this.currentDate.getMonth();

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startDate = new Date(firstDay);

    const dayOfWeek = (firstDay.getDay() + 6) % 7;
    startDate.setDate(firstDay.getDate() - dayOfWeek);

    this.calendarDays = [];
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    for (let i = 0; i < 42; i++) {
      const date = new Date(startDate);
      date.setDate(startDate.getDate() + i);

      const dayTrainings = this.getTrainingsForDate(date);
      const isPast = date < today;

      this.calendarDays.push({
        date: new Date(date),
        isCurrentMonth: date.getMonth() === month,
        isToday: this.isSameDay(date, today),
        isSelected: this.selectedDate ? this.isSameDay(date, this.selectedDate) : false,
        isPast: isPast,
        trainings: dayTrainings
      });
    }
  }

  // Generate week view
  generateWeekView(): void {
    const startOfWeek = this.getStartOfWeek(this.selectedDate || this.currentDate);
    this.weekDays = [];
    const today = new Date();

    for (let i = 0; i < 7; i++) {
      const date = new Date(startOfWeek);
      date.setDate(startOfWeek.getDate() + i);

      this.weekDays.push({
        date: new Date(date),
        dayName: this.dayNames[i],
        dayNumber: date.getDate(),
        isToday: this.isSameDay(date, today),
        trainings: this.getTrainingsForDate(date)
      });
    }
  }

  getStartOfWeek(date: Date): Date {
    const start = new Date(date);
    const day = start.getDay();
    const diff = start.getDate() - day + (day === 0 ? -6 : 1);
    start.setDate(diff);
    start.setHours(0, 0, 0, 0);
    return start;
  }

  // Get trainings for specific date
  getTrainingsForDate(date: Date): Training[] {
    const dateStr = this.formatDate(date);
    return this.trainings.filter(training => training.date === dateStr);
  }

  // Navigation
  previousMonth(): void {
    this.currentDate.setMonth(this.currentDate.getMonth() - 1);
    this.generateCalendar();
    if (this.viewMode === 'week') {
      this.generateWeekView();
    }
  }

  nextMonth(): void {
    this.currentDate.setMonth(this.currentDate.getMonth() + 1);
    this.generateCalendar();
    if (this.viewMode === 'week') {
      this.generateWeekView();
    }
  }

  previousWeek(): void {
    const current = this.selectedDate || this.currentDate;
    current.setDate(current.getDate() - 7);
    this.generateWeekView();
  }

  nextWeek(): void {
    const current = this.selectedDate || this.currentDate;
    current.setDate(current.getDate() + 7);
    this.generateWeekView();
  }

  // Select day
  selectDay(day: CalendarDay): void {
    // Don't allow selecting past days for adding trainings
    if (day.isPast && !day.trainings.length) {
      return;
    }

    this.selectedDate = day.date;
    this.generateCalendar();
    if (this.viewMode === 'week') {
      this.generateWeekView();
    }
  }

  // Change view mode
  setViewMode(mode: 'month' | 'week' | 'day'): void {
    this.viewMode = mode;
    if (mode === 'week') {
      this.generateWeekView();
    }
  }

  // Open add modal with validation
  openAddModal(preselectedDate?: Date): void {
    const targetDate = preselectedDate || this.selectedDate || new Date();

    // Prevent adding trainings to past dates
    if (this.isPastDate(targetDate)) {
      this.showError('Nie można dodać treningu w przeszłości!');
      return;
    }

    this.editingTraining = null;
    this.newTraining = {
      date: this.formatDate(targetDate),
      startTime: '',
      duration: 60,
      clientName: '',
      location: '',
      notes: '',
      status: 'confirmed'
    };
    this.selectedDurationOption = 60;
    this.customDuration = 60;
    this.validationErrors = [];
    this.showAddModal = true;
  }

  // Open training details
  openTrainingDetails(training: Training): void {
    this.selectedTraining = training;
    this.showDetailsModal = true;
  }

  // Duration selection
  selectDuration(duration: number): void {
    this.selectedDurationOption = duration;
    if (duration > 0) {
      this.newTraining.duration = duration;
    } else {
      // Custom duration
      this.newTraining.duration = this.customDuration;
    }
  }

  onCustomDurationChange(): void {
    if (this.selectedDurationOption === 0) {
      this.newTraining.duration = this.customDuration;
    }
  }

  // Validation
  validateTraining(): boolean {
    this.validationErrors = [];

    if (!this.newTraining.date) {
      this.validationErrors.push('Data jest wymagana');
    } else if (this.isPastDate(new Date(this.newTraining.date))) {
      this.validationErrors.push('Nie można zaplanować treningu w przeszłości');
    }

    if (!this.newTraining.startTime) {
      this.validationErrors.push('Godzina rozpoczęcia jest wymagana');
    }

    if (!this.newTraining.clientName?.trim()) {
      this.validationErrors.push('Imię klienta jest wymagane');
    }

    if (!this.newTraining.location?.trim()) {
      this.validationErrors.push('Lokalizacja jest wymagana');
    }

    if (!this.newTraining.duration || this.newTraining.duration < 15) {
      this.validationErrors.push('Czas trwania musi wynosić co najmniej 15 minut');
    }

    // Check for time conflicts
    if (this.newTraining.date && this.newTraining.startTime && this.newTraining.duration) {
      const conflict = this.checkTimeConflict(
        this.newTraining.date,
        this.newTraining.startTime,
        this.newTraining.duration,
        this.editingTraining?.id
      );

      if (conflict) {
        this.validationErrors.push(`Konflikt czasowy z treningiem: ${conflict.clientName} (${conflict.startTime})`);
      }
    }

    return this.validationErrors.length === 0;
  }

  checkTimeConflict(date: string, startTime: string, duration: number, excludeId?: string): Training | null {
    const existingTrainings = this.trainings.filter(t =>
      t.date === date && t.id !== excludeId
    );

    const newStart = this.timeToMinutes(startTime);
    const newEnd = newStart + duration;

    for (const training of existingTrainings) {
      const existingStart = this.timeToMinutes(training.startTime);
      const existingEnd = existingStart + training.duration;

      // Check overlap
      if (newStart < existingEnd && newEnd > existingStart) {
        return training;
      }
    }

    return null;
  }

  timeToMinutes(time: string): number {
    const [hours, minutes] = time.split(':').map(Number);
    return hours * 60 + minutes;
  }

  minutesToTime(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}`;
  }

  // Save training
  saveTraining(): void {
    if (!this.validateTraining()) {
      return;
    }

    if (this.editingTraining) {
      // Update existing training
      const index = this.trainings.findIndex(t => t.id === this.editingTraining!.id);
      if (index !== -1) {
        this.trainings[index] = {
          ...this.editingTraining,
          ...this.newTraining as Training
        };
        this.showSuccess('Trening został zaktualizowany!');
      }
    } else {
      // Add new training
      const newId = (Date.now() + Math.random()).toString();
      const training: Training = {
        id: newId,
        date: this.newTraining.date!,
        startTime: this.newTraining.startTime!,
        duration: this.newTraining.duration!,
        clientName: this.newTraining.clientName!,
        location: this.newTraining.location!,
        notes: this.newTraining.notes!,
        status: this.newTraining.status as 'confirmed' | 'pending' | 'cancelled'
      };

      this.trainings.push(training);
      this.showSuccess('Trening został dodany!');
    }

    this.generateCalendar();
    this.generateWeekView();
    this.closeAddModal();
  }

  // Delete training
  deleteTraining(trainingId: string): void {
    if (confirm('Czy na pewno chcesz usunąć ten trening?')) {
      this.trainings = this.trainings.filter(t => t.id !== trainingId);
      this.generateCalendar();
      this.generateWeekView();
      this.closeDetailsModal();
      this.showSuccess('Trening został usunięty!');
    }
  }

  // Edit training
  editTraining(training: Training): void {
    // Check if training is in the past
    const trainingDate = new Date(training.date);
    if (this.isPastDate(trainingDate)) {
      this.showError('Nie można edytować treningu z przeszłości!');
      return;
    }

    this.editingTraining = training;
    this.newTraining = { ...training };
    this.selectedDurationOption = training.duration;
    this.customDuration = training.duration;
    this.validationErrors = [];
    this.closeDetailsModal();
    this.showAddModal = true;
  }

  // Helper methods
  isPastDate(date: Date): boolean {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const checkDate = new Date(date);
    checkDate.setHours(0, 0, 0, 0);
    return checkDate < today;
  }

  // Close modals
  closeAddModal(): void {
    this.showAddModal = false;
    this.newTraining = {};
    this.editingTraining = null;
    this.validationErrors = [];
  }

  closeDetailsModal(): void {
    this.showDetailsModal = false;
    this.selectedTraining = null;
  }

  // Get training end time
  getTrainingEndTime(training: Training): string {
    const startMinutes = this.timeToMinutes(training.startTime);
    const endMinutes = startMinutes + training.duration;
    return this.minutesToTime(endMinutes);
  }

  // Format duration for display
  formatDuration(minutes: number): string {
    if (minutes < 60) {
      return `${minutes} min`;
    } else if (minutes === 60) {
      return '1h';
    } else if (minutes === 90) {
      return '1,5h';
    } else if (minutes === 120) {
      return '2h';
    } else {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return mins > 0 ? `${hours}h ${mins}min` : `${hours}h`;
    }
  }

  // Get trainings for time slot in week/day view
  getTrainingsForTimeSlot(date: Date, timeSlot: string): Training[] {
    const dateStr = this.formatDate(date);
    return this.trainings.filter(training => {
      if (training.date !== dateStr) return false;

      const slotMinutes = this.timeToMinutes(timeSlot);
      const trainingStart = this.timeToMinutes(training.startTime);
      const trainingEnd = trainingStart + training.duration;

      return slotMinutes >= trainingStart && slotMinutes < trainingEnd;
    });
  }

  // Filters
  get filteredTrainings(): Training[] {
    return this.trainings.filter(training => {
      const matchesClient = !this.searchClient ||
        training.clientName.toLowerCase().includes(this.searchClient.toLowerCase());
      const matchesStatus = !this.statusFilter || training.status === this.statusFilter;
      return matchesClient && matchesStatus;
    });
  }

  // Helper functions
  isSameDay(date1: Date, date2: Date): boolean {
    return date1.getDate() === date2.getDate() &&
      date1.getMonth() === date2.getMonth() &&
      date1.getFullYear() === date2.getFullYear();
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'confirmed': return 'status-confirmed';
      case 'pending': return 'status-pending';
      case 'cancelled': return 'status-cancelled';
      default: return '';
    }
  }

  getStatusLabel(status: string): string {
    switch (status) {
      case 'confirmed': return 'Potwierdzony';
      case 'pending': return 'Oczekujący';
      case 'cancelled': return 'Anulowany';
      default: return status;
    }
  }

  showSuccess(message: string): void {
    // Implement toast notification
    console.log('✅', message);
    // You can add a proper toast notification library here
  }

  showError(message: string): void {
    // Implement error notification
    console.error('❌', message);
    alert(message); // Temporary - replace with proper notification
  }

  // Navigation methods
  goToHomepage(): void {
    this.router.navigate(['/homepage']);
  }

  logout(): void {
    localStorage.removeItem('gymbucket_user');
    localStorage.removeItem('gymbucket_token');
    sessionStorage.removeItem('gymbucket_user');
    sessionStorage.removeItem('gymbucket_token');
    this.router.navigate(['/login']);
  }

  // API methods - ready for backend integration
  async loadTrainings(): Promise<void> {
    try {
      // GET /api/trainings
      // const response = await this.http.get<Training[]>('/api/trainings').toPromise();
      // this.trainings = response || [];
      // this.generateCalendar();
      // this.generateWeekView();
    } catch (error) {
      this.showError('Błąd podczas ładowania treningów');
    }
  }

  async saveTrainingToAPI(training: Training): Promise<void> {
    try {
      // POST /api/trainings
      // await this.http.post('/api/trainings', training).toPromise();
    } catch (error) {
      this.showError('Błąd podczas zapisywania treningu');
    }
  }

  async updateTrainingAPI(training: Training): Promise<void> {
    try {
      // PUT /api/trainings/:id
      // await this.http.put(`/api/trainings/${training.id}`, training).toPromise();
    } catch (error) {
      this.showError('Błąd podczas aktualizacji treningu');
    }
  }

  async deleteTrainingAPI(trainingId: string): Promise<void> {
    try {
      // DELETE /api/trainings/:id
      // await this.http.delete(`/api/trainings/${trainingId}`).toPromise();
    } catch (error) {
      this.showError('Błąd podczas usuwania treningu');
    }
  }
}
