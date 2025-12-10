import { WatchlistCountryInsight } from './watchlist-country-insight.model';

export interface WatchlistInsightsResponse {
  baseCurrency: string;
  items: WatchlistCountryInsight[];
  generatedAt: string; 
}
