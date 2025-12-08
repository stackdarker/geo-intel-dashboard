import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  signal,
} from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { CurrencyApiService } from '../../services/currency-api.service';
import { LatestRatesResponse } from '../../models/latest-rates.model';

const FX_BASE_PREF_KEY = 'geoDashboard.fxBaseCurrency'; // preference key

@Component({
  selector: 'app-currency-dashboard-widget',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './currency-dashboard-widget.component.html',
  styleUrls: ['./currency-dashboard-widget.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurrencyDashboardWidgetComponent implements OnInit {
  private api = inject(CurrencyApiService);

  private readonly _data = signal<LatestRatesResponse | null>(null);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly data = computed(() => this._data());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

  // allowed base currencies we support in the UI
  readonly allowedBases = ['USD', 'EUR', 'GBP'];

  // base stored as a signal (we'll back it with localStorage)
  readonly base = signal<string>('USD');

  // majors to display against the base
  readonly majors = ['EUR', 'GBP', 'JPY', 'CAD', 'AUD'];

  // bridge for ngModel: getter/setter around the signal
  get baseModel(): string {
    return this.base();
  }

  set baseModel(value: string) {
    this.onBaseChange(value);
  }

  ngOnInit(): void {
    this.loadBasePreference();
    this.loadRates();
  }

  private loadBasePreference(): void {
    if (typeof localStorage === 'undefined') {
      return;
    }

    try {
      const stored = localStorage.getItem(FX_BASE_PREF_KEY);
      if (stored) {
        const normalized = stored.toUpperCase();
        if (this.allowedBases.includes(normalized)) {
          this.base.set(normalized);
        }
      }
    } catch {
      // ignore storage errors; fall back to default 'USD'
    }
  }

  private saveBasePreference(base: string): void {
    if (typeof localStorage === 'undefined') {
      return;
    }
    try {
      localStorage.setItem(FX_BASE_PREF_KEY, base);
    } catch {
      // ignore; not fatal
    }
  }

  loadRates(): void {
    this._loading.set(true);
    this._error.set(null);

    this.api.getLatestRates(this.base(), this.majors).subscribe({
      next: (res) => {
        this._data.set(res);
        this._loading.set(false);
      },
      error: (err) => {
        console.error('Failed to load latest rates', err);
        this._error.set('Unable to load currency snapshot.');
        this._loading.set(false);
      },
    });
  }

  onBaseChange(newBase: string): void {
    const normalized = (newBase ?? '').toUpperCase();
    if (!this.allowedBases.includes(normalized)) {
      return;
    }
    if (normalized === this.base()) {
      return;
    }

    this.base.set(normalized);
    this._data.set(null);
    this.saveBasePreference(normalized);
    this.loadRates();
  }

  getRate(code: string): number | null {
    const d = this.data();
    if (!d) {
      return null;
    }
    return d.rates[code] ?? null;
  }
}
