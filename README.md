🌦️ Event Weather Guard

Event Weather Guard is a Spring Boot backend service that analyzes hourly weather forecasts for outdoor events and determines whether an event is Safe, Risky, or Unsafe using deterministic and explainable rules.

This project was built as a Backend Intern take-home assignment, with emphasis on:

Clean API design

External API integration

Validation & edge-case handling

Clear business logic and explainability

🚀 Features

REST API to evaluate weather risk for outdoor events

Integration with Open-Meteo public weather API (hourly forecasts)

Deterministic classification rules (Safe / Risky / Unsafe)

Numeric severity score (0–100) for risk quantification

Clear, human-readable explanations in responses

Input validation with structured error responses

Layered architecture (Controller → Service → Client → Rule Engine)

Optional Swagger / OpenAPI documentation

No database or authentication (as per assignment requirements)

🛠️ Tech Stack

Java 17

Spring Boot 3.x

Spring MVC

RestTemplate (blocking external API calls)

Jakarta Bean Validation

Lombok

Springdoc OpenAPI (Swagger UI)

📦 API Endpoint
POST /event-forecast


Evaluates weather conditions during the given event time window and returns a weather advisory.

📤 API Usage Examples
Example 1: Safe Event
Request
{
  "name": "Morning Yoga",
  "location": {
    "latitude": 19.0760,
    "longitude": 72.8777
  },
  "startTime": "2026-02-05T06:00:00",
  "endTime": "2026-02-05T08:00:00"
}

Response
{
  "classification": "Safe",
  "severityScore": 6,
  "summary": "Weather conditions are suitable throughout the event duration",
  "reason": [
    "No significant weather risks detected"
  ],
  "eventWindowForecast": [
    { "time": "06:00", "rainProb": 0, "windKmh": 4.5 },
    { "time": "07:00", "rainProb": 0, "windKmh": 1.5 },
    { "time": "08:00", "rainProb": 0, "windKmh": 8.0 }
  ]
}

Example 2: Risky Event
Request
{
  "name": "Football Match",
  "location": {
    "latitude": 52.52,
    "longitude": 13.41
  },
  "startTime": "2026-02-05T17:00:00",
  "endTime": "2026-02-05T19:00:00"
}

Response
{
  "classification": "Risky",
  "severityScore": 46,
  "summary": "High chance of rain during multiple hours of the event",
  "reason": [
    "High rain probability from 17:00 to 19:00"
  ],
  "eventWindowForecast": [
    { "time": "17:00", "rainProb": 53, "windKmh": 10.8 },
    { "time": "18:00", "rainProb": 38, "windKmh": 10.4 },
    { "time": "19:00", "rainProb": 63, "windKmh": 10.8 }
  ]
}

⚖️ Weather Classification Rules

The system applies deterministic rules on hourly forecast data.

❌ Unsafe

An event is classified as Unsafe if any hour during the event window has:

Rain probability ≥ 80%, or

Wind speed ≥ 40 km/h, or

Thunderstorm forecast

⚠️ Risky

An event is classified as Risky if any hour has:

Rain probability between 60%–79%, or

Wind speed between 25–39 km/h

✅ Safe

No risky or unsafe conditions detected during the event window.

Each response includes human-readable reasons explaining which rule(s) were triggered.

🎯 Severity Score (0–100)

In addition to categorical classification, a numeric severity score is computed to represent overall weather risk.

Scoring Logic

Rain contribution (0–40)

Wind contribution (0–30)

Thunderstorm contribution (0–30)

The final severity score reflects the worst expected conditions during the event window and does not override the Safe / Risky / Unsafe classification.

🧠 Design Overview

Controller
Handles HTTP requests and input validation

Service Layer
Orchestrates workflow, validates business rules, builds responses

Weather API Client
Encapsulates external integration with Open-Meteo

Classification Engine
Applies deterministic weather risk rules

DTOs
Clean request/response models with validation

Global Exception Handler
Centralized, structured error handling

This separation keeps the system clean, testable, and easy to explain.

⚙️ Setup Instructions
Prerequisites

Java 17+

Maven 3+

Internet access (for Open-Meteo API)

Build & Run
mvn clean install
mvn spring-boot:run


The application starts at:

http://localhost:8080

📘 Swagger / OpenAPI

Interactive API documentation is available at:

http://localhost:8080/swagger-ui/index.html

⚠️ Validation & Error Handling

Request fields validated using Bean Validation

Logical checks:

startTime must be in the future

endTime must be after startTime

Forecasts supported only for the next 7 days

Errors returned as structured JSON with clear messages

🔍 Key Assumptions & Trade-offs

Weather data is evaluated hourly

Event times are assumed to be in the local timezone of the event location

Forecast availability is limited to 7 days

Classification logic is rule-based for transparency

Severity score is heuristic, not meteorological

No persistence or authentication (out of scope)

Focus is on service-layer business logic rather than controller tests
