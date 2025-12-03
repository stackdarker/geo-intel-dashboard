import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Country } from '../models/country.model';
import { forkJoin, Observable } from 'rxjs';
import { CountryIndicators } from '../models/country-indicators.model';

@Injectable({
  providedIn: 'root',
})
export class CountryService {
  private readonly http = inject(HttpClient);

  getAllCountries(): Observable<Country[]> {
    // interceptor → http://localhost:8080/api/v1/countries/all
    return this.http.get<Country[]>('/countries/all');
  }

  getCountryByCode(code: string): Observable<Country> {
    return this.http.get<Country>(`/countries/${code}`);
  }

  getCountryIndicators(code: string): Observable<CountryIndicators> {
    // -> http://localhost:8080/api/v1/countries/{code}/indicators
    return this.http.get<CountryIndicators>(`/countries/${code}/indicators`);
  }

  // method to get both country profile and indicators
  getCountryDetail(code: string): Observable<{ profile: Country; indicators: CountryIndicators }> {
    return forkJoin({
      profile: this.getCountryByCode(code),
      indicators: this.getCountryIndicators(code),
    });
  }
}
