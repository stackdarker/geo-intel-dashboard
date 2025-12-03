import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Country } from '../models/country.model';
import { Observable } from 'rxjs';

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
}
