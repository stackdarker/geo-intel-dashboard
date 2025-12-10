import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InsightsApiService } from '../../services/insights-api.service';
import { GlobalPopulationChartComponent } from '../global-population-chart/global-population-chart.component';
import { WatchlistCorrelationChartComponent } from '../watchlist-correlation-chart/watchlist-correlation-chart.component';

@Component({
  standalone: true,
  selector: 'app-insights-home',
  imports: [CommonModule, GlobalPopulationChartComponent, WatchlistCorrelationChartComponent],
  templateUrl: './insights-home.component.html',
  styleUrls: ['./insights-home.component.scss'],
})
export class InsightsHomeComponent implements OnInit {
  private insightsApi = inject(InsightsApiService);

  overview = this.insightsApi.globalOverview;
  loading = this.insightsApi.loading;
  error = this.insightsApi.error;

  watchlistInsights = this.insightsApi.watchlistInsights;

  private readonly watchlistKey = 'geoDashboard.insights.watchlist';
  watchlistCodes = signal<string[]>([]);
  newCode = signal('');

  ngOnInit(): void {
    this.insightsApi.loadGlobalOverview();
    this.restoreWatchlist();
    this.refreshWatchlist();
  }

  get baseCurrency(): string {
    return this.overview()?.baseCurrency ?? 'USD';
  }

  get topPopulation() {
    return this.overview()?.topPopulation ?? [];
  }

  get watchlistItems() {
    return this.watchlistInsights()?.items ?? [];
  }

  private restoreWatchlist(): void {
    const raw = localStorage.getItem(this.watchlistKey);
    if (!raw) {
      this.watchlistCodes.set(['US', 'JP', 'DE']);
      return;
    }
    try {
      const parsed = JSON.parse(raw);
      if (Array.isArray(parsed)) {
        this.watchlistCodes.set(parsed);
      }
    } catch {
      this.watchlistCodes.set(['US', 'JP', 'DE']);
    }
  }

  private saveWatchlist(): void {
    localStorage.setItem(this.watchlistKey, JSON.stringify(this.watchlistCodes()));
  }

  addCode(): void {
    const code = this.newCode().trim().toUpperCase();
    if (!code) return;

    if (!this.watchlistCodes().includes(code)) {
      this.watchlistCodes.set([...this.watchlistCodes(), code]);
      this.saveWatchlist();
      this.refreshWatchlist();
    }
    this.newCode.set('');
  }

  removeCode(code: string): void {
    this.watchlistCodes.set(this.watchlistCodes().filter(c => c !== code));
    this.saveWatchlist();
    this.refreshWatchlist();
  }

  refreshWatchlist(): void {
    this.insightsApi.loadWatchlistInsights(this.watchlistCodes());
  }

  onNewCodeChange(value: string): void {
    this.newCode.set(value);
  }
}
