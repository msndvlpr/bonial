# 📄 Android Jetpack Compose MVVM Project

## 📌 Overview
This project as part of a technical assessment, is an **Android application** built using **Jetpack Compose** and **MVVM architecture**.  
It demonstrates clean architecture principles, lifecycle-aware UI updates, and modular code organization, making it easy to maintain and extend.

---

## 🏗 Architecture
We follow the **MVVM (Model–View–ViewModel)** pattern:

```
UI (View) <--> ViewModel <--> Repository <--> Data Source (Network)
```

- **UI (View)**  
  - Built entirely with **Jetpack Compose**.
  - Uses `StateFlow` to observe state from the `ViewModel`.
  - Displays state using composables that are lifecycle-aware via `collectAsStateWithLifecycle()`.

- **ViewModel**  
  - Holds UI state and light weight UI logic.
  - Exposes state as `StateFlow` to the UI.
  - Receives dependencies via **Hilt** (dependency injection).

- **Repository**  
  - Fetches data from datasource and handles business logic.
  - Coordinates between datasource and the view model layers.

- **Data Sources**  
  - **Network**: Http Rest API request and response handling.

---

## 📜 Application Features
- This application fetches the list of retailer advertisements with their images and shows them in a single screen as brochures.
- As requested in the requirement document, it shows only the brochures with type BROCHURE and BROCHURE_PREMIUM and filters the items closer than 5 km.
- So a menu has been added to let the user change the brochure type filter between BROCHURE and BROCHURE_PREMIUM.
- Additionally in the menu, a switch button has been added for Dark/Light theme change, as it has been implemented.
- It supports different UI state handling based on the response received from Rest API, so it initially shows LOADING, and then SUCCESS state which shows the brochure items, or ERROR in case of any failure.
- As requested in the requirements, it supports screen rotation and the layout changes accordingly to fit the new orientation, and each item type also must be shown differently if it is BROCHURE or BROCHURE_PREMIUM.

---

## 🛠 Tech Stack
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)  
- **Architecture**: MVVM + Clean Architecture principles  
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)  
- **Networking**: Ktor
- **Coroutines & Flow**: Asynchronous and reactive programming  
- **State Management**: `StateFlow` + `collectAsStateWithLifecycle()`  
- **Testing**:  
  - Unit Tests: JUnit + MockK  
  - UI Tests: Compose UI Test  
- **Image Loading**: Coil 

---

## 📂 Overall Project Structure
```
app/
 ├── di/                  # Hilt modules for dependency injection
 ├── ui/                  # UI screens and composables
 │    ├── home/
 │    │    ├── BrochureScreen.kt
 │    │    ├── BrochureViewModel.kt
 │    │    ├── ErrorScreen.kt
 │    │    ├── widget/
 │    │    ├── model/
 │    │    ├── theme/
 │    │    └── ViewState.kt
 ├── data/
 │    ├── repository/                 # Repository implementations
 │         └── advertisement/         # Handling advertisement data
 │                └── model/          # Data models (DTOs, entities)
 │           
 │    ├── datasource/                 # Datasource implementations
 │         └── remote/                # Network Rest API 
 └── MainActivity.kt
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Meerkat or newer
- **Kotlin** 1.9+
- **Gradle** 8.x
- **Minimum SDK**: 24+
- **Target SDK**: 34

### Setup
1. Clone this repository:
   ```bash
   git clone https://github.com/msndvlpr/bonial.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle to download dependencies.
4. Build and run the app on an emulator or device.

---

## 🔮 Future Improvements
Due to the time constraints and also the API limitations, the following could also have been implemented:
- Add pagination support for in the repository and also view when the API supports.
- Add more filtering possibilities for the user when the API supports it.
- Add offline-first caching strategy with Room.
- Improve test coverage for all available units/widgets and also edge case.
