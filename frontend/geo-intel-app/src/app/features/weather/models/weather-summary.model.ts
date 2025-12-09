export interface WeatherSummary {
    city: string;
    country: string | null;
    temperature: number | null;
    feelsLike: number | null;
    humidity: number | null;
    windSpeed: number | null;
    description: string | null;
    iconCode: string | null;
    timestamp: string; 
  }
  