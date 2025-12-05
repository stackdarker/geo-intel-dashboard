import {
  Component,
  OnInit,
  OnDestroy,
  inject,
  signal,
  computed,
} from '@angular/core';
import { NgForOf, NgIf, DecimalPipe } from '@angular/common';
import { RouterLink, ActivatedRoute, Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';

import { CountryService } from '../../services/country.service';
import { Country } from '../../models/country.model';
import { CountryFilters } from '../../models/country-filters.model';

import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-country-list',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    RouterLink,
    ReactiveFormsModule,
    DecimalPipe,
  ],
  templateUrl: './country-list.component.html',
  styleUrls: ['./country-list.component.scss'],
})
export class CountryListComponent implements OnInit, OnDestroy {
  private readonly countryService = inject(CountryService);
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  // raw data
  countries = signal<Country[]>([]);
  isLoading = signal(false);
  error = signal<string | null>(null);

  // selection for compare
  selectedCodes = signal<string[]>([]);

  // filter form + state
  filterForm!: FormGroup;
  filters = signal<CountryFilters>({
    search: '',
    region: null,
    minPopulation: null,
    sortBy: 'name',
  });

  // derived state
  filteredCountries = computed<Country[]>(() =>
    this.applyFilters(this.countries(), this.filters())
  );
  totalCount = computed(() => this.countries().length);

  private filterSub?: Subscription;

  readonly regions: string[] = [
    'Africa',
    'Americas',
    'Asia',
    'Europe',
    'Oceania',
  ];

  ngOnInit(): void {
    this.initFiltersFromQueryParams();
    this.loadCountries();
    this.setupFilterFormSubscription();
  }

  ngOnDestroy(): void {
    this.filterSub?.unsubscribe();
  }

  // data
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

  // filters/query params
  private initFiltersFromQueryParams(): void {
    const qp = this.route.snapshot.queryParamMap;

    const initial: CountryFilters = {
      search: qp.get('search') ?? '',
      region: qp.get('region'),
      minPopulation: qp.get('minPopulation')
        ? Number(qp.get('minPopulation'))
        : null,
      sortBy: (qp.get('sortBy') as 'name' | 'population') || 'name',
    };

    this.filterForm = this.fb.group({
      search: [initial.search],
      region: [initial.region],
      minPopulation: [initial.minPopulation],
      sortBy: [initial.sortBy],
    });

    this.filters.set(initial);
  }

  private setupFilterFormSubscription(): void {
    this.filterSub = this.filterForm.valueChanges
      .pipe(
        debounceTime(250),
        distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b))
      )
      .subscribe((raw) => {
        const normalized: CountryFilters = {
          search: raw['search'] ?? '',
          region: raw['region'] ?? null,
          minPopulation:
            raw['minPopulation'] !== null && raw['minPopulation'] !== ''
              ? Number(raw['minPopulation'])
              : null,
          sortBy: (raw['sortBy'] as 'name' | 'population') || 'name',
        };

        this.filters.set(normalized);
        this.updateQueryParams(normalized);
      });
  }

  private updateQueryParams(filters: CountryFilters): void {
    const queryParams: any = {
      search: filters.search || null,
      region: filters.region || null,
      minPopulation: filters.minPopulation || null,
      sortBy: filters.sortBy || null,
    };

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
      queryParamsHandling: 'merge',
    });
  }

  clearFilters(): void {
    const reset: CountryFilters = {
      search: '',
      region: null,
      minPopulation: null,
      sortBy: 'name',
    };

    this.filterForm.reset(reset);
    this.filters.set(reset);
    this.updateQueryParams(reset);
  }

  // filtering
  private applyFilters(
    countries: Country[],
    filters: CountryFilters
  ): Country[] {
    let result = [...countries];

    const search = (filters.search || '').toLowerCase().trim();
    if (search) {
      result = result.filter((c) => {
        const name = c.name ?? '';
        const code = c.code ?? '';
        const capital = c.capital ?? '';

        return (
          name.toLowerCase().includes(search) ||
          code.toLowerCase().includes(search) ||
          capital.toLowerCase().includes(search)
        );
      });
    }

    if (filters.region) {
      result = result.filter((c) => c.region === filters.region);
    }

    if (filters.minPopulation != null && filters.minPopulation > 0) {
      result = result.filter(
        (c) => (c.population ?? 0) >= filters.minPopulation!
      );
    }

    if (filters.sortBy === 'population') {
      result = result.sort(
        (a, b) => (b.population ?? 0) - (a.population ?? 0)
      );
    } else {
      // default: sort by name asc
      result = result.sort((a, b) =>
        (a.name || '').localeCompare(b.name || '')
      );
    }

    return result;
  }

  // selection for compare
  onSelectionChange(code: string, event: Event): void {
    const input = event.target as HTMLInputElement | null;
    const checked = !!input?.checked;
    this.toggleSelection(code, checked);
  }

  toggleSelection(code: string, checked: boolean): void {
    const current = this.selectedCodes();
    const exists = current.includes(code);

    let next = current;

    if (checked && !exists) {
      next = [...current, code];
    } else if (!checked && exists) {
      next = current.filter((c) => c !== code);
    }

    this.selectedCodes.set(next);
  }

  isSelected(code: string): boolean {
    return this.selectedCodes().includes(code);
  }

  goToCompare(): void {
    const codes = this.selectedCodes();
    if (codes.length < 2) return;

    this.router.navigate(['/countries/compare'], {
      queryParams: { codes: codes.join(',') },
    });
  }
}
