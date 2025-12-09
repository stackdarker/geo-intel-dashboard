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
  import { PopulationInsight } from '../../models/population-insight.model';
  import {
    Chart,
    ChartConfiguration,
    BarController,
    BarElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
  } from 'chart.js';
  
  Chart.register(
    BarController,
    BarElement,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend
  );
  
  @Component({
    standalone: true,
    selector: 'app-global-population-chart',
    imports: [CommonModule],
    template: `
      <div class="population-chart" *ngIf="data && data.length">
        <canvas #canvas></canvas>
      </div>
      <p class="muted" *ngIf="!data || !data.length">
        No population data available.
      </p>
    `,
    styleUrls: ['./global-population-chart.component.scss'],
  })
  export class GlobalPopulationChartComponent
    implements AfterViewInit, OnChanges, OnDestroy
  {
    @Input() data: PopulationInsight[] | null = null;
  
    @ViewChild('canvas', { static: false }) canvasRef?: ElementRef<HTMLCanvasElement>;
  
    private chart: Chart | null = null;
  
    ngAfterViewInit(): void {
      this.buildChart();
    }
  
    ngOnChanges(changes: SimpleChanges): void {
      if (changes['data']) {
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
  
      const list = this.data ?? [];
      if (!list.length) {
        this.destroyChart();
        return;
      }
  
      const ctx = this.canvasRef.nativeElement.getContext('2d');
      if (!ctx) return;
  
      const labels = list.map((p) => p.name);
      const values = list.map((p) => p.population);
  
      const config: ChartConfiguration<'bar'> = {
        type: 'bar',
        data: {
          labels,
          datasets: [
            {
              label: 'Population',
              data: values,
              borderColor: '#ffffff',
              backgroundColor: 'rgba(154, 180, 255, 0.8)',
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
          },
          scales: {
            x: {
              ticks: {
                color: '#ffffff',
              },
              grid: {
                color: 'rgba(255,255,255,0.15)',
              },
            },
            y: {
              ticks: {
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
  }
  