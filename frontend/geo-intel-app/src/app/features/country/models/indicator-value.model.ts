// model representing the value of an indicator for a specific country and year

export interface IndicatorValue {
    code: string;
    name: string;
    value: number;
    year: number;
  }
  