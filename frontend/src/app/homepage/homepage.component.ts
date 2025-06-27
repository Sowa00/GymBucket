import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

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
export class HomepageComponent implements OnInit {
  selectedTab = 'dashboard';
  searchQuery = '';

  // Mock data
  stats: Stats = {
    totalClients: 24,
    todaysSessions: 6,
    weeklyRevenue: '2,480 zł',
    completionRate: '94%'
  };

  upcomingClients: Client[] = [
    { id: 1, name: 'Anna Kowalska', time: '9:00', type: 'Trening siłowy' },
    { id: 2, name: 'Michał Nowak', time: '11:30', type: 'Cardio' },
    { id: 3, name: 'Ewa Wiśniewska', time: '14:00', type: 'Konsultacja żywieniowa' },
    { id: 4, name: 'Tomasz Zieliński', time: '16:30', type: 'Trening funkcjonalny' }
  ];

  recentActivity: Client[] = [
    {
      id: 1, name: 'Aleksandra Mazur', lastSession: '2 dni temu', progress: '+5 kg siła',
      time: '',
      type: ''
    },
    {
      id: 2, name: 'Łukasz Kowal', lastSession: '1 dzień temu', progress: '-2% tłuszcz',
      time: '',
      type: ''
    },
    {
      id: 3, name: 'Marta Włodarczyk', lastSession: '3 dni temu', progress: '+10 kg martwy ciąg',
      time: '',
      type: ''
    },
    {
      id: 4, name: 'Paweł Sowa', lastSession: '1 dzień temu', progress: '+3 cm biceps',
      time: '',
      type: ''
    }
  ];

  navigationItems = [
    { id: 'dashboard', label: 'Dashboard', icon: '🏠', active: true },
    { id: 'clients', label: 'Klienci', icon: '👥', active: false },
    { id: 'calendar', label: 'Kalendarz', icon: '📅', active: false },
    { id: 'workouts', label: 'Plany treningowe', icon: '💪', active: false },
    { id: 'nutrition', label: 'Plany żywieniowe', icon: '🥗', active: false },
    { id: 'settings', label: 'Ustawienia', icon: '⚙️', active: false }
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

  constructor(private router: Router) { }

  ngOnInit(): void {
    // Component initialization
  }

  selectTab(tabId: string): void {
    this.selectedTab = tabId;
    this.navigationItems.forEach(item => {
      item.active = item.id === tabId;
    });

    // Nawigacja do odpowiednich stron
    switch (tabId) {
      case 'dashboard':
        // Zostajemy na homepage
        break;
      case 'clients':
        console.log('Nawigacja do klientów - w przygotowaniu');
        // this.router.navigate(['/clients']);
        break;
      case 'calendar':
        this.router.navigate(['/calendar']);
        break;
      case 'workouts':
        console.log('Nawigacja do planów treningowych - w przygotowaniu');

        this.router.navigate(['/workout-plans']);
        break;
      case 'nutrition':
        console.log('Nawigacja do planów żywieniowych - w przygotowaniu');
        // this.router.navigate(['/nutrition']);
        break;
      case 'settings':
        console.log('Nawigacja do ustawień - w przygotowaniu');
        // this.router.navigate(['/settings']);
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
    console.log('Dodawanie nowego klienta');
    // Implement add client functionality
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
    console.log('Wyświetlanie wszystkich klientów');
    // Navigate to clients page
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
    // Clear any stored auth data
    localStorage.removeItem('gymbucket_user');
    localStorage.removeItem('gymbucket_token');
    sessionStorage.removeItem('gymbucket_user');
    sessionStorage.removeItem('gymbucket_token');

    // Navigate to login
    this.router.navigate(['/login']);
  }
}
