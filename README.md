# 📄 Android Jetpack Compose MVVM Project

## 📌 Overview
This project is an **Android application** built using **Jetpack Compose** and **MVVM architecture**.  
It demonstrates clean architecture principles, lifecycle-aware UI updates, and modular code organization, making it easy to maintain and extend.

---

## 🏗 Architecture
We follow the **MVVM (Model–View–ViewModel)** pattern:

```
UI (View) <--> ViewModel <--> Repository <--> Data Sources (Network / Local)
```

- **UI (View)**  
  - Built entirely with **Jetpack Compose**.
  - Uses `StateFlow` or `LiveData` to observe state from the `ViewModel`.
  - Displays state using composables that are lifecycle-aware via `collectAsStateWithLifecycle()`.

- **ViewModel**  
  - Holds UI state and business logic.
  - Exposes state as `StateFlow` or `LiveData` to the UI.
  - Receives dependencies via **Hilt** (dependency injection).

- **Repository**  
  - Handles data operations and abstracts data sources.
  - Coordinates between local (Room database, DataStore) and remote (Retrofit, Ktor) sources.

- **Data Sources**  
  - **Network**: API requests and responses.
  - **Local**: Caching and persistence.

---

## 🛠 Tech Stack
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)  
- **Architecture**: MVVM + Clean Architecture principles  
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/)  
- **Networking**: Retrofit / Ktor (configurable)  
- **Coroutines & Flow**: Asynchronous programming  
- **State Management**: `StateFlow` + `collectAsStateWithLifecycle()`  
- **Testing**:  
  - Unit Tests: JUnit + MockK  
  - UI Tests: Compose UI Test  
- **Image Loading**: Coil (if needed)  
- **Persistence**: Room / DataStore (optional)  

---

## 📂 Project Structure
```
app/
 ├── di/                  # Hilt modules for dependency injection
 ├── ui/                  # UI screens and composables
 │    ├── screen_name/
 │    │    ├── ScreenNameScreen.kt
 │    │    ├── ScreenNameViewModel.kt
 │    │    ├── components/
 │    │    └── ScreenNameUiState.kt
 ├── data/
 │    ├── repository/     # Repository implementations
 │    ├── remote/         # Network API interfaces
 │    ├── local/          # Database, DataStore
 │    └── model/          # Data models (DTOs, entities)
 ├── domain/              # Optional: Use cases, domain models
 ├── utils/               # Helper classes and extensions
 ├── MainActivity.kt
 └── App.kt               # Application class
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Giraffe or newer
- **Kotlin** 1.9+
- **Gradle** 8.x
- **Minimum SDK**: 24+
- **Target SDK**: 34

### Setup
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/yourproject.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle to download dependencies.
4. Build and run the app on an emulator or device.

---

## 📌 Usage Example (UI Flow)
```kotlin
@Composable
fun ExampleScreen(viewModel: ExampleViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> Text("Data: ${(uiState as UiState.Success).data}")
        is UiState.Error -> Text("Something went wrong")
    }
}
```

---

## 🧪 Running Tests
**Unit tests:**
```bash
./gradlew test
```

**UI tests:**
```bash
./gradlew connectedAndroidTest
```

---

## 🔮 Future Improvements
- Add pagination support in the repository.
- Implement dark mode toggle using DataStore.
- Add offline-first caching strategy with Room.
- Improve test coverage for edge cases.

---

## 📜 License
This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
