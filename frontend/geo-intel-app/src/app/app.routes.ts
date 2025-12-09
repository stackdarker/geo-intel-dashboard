import { Routes } from '@angular/router';
import { DashboardHomeComponent } from './features/dashboard/dashboard-home/dashboard-home.component';
import { CountryListComponent } from './features/country/components/country-list/country-list.component';


// Define application routes
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
    {
      path: 'currencies/:code',
      loadComponent: () =>
        import(
          './features/currencies/components/currency-detail/currency-detail.component'
        ).then((m) => m.CurrencyDetailComponent),
    },
    {
      path: 'weather',
      loadComponent: () =>
        import('./features/weather/components/weather-lookup/weather-lookup.component')
          .then(m => m.WeatherLookupComponent),
    },
    { path: '**', redirectTo: 'dashboard' },
    // placeholders for future features
    // { path: 'timezones', component: TimezoneViewComponent },
    // { path: 'insights', component: InsightsViewComponent },
  ];
  