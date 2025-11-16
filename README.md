# Mobile Weather App (Android Native - Java)

A **native Android weather application** built in **Java** that allows users to search for a city and view its current weather as well as a 3-day forecast. The app fetches data from the **OpenWeatherMap API**.

---

## Features

- Search weather by city name.  
- Displays current weather:
  - Temperature  
  - Weather description  
  - Humidity  
  - Wind speed  
  - Weather condition icon  
- Displays a **3-day forecast** with:
  - Date  
  - Min/Max temperature  
  - Short weather description  
- Handles **loading states** and **API errors** (e.g., city not found, no internet).  
- Stores the **last-searched city** locally and shows it on the next app launch.

---

## Screenshots

*(Add mobile screenshots here)*

- **Main screen** showing current weather  
- **Forecast section** showing upcoming days  
- **Error / No network screen** (if implemented)

---

## Project Structure

- `activity_main.xml` – Layout for the main screen (search bar + current weather display).  
- `item_forecast.xml` – Layout for individual forecast items.  
- `MainActivity.java` – Handles UI interactions and API calls.  
- Other Java classes – For data models, adapters, and network requests.

---

## Technologies Used

- **Language:** Java  
- **Platform:** Android Native  
- **UI:** XML layouts  
- **API:** [OpenWeatherMap](https://openweathermap.org/api) (free tier)  

---

## API Key Setup

This app requires an **OpenWeatherMap API key**:

1. Register at [OpenWeatherMap](https://openweathermap.org/api).  
2. Generate a new API key.  
3. Name your key `WeatherAppKey`.  
4. Add the key to your project (usually in `strings.xml`):

```xml
<string name="WeatherAppKey">YOUR_API_KEY_HERE</string>
