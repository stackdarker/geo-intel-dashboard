// defines the structure for filtering countries in the application

export interface CountryFilters {
    search: string;
    region: string | null;
    minPopulation: number | null;
    sortBy: 'name' | 'population';
  }
  