import {
    Component,
    Input,
    OnChanges,
    SimpleChanges,
    inject,
  } from '@angular/core';
  import { CommonModule } from '@angular/common';
  import { InsightsApiService } from '../../services/insights-api.service';
  
  @Component({
    standalone: true,
    selector: 'app-country-insights-panel',
    imports: [CommonModule],
    templateUrl: './country-insights-panel.component.html',
    styleUrls: ['./country-insights-panel.component.scss'],
  })
  export class CountryInsightsPanelComponent implements OnChanges {
    @Input() countryCode!: string;
  
    private insightsApi = inject(InsightsApiService);
  
    insights = this.insightsApi.countryInsights;
    loading = this.insightsApi.loading;
    error = this.insightsApi.error;
  
    ngOnChanges(changes: SimpleChanges): void {
      if (changes['countryCode'] && this.countryCode) {
        this.insightsApi.loadCountryInsights(this.countryCode);
      }
    }
  }
  