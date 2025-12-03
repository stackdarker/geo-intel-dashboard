import { Routes } from '@angular/router';
import { DashboardHomeComponent } from './features/dashboard/dashboard-home/dashboard-home.component';
import { CountryListComponent } from './features/country/components/country-list/country-list.component';


// Define application routes
// Future routes for additional features are commented out for now
export const routes: Routes = [
    { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
    { path: 'dashboard', component: DashboardHomeComponent },
    { path: 'countries', component: CountryListComponent },
    {
      path: 'countries/:code',
      loadComponent: () =>
        import('./features/country/components/country-detail/country-detail.component')
          .then(m => m.CountryDetailComponent),
    },    
    { path: '**', redirectTo: 'dashboard' },
    // placeholders for future features
    // { path: 'currencies', component: CurrencyListComponent },
    // { path: 'weather', component: WeatherViewComponent },
    // { path: 'timezones', component: TimezoneViewComponent },
    // { path: 'insights', component: InsightsViewComponent },
  ];
  