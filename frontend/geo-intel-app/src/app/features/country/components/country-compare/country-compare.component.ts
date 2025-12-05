import {
    Component,
    OnInit,
    inject,
    signal,
    computed,
  } from '@angular/core';
  import { CommonModule, NgForOf, NgIf, DecimalPipe } from '@angular/common';
  import { ActivatedRoute, RouterLink } from '@angular/router';
  
  import { CountryService } from '../../services/country.service';
  import { Country } from '../../models/country.model';
  
  import { forkJoin } from 'rxjs';
  
  @Component({
    selector: 'app-country-compare',
    standalone: true,
    imports: [CommonModule, NgForOf, NgIf, RouterLink, DecimalPipe],
    templateUrl: './country-compare.component.html',
    styleUrls: ['./country-compare.component.scss'],
  })
  export class CountryCompareComponent implements OnInit {
    private readonly route = inject(ActivatedRoute);
    private readonly countryService = inject(CountryService);
  
    isLoading = signal(false);
    error = signal<string | null>(null);
  
    codes = signal<string[]>([]);
    countries = signal<Country[]>([]);
  
    hasEnough = computed(() => this.countries().length >= 2);
  
    ngOnInit(): void {
      const qp = this.route.snapshot.queryParamMap;
      const raw = qp.get('codes') ?? '';
  
      const codes = raw
        .split(',')
        .map((c) => c.trim().toUpperCase())
        .filter((c) => !!c);
  
      this.codes.set(codes);
  
      if (codes.length < 2) {
        this.error.set('Select at least two countries to compare.');
        return;
      }
  
      this.loadCountries(codes);
    }
  
    private loadCountries(codes: string[]): void {
      this.isLoading.set(true);
      this.error.set(null);
  
      const requests = codes.map((code) =>
        this.countryService.getCountryByCode(code)
      );
  
      forkJoin(requests).subscribe({
        next: (results) => {
          this.countries.set(results);
          this.isLoading.set(false);
        },
        error: (err) => {
          console.error('Failed to load countries for comparison', err);
          this.error.set('Failed to load comparison data.');
          this.isLoading.set(false);
        },
      });
    }
  }
  