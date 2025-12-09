export interface TimeNowResponse {
    zoneId: string;
    region: string;
    city: string;
    abbreviation: string;
    offset: string;          
    localDateTime: string; 
    dst: boolean;
    epochMillis: number;
  }
  