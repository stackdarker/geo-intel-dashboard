import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CountryInsights } from '../models/country-insights.model';
import { catchError } from 'rxjs/operators';
import { of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class InsightsApiService {
  private http = inject(HttpClient);

  private readonly _countryInsights = signal<CountryInsights | null>(null);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly countryInsights = computed(() => this._countryInsights());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

  private readonly fxBaseKey = 'geoDashboard.fxBaseCurrency';

  loadCountryInsights(countryCode: string, timeZone?: string): void {
    if (!countryCode?.trim()) {
      return;
    }

    const baseCurrency = localStorage.getItem(this.fxBaseKey) || 'USD';

    let params = new HttpParams().set('baseCurrency', baseCurrency);
    if (timeZone?.trim()) {
      params = params.set('timeZone', timeZone.trim());
    }

    this._loading.set(true);
    this._error.set(null);
    this._countryInsights.set(null);

    this.http
      .get<CountryInsights>(`/insights/countries/${countryCode}`, { params })
      .pipe(
        tap((res) => {
          this._countryInsights.set(res);
        }),
        catchError((err) => {
          console.error('Failed to load country insights', err);
          this._error.set(
            err?.error?.message || 'Unable to load country insights.'
          );
          return of(null);
        })
      )
      .subscribe({
        complete: () => this._loading.set(false),
      });
  }
}
