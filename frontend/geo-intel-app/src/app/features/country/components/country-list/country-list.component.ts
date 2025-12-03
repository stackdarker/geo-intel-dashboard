import { Component, OnInit, inject, signal } from '@angular/core';
import { CountryService } from '../../services/country.service';
import { Country } from '../../models/country.model';
import { NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'app-country-list',
  standalone: true,
  imports: [NgForOf, NgIf],
  templateUrl: './country-list.component.html',
  styleUrls: ['./country-list.component.scss'],
})
export class CountryListComponent implements OnInit {
  private readonly countryService = inject(CountryService);

  countries = signal<Country[]>([]);
  isLoading = signal(false);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadCountries();
  }

  private loadCountries(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.countryService.getAllCountries().subscribe({
      next: (data) => {
        this.countries.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Failed to load countries', err);
        this.error.set('Failed to load countries');
        this.isLoading.set(false);
      },
    });
  }
}
