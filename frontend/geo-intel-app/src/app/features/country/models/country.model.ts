export interface Country {
    code: string;       
    name: string;
    region?: string;
    capital?: string;
    population?: number;
    currencies?: string[];
    timezones?: string[];
  }
  