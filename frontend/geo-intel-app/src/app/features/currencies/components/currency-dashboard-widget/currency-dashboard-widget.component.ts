import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  signal,
} from '@angular/core';

import { CurrencyApiService } from '../../services/currency-api.service';
import { LatestRatesResponse } from '../../models/latest-rates.model';

@Component({
  selector: 'app-currency-dashboard-widget',
  standalone: true,
  imports: [CommonModule],
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

  // initial base & majors we care about
  readonly base = signal<string>('USD');
  readonly majors = ['EUR', 'GBP', 'JPY', 'CAD', 'AUD'];

  ngOnInit(): void {
    this.loadRates();
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
    if (newBase === this.base()) return;
    this.base.set(newBase);
    this._data.set(null);
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
