import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CountryInsights } from '../models/country-insights.model';
import { GlobalInsightsOverview } from '../models/global-insights-overview.model';
import { catchError } from 'rxjs/operators';
import { of, tap } from 'rxjs';
import { WatchlistInsightsResponse } from '../models/watchlist-insights-response.model';


@Injectable({
  providedIn: 'root',
})
export class InsightsApiService {
  private http = inject(HttpClient);

  private readonly fxBaseKey = 'geoDashboard.fxBaseCurrency';

  private readonly _countryInsights = signal<CountryInsights | null>(null);

  private readonly _globalOverview = signal<GlobalInsightsOverview | null>(null);

  private readonly _watchlistInsights = signal<WatchlistInsightsResponse | null>(null);

  readonly watchlistInsights = computed(() => this._watchlistInsights());

  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly countryInsights = computed(() => this._countryInsights());
  readonly globalOverview = computed(() => this._globalOverview());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

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
          if (res) {
            this._countryInsights.set(res);
          }
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

  loadGlobalOverview(): void {
    const baseCurrency = localStorage.getItem(this.fxBaseKey) || 'USD';

    let params = new HttpParams().set('baseCurrency', baseCurrency);

    this._loading.set(true);
    this._error.set(null);
    this._globalOverview.set(null);

    this.http
      .get<GlobalInsightsOverview>('/insights/global/overview', { params })
      .pipe(
        tap((res) => {
          if (res) {
            this._globalOverview.set(res);
          }
        }),
        catchError((err) => {
          console.error('Failed to load global insights overview', err);
          this._error.set(
            err?.error?.message || 'Unable to load global insights overview.'
          );
          return of(null);
        })
      )
      .subscribe({
        complete: () => this._loading.set(false),
      });
  }

  loadWatchlistInsights(codes: string[]): void {
    const cleaned = codes
      .map(c => c.trim())
      .filter(c => !!c);
  
    if (cleaned.length === 0) {
      this._watchlistInsights.set({
        baseCurrency: localStorage.getItem(this.fxBaseKey) || 'USD',
        items: [],
        generatedAt: new Date().toISOString(),
      });
      return;
    }
  
    const baseCurrency = localStorage.getItem(this.fxBaseKey) || 'USD';
  
    let params = new HttpParams().set('baseCurrency', baseCurrency);
    cleaned.forEach(code => {
      params = params.append('codes', code);
    });
  
    this._loading.set(true);
    this._error.set(null);
  
    this.http
      .get<WatchlistInsightsResponse>('/insights/watchlist', { params })
      .pipe(
        tap(res => {
          if (res) {
            this._watchlistInsights.set(res);
          }
        }),
        catchError(err => {
          console.error('Failed to load watchlist insights', err);
          this._error.set(
            err?.error?.message || 'Unable to load watchlist insights.'
          );
          return of(null);
        })
      )
      .subscribe({
        complete: () => this._loading.set(false),
      });
  }
  
}
