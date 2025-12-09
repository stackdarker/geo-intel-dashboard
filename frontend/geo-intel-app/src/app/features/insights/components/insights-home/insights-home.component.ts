import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InsightsApiService } from '../../services/insights-api.service';
import { GlobalPopulationChartComponent } from '../global-population-chart/global-population-chart.component';

@Component({
  standalone: true,
  selector: 'app-insights-home',
  imports: [CommonModule, GlobalPopulationChartComponent],
  templateUrl: './insights-home.component.html',
  styleUrls: ['./insights-home.component.scss'],
})
export class InsightsHomeComponent implements OnInit {
  private insightsApi = inject(InsightsApiService);

  overview = this.insightsApi.globalOverview;
  loading = this.insightsApi.loading;
  error = this.insightsApi.error;

  ngOnInit(): void {
    this.insightsApi.loadGlobalOverview();
  }

  get baseCurrency(): string {
    return this.overview()?.baseCurrency ?? 'USD';
  }

  get fxRates() {
    return this.overview()?.fxMajors?.rates ?? {};
  }

  get topPopulation() {
    return this.overview()?.topPopulation ?? [];
  }

  get generatedAt() {
    return this.overview()?.generatedAt ?? null;
  }
}
