// Models for country indicators data structure

import { IndicatorValue } from './indicator-value.model';

export interface CountryIndicators {
  countryCode: string;
  gdp: IndicatorValue;
  population: IndicatorValue;
  lifeExpectancy: IndicatorValue;
}
