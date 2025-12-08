import { Injectable, computed, effect, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Currency } from '../models/currency.model';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { ConversionResult } from '../models/conversion-result.model';
import { HistoricalRatePoint } from '../models/historical-rate-point.model';
import { LatestRatesResponse } from '../models/latest-rates.model';



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

  convert(from: string, to: string, amount: number) {
    return this.http.get<ConversionResult>('/currency/convert', {
      params: {
        from,
        to,
        amount: amount.toString(),
      },
    });
  }

  getHistory(from: string, to: string, days: number) {
    return this.http.get<HistoricalRatePoint[]>('/currency/history', {
      params: {
        from,
        to,
        days: days.toString(),
      },
    });
  }

  getLatestRates(base: string, symbols: string[]) {
    const params: { [key: string]: string } = {
      base,
    };
  
    if (symbols && symbols.length > 0) {
      params['symbols'] = symbols.join(',');
    }
  
    return this.http.get<LatestRatesResponse>('/currency/latest', { params });
  }
}


