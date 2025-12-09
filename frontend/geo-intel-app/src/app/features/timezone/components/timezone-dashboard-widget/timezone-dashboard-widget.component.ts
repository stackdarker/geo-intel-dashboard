import {
    Component,
    OnInit,
    inject,
    signal,
    computed,
  } from '@angular/core';
  import { CommonModule } from '@angular/common';
  import { RouterModule } from '@angular/router';
  import { TimezoneApiService } from '../../services/timezone-api.service';
  import { TimeNowResponse } from '../../models/time-now-response.model';
  
  @Component({
    standalone: true,
    selector: 'app-timezone-dashboard-widget',
    imports: [CommonModule, RouterModule],
    templateUrl: './timezone-dashboard-widget.component.html',
    styleUrls: ['./timezone-dashboard-widget.component.scss'],
  })
  export class TimezoneDashboardWidgetComponent implements OnInit {
    private timezoneApi = inject(TimezoneApiService);
  
    private readonly favoritesKey = 'geoDashboard.timezones.favorites';
  
    favorites = signal<string[]>([]);
    worldClock = this.timezoneApi.worldClock;
    loading = this.timezoneApi.loading;
    error = this.timezoneApi.error;
  
    ngOnInit(): void {
      this.restoreFavorites();
      this.refresh();
      this.startAutoRefresh();
    }
  
    private restoreFavorites(): void {
      const raw = localStorage.getItem(this.favoritesKey);
      if (!raw) return;
  
      try {
        const parsed = JSON.parse(raw);
        if (Array.isArray(parsed)) {
          this.favorites.set(parsed);
        }
      } catch (_) {}
    }
  
    refresh(): void {
      this.timezoneApi.loadWorldClock(this.favorites());
    }
  
    private startAutoRefresh(): void {
      setInterval(() => this.refresh(), 60000); 
    }
  }
  