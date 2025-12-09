import { LatestRatesResponse } from '../../currencies/models/latest-rates.model';
import { PopulationInsight } from './population-insight.model';

export interface GlobalInsightsOverview {
  baseCurrency: string;
  fxMajors: LatestRatesResponse;
  topPopulation: PopulationInsight[];
  generatedAt: string;
}
