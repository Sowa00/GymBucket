import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, NavigationEnd } from '@angular/router';
import { Subject, takeUntil, filter } from 'rxjs';
import { ClientService, Client as ApiClient, ClientStats } from '../services/client.service';
import { TrainingSessionService, TrainingSession } from '../services/training-session.service';
import { AuthService } from '../services/auth.service';

interface Client {
  id: number;
  name: string;
  time: string;
  type: string;
  lastSession?: string;
  progress?: string;
}

interface Stats {
  totalClients: number;
  todaysSessions: number;
  weeklyRevenue: string;
  completionRate: string;
}

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit, OnDestroy {
  selectedTab = 'dashboard';
  searchQuery = '';
  isLoading = false;
  errorMessage = '';

  // Real data
  stats: Stats = {
    totalClients: 0,
    todaysSessions: 0,
    weeklyRevenue: '0 zł',
    completionRate: '0%'
  };

  realClients: ApiClient[] = [];
  clientStats: ClientStats | null = null;
  realSessions: TrainingSession[] = [];

  private destroy$ = new Subject<void>();

  upcomingClients: Client[] = [];

  recentActivity: Client[] = [];

  navigationItems = [
    { id: 'dashboard', label: 'Dashboard', icon: '🏠', active: true },
    { id: 'clients', label: 'Klienci', icon: '👥', active: false },
    { id: 'calendar', label: 'Kalendarz', icon: '📅', active: false },
    { id: 'workouts', label: 'Plany treningowe', icon: '💪', active: false },
    { id: 'nutrition', label: 'Plany żywieniowe', icon: '🥗', active: false }
  ];

  quickActions = [
    {
      title: 'Stwórz Plan Treningowy',
      description: 'Zaprojektuj spersonalizowane ćwiczenia',
      icon: '💪',
      color: 'blue'
    },
    {
      title: 'Plan Żywieniowy',
      description: 'Twórz jadłospisy i monitoruj odżywianie',
      icon: '🥗',
      color: 'green'
    },
    {
      title: 'Zaplanuj Sesję',
      description: 'Umów spotkania z klientami',
      icon: '📅',
      color: 'purple'
    }
  ];

  constructor(
    private router: Router,
    private clientService: ClientService,
    private trainingSessionService: TrainingSessionService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadRealData();
    
    // Listen for navigation events to refresh data when returning to homepage
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/homepage' || event.url === '/') {
          this.loadRealData();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Load real data from API
  loadRealData(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Load client statistics
    this.clientService.getClientStats()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (stats) => {
          this.clientStats = stats;
          this.updateStatsDisplay(stats);
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading client stats:', error);
          this.errorMessage = 'Błąd podczas ładowania statystyk';
          this.isLoading = false;
        }
      });

    // Load active clients for upcoming sessions
    this.clientService.getActiveClients()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (clients) => {
          this.realClients = clients;
          this.updateUpcomingClients(clients);
        },
        error: (error) => {
          console.error('Error loading clients:', error);
        }
      });

    // Load today's training sessions
    this.trainingSessionService.getTodaysSessions()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (sessions) => {
          this.realSessions = sessions;
          this.updateTodaysSessions(sessions);
        },
        error: (error) => {
          console.error('Error loading today\'s sessions:', error);
        }
      });
  }

  // Update stats display with real data
  updateStatsDisplay(stats: ClientStats): void {
    this.stats = {
      totalClients: stats.totalClients,
      todaysSessions: this.realSessions.length,
      weeklyRevenue: `${stats.monthlyRevenue.toFixed(0)} zł`,
      completionRate: stats.totalClients > 0 ? `${Math.round((stats.paidClients / stats.totalClients) * 100)}%` : '0%'
    };
  }

  // Update upcoming clients with real data
  updateUpcomingClients(clients: ApiClient[]): void {
    // For now, show first 3 active clients as "upcoming"
    // In a real app, this would be actual scheduled sessions
    this.upcomingClients = clients.slice(0, 3).map((client, index) => ({
      id: client.id,
      name: this.clientService.getClientFullName(client),
      time: `${9 + index * 2}:00`, // Mock times
      type: 'Trening siłowy' // Mock type
    }));
  }

  // Update today's sessions with real data
  updateTodaysSessions(sessions: TrainingSession[]): void {
    // Update the stats with real session count
    if (this.clientStats) {
      this.updateStatsDisplay(this.clientStats);
    }

    // Update upcoming clients with real session data
    this.upcomingClients = sessions.slice(0, 3).map((session) => ({
      id: session.id,
      name: session.clientName,
      time: this.trainingSessionService.formatSessionTime(session.startTime, session.endTime),
      type: session.sessionType
    }));
  }

  selectTab(tabId: string): void {
    this.selectedTab = tabId;
    this.navigationItems.forEach(item => {
      item.active = item.id === tabId;
    });

    // Nawigacja do odpowiednich stron
    switch (tabId) {
      case 'dashboard':
        // Refresh data when returning to dashboard
        this.loadRealData();
        break;
      case 'clients':
        this.router.navigate(['/clients']);
        break;
      case 'calendar':
        this.router.navigate(['/calendar']);
        break;
      case 'workouts':
        console.log('Nawigacja do planów treningowych - w przygotowaniu');

        this.router.navigate(['/workout-plans']);
        break;
      case 'nutrition':
        console.log('Nawigacja do planów żywieniowych');
        this.router.navigate(['/nutrition-plans']);
        break;
    }
  }

  onSearch(): void {
    console.log('Szukanie:', this.searchQuery);
    // Implement search functionality
  }

  showNotifications(): void {
    console.log('Pokazywanie powiadomień');
    // Implement notifications
  }

  addNewClient(): void {
    this.router.navigate(['/clients']);
  }

  viewClientDetails(client: Client): void {
    console.log('Szczegóły klienta:', client);
    // Implement client details view
  }

  addNewSession(): void {
    console.log('Dodawanie nowej sesji');
    // Implement add session functionality
  }

  viewAllClients(): void {
    this.router.navigate(['/clients']);
  }

  executeQuickAction(action: any): void {
    console.log('Wykonywanie akcji:', action.title);
    // Implement quick action functionality
  }

  getInitials(name: string): string {
    return name
      .split(' ')
      .map(part => part.charAt(0))
      .join('')
      .toUpperCase();
  }

  logout(): void {
    this.authService.logout();
  }
}
