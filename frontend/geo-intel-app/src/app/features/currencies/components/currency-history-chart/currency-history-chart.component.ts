import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Input,
  OnChanges,
  SimpleChanges,
  ViewChild,
  inject,
  signal,
} from '@angular/core';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
Chart.register(...registerables);

import { CurrencyApiService } from '../../services/currency-api.service';
import { HistoricalRatePoint } from '../../models/historical-rate-point.model';

@Component({
  selector: 'app-currency-history-chart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './currency-history-chart.component.html',
  styleUrls: ['./currency-history-chart.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurrencyHistoryChartComponent implements OnChanges {
  private api = inject(CurrencyApiService);

  @Input() fromCode!: string;
  @Input() toCode!: string;

  @ViewChild('chartCanvas', { static: false })
  chartCanvas!: ElementRef<HTMLCanvasElement>;

  private chart: Chart | null = null;

  readonly daysOptions = [7, 30, 90];
  readonly selectedDays = signal<number>(30);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly points = signal<HistoricalRatePoint[]>([]);

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['fromCode'] || changes['toCode']) && this.fromCode && this.toCode) {
      this.loadHistory();
    }
  }

  onChangeDays(days: number): void {
    if (this.selectedDays() === days) {
      return;
    }
    this.selectedDays.set(days);
    this.loadHistory();
  }

  private loadHistory(): void {
    if (!this.fromCode || !this.toCode) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.api
      .getHistory(this.fromCode, this.toCode, this.selectedDays())
      .subscribe({
        next: (points) => {
          this.points.set(points);
          this.updateChart(points);
          this.loading.set(false);
        },
        error: (err) => {
          console.error('Failed to load historical rates', err);
          this.error.set('Unable to load historical data.');
          this.loading.set(false);
        },
      });
  }

  private updateChart(points: HistoricalRatePoint[]): void {
    console.log('updateChart called, points:', points);
    if (!this.chartCanvas) {
      return;
    }

    if (!points || points.length === 0) {
      if (this.chart) {
        this.chart.destroy();
        this.chart = null;
      }
      return;
    }

    const ctx = this.chartCanvas.nativeElement.getContext('2d');
    if (!ctx) {
      return;
    }

    const labels = points.map((p) => p.date);
    const data = points.map((p) => p.rate);

    const config: ChartConfiguration<'line'> = {
      type: 'line',
      data: {
        labels,
        datasets: [
          {
            data,
            label: `${this.fromCode} → ${this.toCode}`,
            fill: false,
            tension: 0.2,
            borderWidth: 2,
            borderColor: '#e5e7eb',
            pointRadius: 0,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            ticks: { color: '#f5f5f5' },
            grid: { color: '#4b5563' },
          },
          y: {
            ticks: { color: '#f5f5f5' },
            grid: { color: '#4b5563' },
          },
        },
        plugins: {
          legend: {
            labels: {
              color: '#f5f5f5',
            },
          },
        },
      },
    };

    if (this.chart) {
      this.chart.destroy();
    }
    this.chart = new Chart(ctx, config);
    console.log('Chart created', this.chart);
    
  }
}
