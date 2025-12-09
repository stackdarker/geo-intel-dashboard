import {
    Component,
    OnInit,
    inject,
    signal,
    computed,
  } from '@angular/core';
  import { CommonModule } from '@angular/common';
  import { HttpClient, HttpParams } from '@angular/common/http';
  import { RouterModule } from '@angular/router';
  import { WeatherSummary } from '../../models/weather-summary.model';
  
  type TempUnit = 'C' | 'F';
  
  @Component({
    standalone: true,
    selector: 'app-weather-dashboard-widget',
    imports: [CommonModule, RouterModule],
    templateUrl: './weather-dashboard-widget.component.html',
    styleUrls: ['./weather-dashboard-widget.component.scss'],
  })
  export class WeatherDashboardWidgetComponent implements OnInit {
    private http = inject(HttpClient);
  
    private readonly lastCityKey = 'geoDashboard.weather.lastCity';
    private readonly lastCountryKey = 'geoDashboard.weather.lastCountryCode';
    private readonly tempUnitKey = 'geoDashboard.weather.tempUnit';
  
    private readonly _summary = signal<WeatherSummary | null>(null);
    private readonly _loading = signal(false);
    private readonly _error = signal<string | null>(null);
  
    readonly summary = computed(() => this._summary());
    readonly loading = computed(() => this._loading());
    readonly error = computed(() => this._error());
  
    tempUnit: TempUnit = this.loadInitialUnit();
  
    ngOnInit(): void {
      const city = localStorage.getItem(this.lastCityKey);
      const countryCode = localStorage.getItem(this.lastCountryKey);
  
      if (!city) {
        this._error.set('No default city. Use the Weather page to search once.');
        return;
      }
  
      this.loadCurrent(city, countryCode ?? undefined);
    }
  
    private loadInitialUnit(): TempUnit {
      const stored = localStorage.getItem(this.tempUnitKey);
      return stored === 'F' ? 'F' : 'C';
    }
  
    private loadCurrent(city: string, countryCode?: string): void {
      this._loading.set(true);
      this._error.set(null);
  
      let params = new HttpParams().set('city', city);
      if (countryCode) {
        params = params.set('countryCode', countryCode);
      }
  
      this.http.get<WeatherSummary>('/weather/current', { params }).subscribe({
        next: (res) => {
          this._summary.set(res);
          this._loading.set(false);
        },
        error: (err) => {
          console.error('Weather dashboard widget error', err);
          this._error.set('Unable to load weather snapshot.');
          this._loading.set(false);
        },
      });
    }
  
    convertTemp(temp: number | null | undefined): number | null {
      if (temp === null || temp === undefined) {
        return null;
      }
      if (this.tempUnit === 'C') {
        return temp;
      }
      return (temp * 9) / 5 + 32;
    }
  
    getTempUnitLabel(): string {
      return this.tempUnit === 'C' ? '°C' : '°F';
    }
  }
  