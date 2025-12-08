import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { CurrencyDashboardWidgetComponent } from '../../../features/currencies/components/currency-dashboard-widget/currency-dashboard-widget.component';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [CommonModule, CurrencyDashboardWidgetComponent],
  templateUrl: './dashboard-home.component.html',
  styleUrls: ['./dashboard-home.component.scss'],
})
export class DashboardHomeComponent {}
