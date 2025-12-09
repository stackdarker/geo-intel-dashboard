import {
  Component,
  OnInit,
  inject,
  signal,
  computed,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TimezoneApiService } from '../services/timezone-api.service';
import { TimeZoneInfo } from '../models/time-zone-info.model';

@Component({
  standalone: true,
  selector: 'app-timezone-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './timezone-list.component.html',
  styleUrls: ['./timezone-list.component.scss'],
})
export class TimezoneListComponent implements OnInit {
  private timezoneApi = inject(TimezoneApiService);

  zones = this.timezoneApi.zones;
  worldClock = this.timezoneApi.worldClock;
  loading = this.timezoneApi.loading;
  error = this.timezoneApi.error;

  private readonly favoritesKey = 'geoDashboard.timezones.favorites';

  // selected favorites (zone IDs)
  favorites = signal<string[]>([] as string[]);

  // filter text for the zone list
  filterText = signal('');

  // derived: filtered zones list
  filteredZones = computed(() => {
    const term = this.filterText().toLowerCase();
    const all = this.zones();
    if (!term) {
      return all;
    }
    return all.filter((z) =>
      (z.zoneId + ' ' + z.region + ' ' + z.city).toLowerCase().includes(term)
    );
  });

  ngOnInit(): void {
    this.timezoneApi.loadZones();
    this.restoreFavorites();
    this.refreshWorldClock();
  }

  private restoreFavorites(): void {
    const raw = localStorage.getItem(this.favoritesKey);
    if (!raw) {
      // sensible defaults
      this.favorites.set([
        'America/New_York',
        'Europe/London',
        'Asia/Tokyo',
      ]);
      return;
    }
    try {
      const parsed = JSON.parse(raw);
      if (Array.isArray(parsed)) {
        this.favorites.set(parsed);
      }
    } catch {
      // ignore
    }
  }

  private saveFavorites(): void {
    localStorage.setItem(this.favoritesKey, JSON.stringify(this.favorites()));
  }

  toggleFavorite(zoneId: string): void {
    const current = this.favorites();
    if (current.includes(zoneId)) {
      this.favorites.set(current.filter((z) => z !== zoneId));
    } else {
      this.favorites.set([...current, zoneId]);
    }
    this.saveFavorites();
    this.refreshWorldClock();
  }

  isFavorite(zoneId: string): boolean {
    return this.favorites().includes(zoneId);
  }

  refreshWorldClock(): void {
    this.timezoneApi.loadWorldClock(this.favorites());
  }

  onFilterChange(value: string): void {
    this.filterText.set(value);
  }

  displayZoneLabel(z: TimeZoneInfo): string {
    return z.city ? `${z.region}/${z.city}` : z.zoneId;
  }
}
