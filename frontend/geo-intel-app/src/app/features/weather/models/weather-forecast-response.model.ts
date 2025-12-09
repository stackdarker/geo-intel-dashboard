export interface ForecastPoint {
  timestamp: string;          
  temperature: number | null; 
  description?: string | null;
  icon?: string | null;
}

export interface WeatherForecastResponse {
  city: string;
  country?: string | null;
  points: ForecastPoint[];
}
