import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnInit,
  computed,
  inject,
  signal,
} from '@angular/core';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { startWith } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { CurrencyApiService } from '../../services/currency-api.service';
import { Currency } from '../../models/currency.model';
import { Router } from '@angular/router';


@Component({
  selector: 'app-currency-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurrencyListComponent implements OnInit {
  private api = inject(CurrencyApiService);
  private destroyRef = inject(DestroyRef);

  // search input
  searchControl = new FormControl<string>('', { nonNullable: true });

  // state from service
  readonly currencies = this.api.currencies;
  readonly loading = this.api.loading;
  readonly error = this.api.error;


  private readonly searchTerm = signal<string>('');

  // derived, filtered list
  readonly filteredCurrencies = computed<Currency[]>(() => {
    const term = this.searchTerm().toLowerCase().trim();
    const list = this.currencies();

    if (!term) {
      return list;
    }

    return list.filter(
      (c) =>
        c.code.toLowerCase().includes(term) ||
        c.description.toLowerCase().includes(term)
    );
  });

  constructor() {
    this.searchControl.valueChanges
      .pipe(
        startWith(this.searchControl.value),     
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((value) => {
        this.searchTerm.set(value ?? '');
      });
  }

  ngOnInit(): void {
    this.api.loadCurrencies();
  }

  onRetry(): void {
    this.api.loadCurrencies(true);
  }

  trackByCode(_index: number, item: Currency): string {
    return item.code;
  }

  private router = inject(Router);

  goToDetail(code: string): void {
    this.router.navigate(['/currencies', code]);
  }
}
