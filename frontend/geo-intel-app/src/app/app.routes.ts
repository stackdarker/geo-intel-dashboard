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
      path: 'countries/compare',
      loadComponent: () =>
        import('./features/country/components/country-compare/country-compare.component')
          .then(m => m.CountryCompareComponent),
    },
    {
      path: 'countries/:code',
      loadComponent: () =>
        import('./features/country/components/country-detail/country-detail.component')
          .then(m => m.CountryDetailComponent),
    },    

    {
      path: 'currencies',
      loadComponent: () =>
        import(
          './features/currencies/components/currency-list/currency-list.component'
        ).then((m) => m.CurrencyListComponent),
    },
    { path: '**', redirectTo: 'dashboard' },
    // placeholders for future features
    // { path: 'weather', component: WeatherViewComponent },
    // { path: 'timezones', component: TimezoneViewComponent },
    // { path: 'insights', component: InsightsViewComponent },
  ];
  