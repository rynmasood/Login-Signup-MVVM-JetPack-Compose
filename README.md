# Authentication App - MVVM Architecture with Hilt

This is a simple Android authentication app built with **MVVM architecture**, **Jetpack Compose**, **Room Database**, and **Hilt** for dependency injection.

## 📁 Project Structure

```
app/src/main/java/com/example/auththentication/
├── data/                           # Data Layer
│   ├── local/
│   │   ├── dao/                   # Database Access Objects
│   │   │   └── UserDao.kt         # User database operations
│   │   ├── database/
│   │   │   └── AppDatabase.kt     # Room database configuration
│   │   └── entity/
│   │       └── UserEntity.kt      # User table schema
│   ├── repository/
│   │   └── AuthRepositoryImpl.kt  # Repository implementation
│   └── preferences/
│       └── PreferencesManager.kt  # SharedPreferences manager
│
├── domain/                         # Domain Layer (Business Logic)
│   ├── model/
│   │   └── User.kt                # Domain model
│   └── repository/
│       └── AuthRepository.kt      # Repository interface
│
├── presentation/                   # UI Layer
│   ├── splash/
│   │   ├── SplashViewModel.kt     # Splash screen logic
│   │   └── SplashScreen.kt        # Splash screen UI
│   ├── login/
│   │   ├── LoginViewModel.kt      # Login logic
│   │   ├── LoginState.kt          # Login states
│   │   └── LoginScreen.kt         # Login UI
│   ├── signup/
│   │   ├── SignUpViewModel.kt     # SignUp logic
│   │   ├── SignUpState.kt         # SignUp states
│   │   └── SignUpScreen.kt        # SignUp UI
│   ├── dashboard/
│   │   ├── DashboardViewModel.kt  # Dashboard logic
│   │   └── DashboardScreen.kt     # Dashboard UI
│   └── navigation/
│       ├── Screen.kt              # Navigation routes
│       └── NavGraph.kt            # Navigation setup
│
├── di/                             # Dependency Injection
│   ├── DatabaseModule.kt          # Provides database dependencies
│   └── RepositoryModule.kt        # Provides repository dependencies
│
├── ui/theme/                       # Theme files
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
│
├── AuthApp.kt                      # Application class (Hilt entry point)
└── MainActivity.kt                 # Main activity
```

## 🎯 Features

1. **Splash Screen (3 seconds)**
   - Checks if user is logged in using SharedPreferences
   - Navigates to Dashboard if logged in, Login if not

2. **Login Screen**
   - Email validation (must contain @)
   - Password validation (minimum 6 characters)
   - Checks credentials against Room database
   - Saves login status to SharedPreferences

3. **SignUp Screen**
   - Full Name field
   - Email validation (must contain @)
   - Password validation (minimum 6 characters)
   - Confirm Password validation (must match password)
   - Stores user in Room database
   - Navigates back to Login after successful signup

4. **Dashboard Screen**
   - Displays welcome message with user's full name
   - Logout button
   - Clears SharedPreferences and navigates to Login

## 🏗️ Architecture Overview

### MVVM (Model-View-ViewModel)

```
View (Composable) ↔️ ViewModel ↔️ Repository ↔️ Data Source
```

- **View (Screen)**: Displays UI, observes ViewModel state
- **ViewModel**: Contains UI logic, exposes state via StateFlow
- **Repository**: Abstracts data sources, handles data operations
- **Data Source**: Room database, SharedPreferences

### Why MVVM?

1. **Separation of Concerns**: UI, business logic, and data are separate
2. **Testability**: Each layer can be tested independently
3. **Lifecycle Awareness**: ViewModels survive configuration changes
4. **Reactive**: UI automatically updates when state changes

## 🔧 Key Technologies

### 1. **Hilt (Dependency Injection)**
- **Purpose**: Automatically provides dependencies
- **Benefits**: 
  - No manual object creation
  - Easy to swap implementations
  - Better testing
- **Usage**: 
  - `@HiltAndroidApp` on Application class
  - `@AndroidEntryPoint` on Activity
  - `@HiltViewModel` on ViewModels
  - `@Inject` to receive dependencies

### 2. **Room Database**
- **Purpose**: Local database for storing user data
- **Components**:
  - `UserEntity`: Defines table structure
  - `UserDao`: Database operations (insert, query)
  - `AppDatabase`: Database configuration
- **Benefits**: 
  - Type-safe
  - Compile-time verification
  - LiveData/Flow support

### 3. **SharedPreferences**
- **Purpose**: Store simple login state
- **Benefits**: 
  - Fast for small data
  - Persists across app restarts
  - Perfect for login status

### 4. **Jetpack Compose**
- **Purpose**: Modern UI toolkit
- **Benefits**: 
  - Declarative UI
  - Less boilerplate
  - Kotlin-first

### 5. **Navigation Compose**
- **Purpose**: Handle screen navigation
- **Benefits**: 
  - Type-safe navigation
  - Automatic back stack management
  - Deep linking support

### 6. **StateFlow**
- **Purpose**: Reactive state management
- **Benefits**: 
  - UI automatically updates when state changes
  - Type-safe
  - Works great with Compose

## 📝 How It Works

### 1. App Startup
```
AuthApp (@HiltAndroidApp)
    ↓
MainActivity (@AndroidEntryPoint)
    ↓
NavGraph (starts with Splash)
```

### 2. Splash Screen Flow
```
SplashScreen
    ↓
SplashViewModel.checkLoginStatus()
    ↓
PreferencesManager.isLoggedIn()
    ↓
Navigate to Login OR Dashboard
```

### 3. SignUp Flow
```
SignUpScreen (user fills form)
    ↓
SignUpViewModel.signUp()
    ↓
Validate inputs (email has @, password >= 6, passwords match)
    ↓
AuthRepository.registerUser()
    ↓
UserDao.insertUser() → Room Database
    ↓
Navigate back to Login
```

### 4. Login Flow
```
LoginScreen (user enters credentials)
    ↓
LoginViewModel.login()
    ↓
Validate inputs
    ↓
AuthRepository.loginUser()
    ↓
UserDao.getUserByEmailAndPassword() → Check Room Database
    ↓
PreferencesManager.saveLoginStatus() → Save to SharedPreferences
    ↓
Navigate to Dashboard
```

### 5. Dashboard Flow
```
DashboardScreen
    ↓
DashboardViewModel loads user name from PreferencesManager
    ↓
User clicks Logout
    ↓
PreferencesManager.clearLoginData()
    ↓
Navigate to Login
```

## 🔄 Data Flow

### SignUp Data Flow
```
SignUpScreen
    ↓ (User object)
SignUpViewModel
    ↓ (User)
AuthRepository
    ↓ (UserEntity)
UserDao
    ↓
Room Database
```

### Login Data Flow
```
LoginScreen
    ↓ (email, password)
LoginViewModel
    ↓
AuthRepository
    ↓
UserDao (queries database)
    ↓
Room Database
    ↓
Returns User
    ↓
Save to PreferencesManager
```

## 🎨 UI Components

### Composable Functions
- **SplashScreen**: Shows logo and loading indicator
- **LoginScreen**: Email/Password fields, Login button
- **SignUpScreen**: Full Name, Email, Password, Confirm Password, SignUp button
- **DashboardScreen**: Welcome message, Logout button

### State Management
- **StateFlow**: Observable state in ViewModels
- **collectAsState()**: Converts StateFlow to Compose State
- **LaunchedEffect**: Side effects in Composables (navigation, one-time actions)

## 🧪 Testing Strategy

### Unit Tests
- **ViewModels**: Test business logic (validation, state changes)
- **Repository**: Test data operations
- **Use Cases**: Test business rules

### Integration Tests
- **Database**: Test Room operations
- **Repository + Database**: Test data flow

### UI Tests
- **Compose UI Tests**: Test user interactions

## 🚀 How to Build and Run

1. **Sync Gradle**: 
   ```
   File → Sync Project with Gradle Files
   ```

2. **Build Project**:
   ```
   Build → Make Project
   ```

3. **Run on Emulator/Device**:
   ```
   Run → Run 'app'
   ```

## 📚 Learning Points

### 1. MVVM Separation
- **View**: Just displays data, no logic
- **ViewModel**: Holds UI state and logic
- **Model**: Data structures and business logic

### 2. Dependency Injection
- Hilt automatically creates objects
- Easy to replace implementations
- Better for testing

### 3. Repository Pattern
- Single source of truth
- Abstracts data sources
- UI doesn't know if data comes from database, network, or cache

### 4. Clean Architecture
- Each layer has specific responsibility
- Inner layers don't depend on outer layers
- Easy to change implementation

### 5. Reactive Programming
- UI observes data
- When data changes, UI automatically updates
- No manual UI updates needed

## 🔒 Security Note

⚠️ **Important**: This is a learning project. In a production app:
- **NEVER** store passwords in plain text
- Use encryption (bcrypt, argon2)
- Use proper authentication (OAuth, JWT)
- Implement password recovery
- Add rate limiting
- Use HTTPS for network calls

## 📖 Next Steps to Learn

1. **Add Password Encryption**: Use bcrypt or similar
2. **Add Form Validation States**: Show real-time validation errors
3. **Add Loading States**: Show shimmer effects
4. **Add Error Handling**: Better error messages
5. **Add Unit Tests**: Test ViewModels and Repository
6. **Add Remember Me**: Option to stay logged in
7. **Add Profile Screen**: View/Edit user profile
8. **Add Password Recovery**: Forgot password feature
9. **Add Email Verification**: Verify email before login
10. **Add Biometric Auth**: Fingerprint/Face recognition

## 🎓 Key Concepts Learned

✅ MVVM Architecture
✅ Hilt Dependency Injection
✅ Room Database (Entity, DAO, Database)
✅ SharedPreferences
✅ Jetpack Compose UI
✅ Navigation Compose
✅ StateFlow and State Management
✅ Coroutines (viewModelScope, suspend functions)
✅ Repository Pattern
✅ Clean Architecture Principles
✅ Input Validation
✅ Sealed Classes for State
✅ LaunchedEffect for side effects

---

**Happy Learning! 🎉**
#   L o g i n - S i g n u p - M V V M - J e t P a c k - C o m p o s e 
 
 
