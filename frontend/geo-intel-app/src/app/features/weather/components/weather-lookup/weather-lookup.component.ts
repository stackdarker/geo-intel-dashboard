// src/app/features/weather/components/weather-lookup/weather-lookup.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { WeatherApiService } from '../../services/weather-api.service';

interface CountryOption {
  code: string;
  name: string;
}

type TempUnit = 'C' | 'F';

@Component({
  standalone: true,
  selector: 'app-weather-lookup',
  imports: [CommonModule, FormsModule],
  templateUrl: './weather-lookup.component.html',
  styleUrls: ['./weather-lookup.component.scss'],
})
export class WeatherLookupComponent implements OnInit {
  private http = inject(HttpClient);
  private weatherApi = inject(WeatherApiService);

  cityModel = '';
  countryCodeModel = '';

  countries = signal<CountryOption[]>([]);
  countriesLoading = signal(false);
  countriesError = signal<string | null>(null);

  current = this.weatherApi.current;
  forecast = this.weatherApi.forecast;
  loading = this.weatherApi.loading;
  error = this.weatherApi.error;

  private readonly lastCityKey = 'geoDashboard.weather.lastCity';
  private readonly lastCountryKey = 'geoDashboard.weather.lastCountryCode';
  private readonly tempUnitKey = 'geoDashboard.weather.tempUnit';

  tempUnit: TempUnit = this.loadInitialUnit();


  ngOnInit(): void {
    this.loadCountries();
    this.restoreLastSearch();
  }

  private loadInitialUnit(): TempUnit {
    const stored = localStorage.getItem(this.tempUnitKey);
    return stored === 'F' ? 'F' : 'C';
  }

  setTempUnit(unit: TempUnit): void {
    this.tempUnit = unit;
    localStorage.setItem(this.tempUnitKey, unit);
  }

  convertTemp(temp: number | null | undefined): number | null {
    if (temp === null || temp === undefined) {
      return null;
    }
    if (this.tempUnit === 'C') {
      return temp;
    }
    const c = temp;
    const f = (c * 9) / 5 + 32;
    return f;
  }

  getTempUnitLabel(): string {
    return this.tempUnit === 'C' ? '°C' : '°F';
  }

  private loadCountries(): void {
    this.countriesLoading.set(true);
    this.countriesError.set(null);


    this.http.get<any[]>('/countries/all').subscribe({
      next: (data) => {
        const options: CountryOption[] = (data ?? [])
          .map((c) => ({
            code: c.code, 
            name: c.name,
          }))
          .filter((c) => !!c.code && !!c.name)
          .sort((a, b) => a.name.localeCompare(b.name));

        this.countries.set(options);
        this.countriesLoading.set(false);
      },
      error: (err) => {
        console.error('Failed to load countries for weather lookup', err);
        this.countriesError.set('Unable to load country list.');
        this.countriesLoading.set(false);
      },
    });
  }

  private restoreLastSearch(): void {
    const lastCity = localStorage.getItem(this.lastCityKey);
    const lastCountry = localStorage.getItem(this.lastCountryKey);

    if (lastCity) {
      this.cityModel = lastCity;
    }
    if (lastCountry) {
      this.countryCodeModel = lastCountry;
    }

    if (lastCity) {
      this.onSearch(false);
    }
  }

  onSearch(save = true): void {
    const city = this.cityModel?.trim();
    const countryCode = this.countryCodeModel?.trim() || undefined;

    if (!city) {
      return;
    }

    if (save) {
      localStorage.setItem(this.lastCityKey, city);
      if (countryCode) {
        localStorage.setItem(this.lastCountryKey, countryCode);
      } else {
        localStorage.removeItem(this.lastCountryKey);
      }
    }

    this.weatherApi.loadWeather(city, countryCode);
  }
}
