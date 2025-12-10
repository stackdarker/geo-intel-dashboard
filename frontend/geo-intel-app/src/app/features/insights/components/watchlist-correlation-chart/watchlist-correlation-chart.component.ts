import {
    AfterViewInit,
    Component,
    Input,
    OnChanges,
    OnDestroy,
    SimpleChanges,
    ViewChild,
    ElementRef,
  } from '@angular/core';
  import { CommonModule } from '@angular/common';
  import { WatchlistCountryInsight } from '../../models/watchlist-country-insight.model';
  import {
    Chart,
    ChartConfiguration,
    BarController,
    BarElement,
    LineController,
    LineElement,
    PointElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
  } from 'chart.js';
  
  Chart.register(
    BarController,
    BarElement,
    LineController,
    LineElement,
    PointElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend
  );
  
  @Component({
    standalone: true,
    selector: 'app-watchlist-correlation-chart',
    imports: [CommonModule],
    template: `
      <div class="watchlist-chart" *ngIf="data && data.length">
        <canvas #canvas></canvas>
      </div>
      <p class="muted" *ngIf="!data || !data.length">
        No watchlist data available.
      </p>
    `,
    styleUrls: ['./watchlist-correlation-chart.component.scss'],
  })
  export class WatchlistCorrelationChartComponent
    implements AfterViewInit, OnChanges, OnDestroy
  {
    @Input() data: WatchlistCountryInsight[] | null = null;
    @Input() baseCurrency: string = 'USD';
  
    @ViewChild('canvas', { static: false }) canvasRef?: ElementRef<HTMLCanvasElement>;
  
    private chart: Chart | null = null;
  
    ngAfterViewInit(): void {
      this.buildChart();
    }
  
    ngOnChanges(changes: SimpleChanges): void {
      if (changes['data'] || changes['baseCurrency']) {
        this.buildChart();
      }
    }
  
    ngOnDestroy(): void {
      this.destroyChart();
    }
  
    private destroyChart(): void {
      if (this.chart) {
        this.chart.destroy();
        this.chart = null;
      }
    }
  
    private buildChart(): void {
      if (!this.canvasRef) return;
  
      const list = (this.data ?? []).filter(
        d => d.population > 0 && d.fxRate != null
      );
      if (!list.length) {
        this.destroyChart();
        return;
      }
  
      const ctx = this.canvasRef.nativeElement.getContext('2d');
      if (!ctx) return;
  
      const labels = list.map(d => d.code);
      const populations = list.map(d => d.population);
      const fxRates = list.map(d => d.fxRate as number);
  
      const config: ChartConfiguration<'bar' | 'line'> = {
        type: 'bar',
        data: {
          labels,
          datasets: [
            {
              type: 'bar',
              label: 'Population',
              data: populations,
              yAxisID: 'yPopulation',
              backgroundColor: 'rgba(154,180,255,0.8)',
              borderColor: '#ffffff',
            },
            {
              type: 'line',
              label: `Value vs ${this.baseCurrency}`,
              data: fxRates,
              yAxisID: 'yFx',
              borderColor: '#ffffff',
              backgroundColor: '#ffffff',
              tension: 0.3,
              pointRadius: 3,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              labels: {
                color: '#ffffff',
              },
            },
            tooltip: {
              callbacks: {
                label: (ctx) => {
                    const y = ctx.parsed?.y;
                    if (y == null) return ''; 
                  
                    if (ctx.datasetIndex === 0) {
                      return `Population: ${y.toLocaleString()}`;
                    }
                  
                    return `FX: ${y}`;
                  },                  
              },
            },
          },
          scales: {
            x: {
              ticks: { color: '#ffffff' },
              grid: { color: 'rgba(255,255,255,0.15)' },
            },
            yPopulation: {
              type: 'linear',
              position: 'left',
              ticks: { color: '#ffffff' },
              grid: { color: 'rgba(255,255,255,0.15)' },
            },
            yFx: {
              type: 'linear',
              position: 'right',
              ticks: { color: '#ffffff' },
              grid: { drawOnChartArea: false },
            },
          },
        },
      };
  
      this.destroyChart();
      this.chart = new Chart(ctx, config);
    }
  }
  