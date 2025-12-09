import { ForecastPoint } from './forecast-point.model';

export interface WeatherForecastResponse {
  city: string;
  country: string | null;
  points: ForecastPoint[];
}
