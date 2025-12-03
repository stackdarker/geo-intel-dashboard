import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CountryService } from '../../services/country.service';
import { Country } from '../../models/country.model';
import { CountryIndicators } from '../../models/country-indicators.model';

@Component({
  selector: 'app-country-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './country-detail.component.html',
  styleUrls: ['./country-detail.component.scss'],
})
export class CountryDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly countryService = inject(CountryService);

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly profile = signal<Country | null>(null);
  readonly indicators = signal<CountryIndicators | null>(null);

  readonly title = computed(() => {
    const p = this.profile();
    return p ? `${p.name} (${p.code})` : 'Country Details';
  });

  ngOnInit(): void {
    const code = this.route.snapshot.paramMap.get('code');
    if (!code) {
      this.error.set('No country code provided.');
      this.isLoading.set(false);
      return;
    }

    this.isLoading.set(true);
    this.error.set(null);

    this.countryService.getCountryDetail(code).subscribe({
      next: ({ profile, indicators }) => {
        this.profile.set(profile);
        this.indicators.set(indicators);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Failed to load country detail', err);
        this.error.set('Failed to load country details.');
        this.isLoading.set(false);
      },
    });
  }
}
