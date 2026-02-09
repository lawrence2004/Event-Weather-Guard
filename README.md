🌦️ Event Weather Guard – Backend Service
Overview

Event Weather Guard is a stateless backend service that evaluates weather conditions for outdoor events and determines whether the event is Safe, Risky, or Unsafe to proceed.

The service:

Accepts event details (location and time window)

Fetches hourly weather forecasts from a public weather API

Applies deterministic and explainable rules

Returns a structured advisory with reasons and supporting forecast data

The project focuses on correct time-window handling, explainability, edge-case handling, and clean API design.

Tech Stack

Java 17

Spring Boot

REST API

Open-Meteo Weather API (public, no API key required)

Maven

Swagger / OpenAPI (optional documentation)

Project Structure
src/main/java/com/aspora/weather
├── controller
├── service
├── util
├── dto
├── exception
├── config
└── WeatherApplication.java

Setup & Execution
Prerequisites

Java 17+

Git

Internet connection (for weather API)

Clone the Repository
git clone <your-github-repo-url>
cd Event-Weather-Guard-main

Run the Application
✅ Windows (PowerShell)
.\mvnw.cmd spring-boot:run

macOS / Linux
./mvnw spring-boot:run

Alternative (if Maven is installed globally)
mvn spring-boot:run

Successful Startup Log
Tomcat started on port 8080
Started WeatherApplication in X seconds

API Usage
Endpoint
POST /event-forecast

Request Body
{
  "name": "Football Match",
  "location": {
    "latitude": 19.0760,
    "longitude": 72.8777
  },
  "startTime": "2026-02-09T17:00:00",
  "endTime": "2026-02-09T19:00:00"
}

Sample Response
{
  "classification": "Risky",
  "severityScore": 62,
  "summary": "High chance of rain during multiple hours of the event",
  "reason": [
    "Thunderstorm expected from 16:00 to 18:00",
    "High rain probability from 16:00 to 19:00",
    "Strong wind (~31 km/h) from 16:00 to 19:00"
  ],
  "eventWindowForecast": [
    { "time": "16:00", "rainProb": 4, "windKmh": 31.1 },
    { "time": "17:00", "rainProb": 4, "windKmh": 32.0 },
    { "time": "18:00", "rainProb": 3, "windKmh": 30.6 },
    { "time": "19:00", "rainProb": 3, "windKmh": 27.4 }
  ]
}

Weather Classification Rules
❌ Unsafe

An event is classified as Unsafe if any hour during the event window has:

Thunderstorm forecast (weatherCode ≥ 95)

Rain probability ≥ 80%

Wind speed ≥ 40 km/h

⚠️ Risky

An event is classified as Risky if any hour has:

Rain probability between 60% – 79%

Wind speed between 25 – 39 km/h

✅ Safe

No risky or unsafe conditions detected during the event window.

Severity Score (0–100)

In addition to categorical classification, a numeric severity score is calculated.

Scoring Model
Factor	Max Score
Rain probability	40
Wind speed	30
Thunderstorm presence	30
Total	100

The score reflects the worst expected conditions during the event window.

It complements the Safe / Risky / Unsafe classification.

Explainability Strategy

To improve clarity and avoid noisy responses:

Similar conditions across consecutive hours are grouped into time ranges

Large changes in severity are reported separately

Example:

Thunderstorm expected from 16:00 to 19:00

Edge Case Handling

The service explicitly handles:

Past event dates
→ Rejected (forecast data is future-only)

Invalid time ranges
→ End time earlier than start time

Forecast range limitation
→ Forecast supported only for the next 7 days

Unavailable forecast data
→ Clear error response returned

All errors are returned as structured JSON using a global exception handler.

Key Assumptions & Trade-offs

Event times are assumed to be in the local timezone of the event location

Weather data is fetched in real time; no persistence is used

The service is stateless by design

Severity score is heuristic-based, not a meteorological prediction

Forecast availability is limited to 7 days

API Documentation (Optional)

Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

Conclusion

This project demonstrates:

Correct handling of time-window weather forecasts

Deterministic and explainable decision-making

Clean REST API design

Robust edge-case handling

Professional documentation
