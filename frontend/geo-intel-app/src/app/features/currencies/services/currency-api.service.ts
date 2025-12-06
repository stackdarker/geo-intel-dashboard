import { Injectable, computed, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Currency } from '../models/currency.model';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CurrencyApiService {
  private http = inject(HttpClient);

  private readonly _currencies = signal<Currency[]>([]);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);
  private readonly _hasLoadedOnce = signal(false);

  readonly currencies = computed(() => this._currencies());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

    // Effect to load currencies on service initialization
  loadCurrencies(forceRefresh = false): void {
    if (this._hasLoadedOnce() && !forceRefresh) {
      return;
    }

    this._loading.set(true);
    this._error.set(null);

    this.http
    .get<Currency[]>('/currency/symbols/list')
    .pipe(
      tap((data) => {
        this._currencies.set(data);
        this._hasLoadedOnce.set(true);
        this._loading.set(false);
      }),
      catchError((err) => {
        console.error('Failed to load currencies', err);
        this._error.set('Unable to load currencies. Please try again.');
        this._loading.set(false);
        return of([] as Currency[]);
      })
    )
    .subscribe();
  }
}
