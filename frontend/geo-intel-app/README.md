🌐 Global Insights Dashboard (Geo-Intel)

A full-stack analytics dashboard that unifies **global financial**, **geographic**, **weather**, and **timezone** data into a single interactive platform.  
Built with **Angular** and **Spring Boot**.

---

## Features

### Currency Analytics
- Live FX rates from Frankfurter / exchangerate.host  
- Currency conversion (any → any)  
- Historical FX charts (7/30/90 days)  
- Snapshot widget for major currencies  
- User preference for default base currency  

### Country Intelligence
- Searchable, filterable country list  
- Country detail pages (flag, capital, population, region, etc.)  
- World Bank indicators (GDP, population, life expectancy)  
- Comparison mode (multi-country side-by-side metrics)  
- Dashboard widget for “Top Countries” (population-based with region preference)  

### Weather Insights
- City + country-aware lookup (e.g., Bedford, US vs Bedford, GB)  
- Current conditions: temperature, humidity, wind  
- 24-hour forecast  
- Weather trend chart with **°C/°F toggle**  
- Caching for faster repeated lookups  

### Timezones & World Clock
- Full searchable timezone list  
- Favorite timezones stored in local preferences  
- Real-time world clock using `java.time`  
- Dashboard widget showing your selected cities  

### Insights Module (Cross-API Intelligence)
- Global overview combining FX + population  
- Country-level insights (country → indicators + weather + FX + local time)  
- Watchlist with correlation chart (FX vs population)  
- Extensible design for future multi-axis analytics  

---

## Architecture Overview

### Backend — Spring Boot (Java 17)

**Backend Capabilities**
- Central `RestTemplate` with timeouts  
- Caching via `@EnableCaching` (Redis or in-memory)  
- Unified exception structure (`ExternalApiException`)  
- Individual API clients for:
  - Frankfurter / exchangerate.host  
  - RestCountries  
  - World Bank  
  - OpenWeather-style provider  
- Insight aggregation layer combining multiple services  

---

## Frontend — Angular 17

**Frontend Highlights**
- Standalone Angular architecture (no NgRx required)
- Signals for reactive state  
- Dark & light (COMING SOON) themes using CSS variables  
- Responsive grid dashboard layout  
- Chart.js for visualizations:
  - FX historical data  
  - Weather trends  
  - Insights correlation charts  

**Preferences stored in localStorage**
- Base FX currency  
- Region filter  
- Temperature unit  
- Favorite timezones  
- Theme mode (light (COMING SOON) /dark)  

**Shared UI features**
- Reusable `<card>` layout  
- Skeleton loaders & error states  
- Global API interceptor mapping `/currency`, `/countries`, `/weather`, `/time`, `/insights`  
