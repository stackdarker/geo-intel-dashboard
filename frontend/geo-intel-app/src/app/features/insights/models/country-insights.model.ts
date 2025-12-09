import { Country } from '../../country/models/country.model';
import { CountryIndicators } from '../../country/models/country-indicators.model';
import { LatestRatesResponse } from '../../currencies/models/latest-rates.model';
import { WeatherSummary } from '../../weather/models/weather-summary.model';
import { TimeNowResponse } from '../../timezone/models/time-now-response.model';

export interface CountryInsights {
  country: Country;
  indicators: CountryIndicators;
  currencyRates: LatestRatesResponse;
  weather: WeatherSummary | null;
  localTime: TimeNowResponse | null;
}
