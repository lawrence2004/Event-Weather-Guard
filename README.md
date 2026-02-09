# 🌦️ Event Weather Guard

A Spring Boot backend service that analyzes hourly weather forecasts and determines whether an outdoor event is **Safe**, **Risky**, or **Unsafe** based on deterministic, explainable rules.

This project was built as a **backend intern take‑home assignment** focusing on API design, external integration, validation, and clean business logic.

---

## 🚀 Features

* REST API to analyze weather risk for outdoor events based on a given time window
* Integration with Open-Meteo public weather API using hourly forecasts
* Deterministic Safe / Risky / Unsafe classification using clearly defined rules
* Numeric severity score (0–100) to quantify overall weather risk
* Human-readable explanations with time-range aggregation (rain, wind, storms)
* Robust input validation (past dates, invalid ranges, forecast limits)
* Centralized global exception handling with structured error responses
* Clean layered architecture (Controller → Service → Client → Rule Engine)
* Swagger / OpenAPI documentation for easy API exploration
* Stateless design with no database or authentication, as per assignment scope

---

## 🛠️ Tech Stack

* Java 17 – Primary programming language
* Spring Boot 3.x – Backend framework for building RESTful services
* Spring MVC – Request handling and controller layer
* RestTemplate – Blocking HTTP client for external weather API integration
* Open-Meteo Weather API – Public API for hourly weather forecasts
* Lombok – Reduces boilerplate code for DTOs and models
* Springdoc OpenAPI – Swagger UI and OpenAPI documentation
* Maven – Build and dependency management tool

---

## 📦 API Endpoint

### `POST /event-forecast`

Evaluates weather conditions during the event time window and returns a risk classification.

---

## 📤 API Usage Examples

### Example 1: Safe Event

**Request**

```json
{
  "name": "College Festival",
  "location": {
    "latitude": 19.778506, 
    "longitude": 72.750388
  },
  "startTime": "2026-02-15T15:30:00",
  "endTime": "2026-02-15T19:56:00"
}
```

**Response**

```json
{
    "classification": "Safe",
    "severityScore": 12,
    "summary": "Weather conditions are suitable throughout the event duration",
    "reason": [
        "No significant weather risks detected"
    ],
    "eventWindowForecast": [
        {
            "time": "16:00",
            "rainProb": 0,
            "windKmh": 20.0
        },
        {
            "time": "17:00",
            "rainProb": 0,
            "windKmh": 18.1
        },
        {
            "time": "18:00",
            "rainProb": 0,
            "windKmh": 15.6
        },
        {
            "time": "19:00",
            "rainProb": 0,
            "windKmh": 13.0
        }
    ]
}
```

---

### Example 2: Risky Event

**Request**

```json
{
  "name": "Marriage Event",
  "location": {
    "latitude": 30.778506, 
    "longitude": 83.750388
  },
  "startTime": "2026-02-15T15:30:00",
  "endTime": "2026-02-15T19:56:00"
}
```

**Response**

```json
{
    "classification": "Risky",
    "severityScore": 21,
    "summary": "Weather conditions may impact parts of the event",
    "reason": [
        "Strong wind (~31 km/h) from 16:00 to 19:00"
    ],
    "eventWindowForecast": [
        {
            "time": "16:00",
            "rainProb": 4,
            "windKmh": 31.1
        },
        {
            "time": "17:00",
            "rainProb": 4,
            "windKmh": 32.0
        },
        {
            "time": "18:00",
            "rainProb": 3,
            "windKmh": 30.6
        },
        {
            "time": "19:00",
            "rainProb": 3,
            "windKmh": 27.4
        }
    ]
}
```

---

## ⚖️ Weather Classification Rules

The system applies **deterministic, explainable rules** based on hourly weather data.

### ❌ Unsafe

* Thunderstorm forecast detected
* Rain probability ≥ **80%**
* Wind speed ≥ **40 km/h**

### ⚠️ Risky

* Rain probability between **60%** and **79%**
* Wind speed between **25 km/h** and **39 km/h**

### ✅ Safe

* No Unsafe or Risky conditions are detected during the event window

Each response includes **human‑readable reasons** explaining which rule(s) were triggered.

### 🎯 Severity Score (0–100)

In addition to classification, a numeric severity score is computed:

* Rain severity contributes up to 40 points, proportional to rain probability
* Wind severity contributes up to 30 points, with 50 km/h treated as extreme
* Thunderstorm presence contributes a fixed 30 points if detected
* Event severity is calculated using the worst-case hour within the event window
* Severity score is **additive** and does not override classification rules.

---

---

## 🧠 Design Overview

* **Controller**: Handles HTTP requests and validation
* **Service Layer**: Orchestrates flow and validates business rules
* **WeatherApiClient**: Isolates external API integration (Open‑Meteo)
* **Rule Engine**: Applies deterministic weather classification logic
* **DTOs**: Clean request/response models with validation
* **Global Exception Handler**: Centralized error handling

This separation keeps the system **clean, testable, and easy to explain**.

---

## 📘 Swagger / OpenAPI

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Setup Instructions

### Prerequisites

* Java 17 or higher installed
* Maven 3.x (optional – Maven Wrapper is included)
* Internet connection (required for Open-Meteo API)

### Clone the Repository

* git clone Event-Weather-Guard
* cd Event-Weather-Guard-main

### Build the Project and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The service will start at:

```
http://localhost:8080
```

---

## ⚠️ Validation & Error Handling

* Request fields are validated using Jakarta Bean Validation
* Logical validation ensures startTime is in the future
* Logical validation ensures endTime is after startTime
* Event time window is validated to be within the supported forecast range (next 7 days)
* Invalid or unsupported requests return structured JSON error responses
* All errors are handled centrally using a global exception handler

Example 1 error response:

```json
{
  "message": "Event start time must be in the future",
  "status": 400
}
```

Example 2 error response:

```json
{
  "message": "Event start time cannot be greater than end time",
  "status": 400
}
```

Example 3 error response:

```json
{
  "message": "Weather forecast not available for the given time window",
  "status": 404
}
```

---

## 🔍 Key Assumptions & Trade‑offs

* Weather forecasts are **hourly**, and all evaluations align strictly to hourly boundaries
* Classification logic is **rule‑based and deterministic** for transparency and explainability
* The **severity score** is additive and does not override classification rules
* Alternate time recommendations are generated **only for RISKY or UNSAFE events**
* Recommended windows preserve the **original event duration** but shift the time window to reduce weather risk
* External weather API failures result in a fast‑fail error response
* No persistence layer or authentication is included (out of scope)
* Controller tests are intentionally omitted; unit tests focus on **service‑layer business logic**
