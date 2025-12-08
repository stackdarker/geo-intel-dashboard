import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  signal,
} from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { CurrencyApiService } from '../../services/currency-api.service';
import { Currency } from '../../models/currency.model';
import { ConversionResult } from '../../models/conversion-result.model';
import { CurrencyHistoryChartComponent } from '../currency-history-chart/currency-history-chart.component';

@Component({
  selector: 'app-currency-detail',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    CurrencyHistoryChartComponent,
  ],
  templateUrl: './currency-detail.component.html',
  styleUrls: ['./currency-detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CurrencyDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private api = inject(CurrencyApiService);
  private fb = inject(FormBuilder);

  readonly code = signal<string>('');

  readonly currencies = this.api.currencies;

  readonly currency = computed<Currency | undefined>(() => {
    const list = this.currencies();
    const c = this.code().toUpperCase();
    return list.find((cur) => cur.code.toUpperCase() === c);
  });

  form!: FormGroup;

  readonly converting = signal(false);
  readonly conversionError = signal<string | null>(null);
  readonly conversionResult = signal<ConversionResult | null>(null);

  readonly targetCurrencies = computed<Currency[]>(() => {
    const list = this.currencies();
    const base = this.code().toUpperCase();
    return list.filter((c) => c.code.toUpperCase() !== base);
  });

  ngOnInit(): void {
    const codeParam = (this.route.snapshot.paramMap.get('code') ?? '').toUpperCase();
    this.code.set(codeParam);

    this.api.loadCurrencies();

    this.form = this.fb.group({
      amount: [100, [Validators.required, Validators.min(0.000001)]],
      to: ['USD', [Validators.required]],
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const base = this.code().toUpperCase();
    const amount = this.form.value.amount as number;
    const to = (this.form.value.to as string).toUpperCase();

    this.converting.set(true);
    this.conversionError.set(null);
    this.conversionResult.set(null);

    this.api.convert(base, to, amount).subscribe({
      next: (result) => {
        this.conversionResult.set(result);
        this.converting.set(false);
      },
      error: (err) => {
        console.error('Conversion failed', err);
        this.conversionError.set('Conversion failed. Please try again.');
        this.converting.set(false);
      },
    });
  }
}
