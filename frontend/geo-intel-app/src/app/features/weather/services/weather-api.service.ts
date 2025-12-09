import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { WeatherSummary } from '../models/weather-summary.model';
import { WeatherForecastResponse } from '../models/weather-forecast-response.model';
import { catchError } from 'rxjs/operators';
import { of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WeatherApiService {
  private http = inject(HttpClient);

  private readonly _current = signal<WeatherSummary | null>(null);
  private readonly _forecast = signal<WeatherForecastResponse | null>(null);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly current = computed(() => this._current());
  readonly forecast = computed(() => this._forecast());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

  loadWeather(city: string, countryCode?: string): void {
    if (!city || !city.trim()) {
      return;
    }

    const trimmedCity = city.trim();
    const trimmedCountry = countryCode?.trim();

    this._loading.set(true);
    this._error.set(null);
    this._current.set(null);
    this._forecast.set(null);

    let currentParams = new HttpParams().set('city', trimmedCity);
    if (trimmedCountry) {
      currentParams = currentParams.set('countryCode', trimmedCountry);
    }

    const current$ = this.http.get<WeatherSummary>('/weather/current', {
      params: currentParams,
    });

    let forecastParams = new HttpParams()
      .set('city', trimmedCity)
      .set('hours', '24');
      
    if (trimmedCountry) {
      forecastParams = forecastParams.set('countryCode', trimmedCountry);
    }

    const forecast$ = this.http.get<WeatherForecastResponse>('/weather/forecast', {
      params: forecastParams,
    });

    current$
      .pipe(
        tap((summary) => {
          if (summary) {
            this._current.set(summary);
          }
        }),
        catchError((err) => {
          console.error('Failed to load current weather', err);
          this._error.set('Unable to load current weather.');
          return of(null);
        })
      )
      .subscribe(); 

    forecast$
      .pipe(
        tap((forecast) => {
          if (forecast) {
            this._forecast.set(forecast);
          }
        }),
        catchError((err) => {
          console.error('Failed to load forecast', err);
          if (!this._error()) {
            this._error.set('Unable to load forecast.');
          }
          return of(null);
        })
      )
      .subscribe({
        complete: () => {
          this._loading.set(false);
        },
      });
  }
}
