import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CurrencyDashboardWidgetComponent } from '../../../features/currencies/components/currency-dashboard-widget/currency-dashboard-widget.component';
import { CountryDashboardWidgetComponent } from '../../../features/country/components/country-list/country-dashboard-widget/country-dashboard-widget.component';
import { WeatherDashboardWidgetComponent } from '../../../features/weather/components/weather-dashboard-widget/weather-dashboard-widget.component';


@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [CommonModule, CurrencyDashboardWidgetComponent, CountryDashboardWidgetComponent, WeatherDashboardWidgetComponent],
  templateUrl: './dashboard-home.component.html',
  styleUrls: ['./dashboard-home.component.scss'],
})
export class DashboardHomeComponent {}

