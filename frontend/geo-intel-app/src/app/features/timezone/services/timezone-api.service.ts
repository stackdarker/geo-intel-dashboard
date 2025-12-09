import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { TimeZoneInfo } from '../models/time-zone-info.model';
import { TimeNowResponse } from '../models/time-now-response.model';
import { catchError } from 'rxjs/operators';
import { of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TimezoneApiService {
  private http = inject(HttpClient);

  private readonly _zones = signal<TimeZoneInfo[]>([]);
  private readonly _worldClock = signal<TimeNowResponse[]>([]);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly zones = computed(() => this._zones());
  readonly worldClock = computed(() => this._worldClock());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

  loadZones(): void {
    this._loading.set(true);
    this._error.set(null);

    this.http
      .get<TimeZoneInfo[]>('/time/zones')
      .pipe(
        tap((zones) => {
          this._zones.set(zones ?? []);
        }),
        catchError((err) => {
          console.error('Failed to load time zones', err);
          this._error.set('Unable to load time zones.');
          return of([]);
        })
      )
      .subscribe({
        complete: () => this._loading.set(false),
      });
  }

  loadWorldClock(zones: string[]): void {
    if (!zones || zones.length === 0) {
      this._worldClock.set([]);
      return;
    }

    this._loading.set(true);
    this._error.set(null);

    let params = new HttpParams();
    zones.forEach((z) => {
      params = params.append('zones', z);
    });

    this.http
      .get<TimeNowResponse[]>('/time/world-clock', { params })
      .pipe(
        tap((items) => {
          this._worldClock.set(items ?? []);
        }),
        catchError((err) => {
          console.error('Failed to load world clock', err);
          this._error.set('Unable to load world clock.');
          return of([]);
        })
      )
      .subscribe({
        complete: () => this._loading.set(false),
      });
  }
}
