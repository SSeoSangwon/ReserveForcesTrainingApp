# ReserveForcesTraining APP

**Spring 2023 · SKKU Mobile App Programming Lab (SWE3047) Project**

## Overview

ReserveForcesTraining APP is an Android course project for managing reserve-force training workflows. The application separates user and manager flows and combines Firebase-backed data management with public training-camp information and map visualization.

This repository was later organized and published on GitHub for archival and portfolio purposes. Secrets and project-specific cloud configuration from the original submission are intentionally excluded.

## Technologies

- Kotlin
- Android SDK (minSdk 28, targetSdk 33)
- XML-based Android UI
- Firebase Realtime Database
- Google Maps SDK for Android
- Republic of Korea Ministry of National Defense OpenAPI

## Main Implementation

### User flow

- User registration and login
- Training registration / discharge request state handling
- Training progress and score display
- Reserve-force training-camp contact lookup
- Training-camp location lookup and Google Maps visualization
- Link to the Ministry of National Defense YouTube channel

### Manager flow

- Manager authentication flow
- Registration / discharge request processing
- User training-state management
- Training score and completion-time input
- Manager password update

### Data layer

`DataBase_ALL.kt` contains the Firebase Realtime Database access logic and the data models used by the user and manager screens.

## Project Structure

```text
ReserveForcesTrainingApp/
├── app/
│   ├── src/main/java/edu/skku/cs/reserveforcestraining/
│   ├── src/main/res/
│   └── build.gradle
├── gradle/
├── build.gradle
├── settings.gradle
├── local.properties.example
└── README.md
```

## Local Configuration

The original project contained service credentials. They are not included in this public repository.

1. Create or update the root `local.properties` file and add:

```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
MND_API_KEY=YOUR_MND_OPENAPI_KEY
```

2. For Firebase, create your own Firebase Android application and place its configuration file at:

```text
app/google-services.json
```

`local.properties` and `app/google-services.json` are ignored by Git and must not be committed.

## How to Run

1. Open the project in Android Studio.
2. Configure Firebase Realtime Database for the application.
3. Add `app/google-services.json`.
4. Add the Google Maps and Ministry of National Defense OpenAPI keys to `local.properties`.
5. Sync Gradle and run the `app` module on an Android device or emulator.

## Security and Limitations

This is a 2023 academic prototype rather than a production authentication or military information system. The original application-level credential handling and database schema were designed for coursework. Do not use real personal or operational data with this implementation without redesigning authentication, authorization, database rules, secret management, and data protection.

The public version removes the original Google Maps key, Ministry of National Defense OpenAPI key, Firebase configuration file, student-number-based package identifier, and hard-coded personal manager identifier.

## Project Context

Developed as a project for **SWE3047 Mobile App Programming Lab** at Sungkyunkwan University during Spring 2023.
