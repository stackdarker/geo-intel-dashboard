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
  import {
    Chart,
    ChartConfiguration,
    LineController,
    LineElement,
    PointElement,
    LinearScale,
    CategoryScale,
    Tooltip,
    Legend,
  } from 'chart.js';
  
  Chart.register(
    LineController,
    LineElement,
    PointElement,
    LinearScale,
    CategoryScale,
    Tooltip,
    Legend
  );
  
  import { WeatherForecastResponse } from '../../models/weather-forecast-response.model';
  
  type TempUnit = 'C' | 'F';
  
  @Component({
    standalone: true,
    selector: 'app-weather-forecast-chart',
    imports: [CommonModule],
    template: `
      <div class="forecast-chart" *ngIf="forecast && forecast.points?.length">
        <canvas #canvas></canvas>
      </div>
      <p *ngIf="!forecast || !forecast.points?.length" class="muted">
        No forecast data available for chart.
      </p>
    `,
    styleUrls: ['./weather-forecast-chart.component.scss'],
  })
  export class WeatherForecastChartComponent
    implements AfterViewInit, OnChanges, OnDestroy
  {
    @Input() forecast: WeatherForecastResponse | null = null;
    @Input() unit: TempUnit = 'C'; // 'C' or 'F'
  
    @ViewChild('canvas', { static: false })
    canvasRef?: ElementRef<HTMLCanvasElement>;
  
    private chart: Chart | null = null;
  
    ngAfterViewInit(): void {
      this.buildChart();
    }
  
    ngOnChanges(changes: SimpleChanges): void {
      if (changes['forecast'] || changes['unit']) {
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
      if (!this.canvasRef) {
        return;
      }
  
      if (!this.forecast || !this.forecast.points?.length) {
        this.destroyChart();
        return;
      }
  
      const ctx = this.canvasRef.nativeElement.getContext('2d');
      if (!ctx) {
        return;
      }
  
      const points = this.forecast.points.slice(0, 24);
  
      const labels = points.map((p) => {
        const d = new Date(p.timestamp);
        return d.toLocaleTimeString(undefined, {
          hour: 'numeric',
          minute: '2-digit',
        });
      });
  
      const temps = points.map((p) =>
        this.convertTemp(p.temperature ?? null),
      );
  
      const config: ChartConfiguration<'line'> = {
        type: 'line',
        data: {
          labels,
          datasets: [
            {
              label: `Temperature (${this.unit === 'C' ? '°C' : '°F'})`,
              data: temps,
              borderColor: '#ffffff',     
              backgroundColor: '#ffffff',
              pointBackgroundColor: '#ffffff',
              pointBorderColor: '#ffffff',
              fill: false,
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
              display: true,
              labels: {
                color: '#ffffff', 
              },
            },
          },
          scales: {
            x: {
              ticks: {
                color: '#ffffff', 
                maxRotation: 0,
                autoSkip: true,
                maxTicksLimit: 8,
              },
              grid: {
                color: 'rgba(255,255,255,0.15)', 
              },
            },
            y: {
              ticks: {
                color: '#ffffff', 
              },
              title: {
                display: true,
                text: this.unit === 'C' ? '°C' : '°F',
                color: '#ffffff', 
              },
              grid: {
                color: 'rgba(255,255,255,0.15)',
              },
            },
          },
        },
      };      
  
      this.destroyChart();
      this.chart = new Chart(ctx, config);
    }
  
    private convertTemp(temp: number | null): number | null {
      if (temp === null || temp === undefined) {
        return null;
      }
      if (this.unit === 'C') {
        return temp;
      }
      return (temp * 9) / 5 + 32;
    }
  }
  