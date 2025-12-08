import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  signal,
} from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { CountryService } from '../../../services/country.service';
import { Country } from '../../../models/country.model';

const COUNTRY_REGION_PREF_KEY = 'geoDashboard.countries.regionFilter';

@Component({
  selector: 'app-country-dashboard-widget',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './country-dashboard-widget.component.html',
  styleUrls: ['./country-dashboard-widget.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CountryDashboardWidgetComponent implements OnInit {
  private countryService = inject(CountryService);

  private readonly _countries = signal<Country[]>([]);
  private readonly _loading = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly countries = computed(() => this._countries());
  readonly loading = computed(() => this._loading());
  readonly error = computed(() => this._error());

  private readonly _region = signal<string>('ALL');
  readonly region = computed(() => this._region());

  get regionModel(): string {
    return this._region();
  }
  set regionModel(value: string) {
    this.onRegionChange(value);
  }

  readonly regions = computed(() => {
    const list = this.countries();
    const set = new Set<string>();

    for (const c of list) {
      if (c.region) {
        set.add(c.region);
      }
    }

    return Array.from(set).sort((a, b) => a.localeCompare(b));
  });

  readonly topCountries = computed<Country[]>(() => {
    const list = this.countries();
    if (!list || list.length === 0) {
      return [];
    }

    const region = this._region();
    let filtered = list;

    if (region !== 'ALL') {
      filtered = filtered.filter((c) => c.region === region);
    }

    return filtered
      .filter((c) => typeof c.population === 'number')
      .sort((a, b) => (b.population ?? 0) - (a.population ?? 0))
      .slice(0, 5);
  });

  readonly totalCountriesInRegion = computed<number>(() => {
    const list = this.countries();
    const region = this._region();

    if (!list || list.length === 0) {
      return 0;
    }

    if (region === 'ALL') {
      return list.length;
    }

    return list.filter((c) => c.region === region).length;
  });

  readonly totalPopulation = computed<number>(() => {
    const list = this.countries();
    const region = this._region();

    if (!list || list.length === 0) {
      return 0;
    }

    const filtered =
      region === 'ALL' ? list : list.filter((c) => c.region === region);

    return filtered.reduce((sum, c) => sum + (c.population ?? 0), 0);
  });

  ngOnInit(): void {
    this.loadRegionPreference();
    this.loadCountries();
  }

  private loadCountries(): void {
    this._loading.set(true);
    this._error.set(null);

    this.countryService.getAllCountries().subscribe({
      next: (countries: Country[]) => {
        this._countries.set(countries ?? []);
        this._loading.set(false);
      },
      error: (err: unknown) => {
        console.error('Failed to load countries for dashboard widget', err);
        this._error.set('Unable to load country snapshot.');
        this._loading.set(false);
      },
    });
  }

  private loadRegionPreference(): void {
    if (typeof localStorage === 'undefined') {
      return;
    }

    try {
      const stored = localStorage.getItem(COUNTRY_REGION_PREF_KEY);
      if (!stored) return;

      this._region.set(stored);
    } catch {
    }
  }

  private saveRegionPreference(region: string): void {
    if (typeof localStorage === 'undefined') {
      return;
    }

    try {
      localStorage.setItem(COUNTRY_REGION_PREF_KEY, region);
    } catch {
    }
  }

  onRegionChange(newRegion: string): void {
    const normalized = newRegion || 'ALL';
    if (normalized === this._region()) {
      return;
    }

    this._region.set(normalized);
    this.saveRegionPreference(normalized);
  }
}
