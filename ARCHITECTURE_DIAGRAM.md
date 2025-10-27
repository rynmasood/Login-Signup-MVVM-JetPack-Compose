# Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ANDROID APPLICATION                                  │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                          PRESENTATION LAYER (UI)                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │ SplashScreen │  │ LoginScreen  │  │ SignUpScreen │  │  Dashboard   │   │
│  │              │  │              │  │              │  │    Screen    │   │
│  │  - Logo      │  │  - Email     │  │  - Full Name │  │  - Welcome   │   │
│  │  - Loading   │  │  - Password  │  │  - Email     │  │  - Logout    │   │
│  │              │  │  - Login Btn │  │  - Password  │  │              │   │
│  │              │  │  - SignUp    │  │  - Confirm   │  │              │   │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘   │
│         │                 │                  │                 │            │
│         └─────────────────┴──────────────────┴─────────────────┘            │
│                                    │                                         │
│                                    │ observes StateFlow                      │
│                                    ↓                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐   │
│  │                           VIEWMODELS                                 │   │
│  ├─────────────────────────────────────────────────────────────────────┤   │
│  │                                                                      │   │
│  │  ┌────────────────┐  ┌────────────────┐  ┌────────────────┐        │   │
│  │  │ SplashViewModel│  │ LoginViewModel │  │SignUpViewModel │  etc.. │   │
│  │  │                │  │                │  │                │        │   │
│  │  │ - StateFlow    │  │ - email        │  │ - fullName     │        │   │
│  │  │ - isLoggedIn   │  │ - password     │  │ - email        │        │   │
│  │  │                │  │ - loginState   │  │ - password     │        │   │
│  │  │ - check()      │  │ - login()      │  │ - confirmPass  │        │   │
│  │  │                │  │ - validate()   │  │ - signUp()     │        │   │
│  │  └────────┬───────┘  └────────┬───────┘  └────────┬───────┘        │   │
│  │           │                   │                   │                 │   │
│  │           │                   │                   │                 │   │
│  └───────────┼───────────────────┼───────────────────┼─────────────────┘   │
│              │                   │                   │                      │
│              └───────────────────┴───────────────────┘                      │
│                                  │                                           │
│                                  │ depends on                                │
│                                  ↓                                           │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                          DOMAIN LAYER (Business Logic)                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                        Repository Interface                           │  │
│  │  ┌──────────────────────────────────────────────────────────────┐    │  │
│  │  │  interface AuthRepository                                     │    │  │
│  │  │    - registerUser(user: User): Result<User>                  │    │  │
│  │  │    - loginUser(email, password): Result<User>                │    │  │
│  │  │    - isEmailExists(email): Boolean                           │    │  │
│  │  └──────────────────────────────────────────────────────────────┘    │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                          Domain Models                                │  │
│  │  ┌──────────────────────────────────────────────────────────────┐    │  │
│  │  │  data class User(                                             │    │  │
│  │  │    email: String,                                             │    │  │
│  │  │    fullName: String,                                          │    │  │
│  │  │    password: String                                           │    │  │
│  │  │  )                                                            │    │  │
│  │  └──────────────────────────────────────────────────────────────┘    │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
└──────────────────────────────────────┬───────────────────────────────────────┘
                                       │
                                       │ implemented by
                                       ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                            DATA LAYER                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                    Repository Implementation                          │  │
│  │  ┌──────────────────────────────────────────────────────────────┐    │  │
│  │  │  class AuthRepositoryImpl(userDao: UserDao)                  │    │  │
│  │  │    - registerUser() → userDao.insertUser()                   │    │  │
│  │  │    - loginUser() → userDao.getUserByEmailAndPassword()       │    │  │
│  │  │    - isEmailExists() → userDao.isEmailExists()               │    │  │
│  │  │    - Transforms: UserEntity ↔ User                           │    │  │
│  │  └──────────────────────────────────────────────────────────────┘    │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                  │                                           │
│                                  │ uses                                      │
│                                  ↓                                           │
│  ┌────────────────────────┐  ┌─────────────────────────────────────────┐  │
│  │  PreferencesManager    │  │      Room Database                       │  │
│  │                        │  │                                          │  │
│  │  - saveLoginStatus()   │  │  ┌───────────────────────────────────┐  │  │
│  │  - isLoggedIn()        │  │  │  @Database AppDatabase             │  │  │
│  │  - getUserName()       │  │  │    - userDao(): UserDao            │  │  │
│  │  - clearLoginData()    │  │  └───────────────┬───────────────────┘  │  │
│  │                        │  │                  │                       │  │
│  │  Uses:                 │  │                  ↓                       │  │
│  │  SharedPreferences     │  │  ┌───────────────────────────────────┐  │  │
│  │                        │  │  │  @Dao UserDao                      │  │  │
│  └────────────────────────┘  │  │    - insertUser(user)              │  │  │
│                               │  │    - getUserByEmailAndPassword()   │  │  │
│                               │  │    - isEmailExists()               │  │  │
│                               │  └───────────────┬───────────────────┘  │  │
│                               │                  │                       │  │
│                               │                  ↓                       │  │
│                               │  ┌───────────────────────────────────┐  │  │
│                               │  │  @Entity UserEntity                │  │  │
│                               │  │    @PrimaryKey email: String       │  │  │
│                               │  │    fullName: String                │  │  │
│                               │  │    password: String                │  │  │
│                               │  └───────────────────────────────────┘  │  │
│                               └─────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                    DEPENDENCY INJECTION (Hilt)                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  @HiltAndroidApp                                                      │  │
│  │  class AuthApp : Application()  ← Entry point                        │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  @Module @InstallIn(SingletonComponent::class)                       │  │
│  │  object DatabaseModule {                                              │  │
│  │    @Provides fun provideDatabase(): AppDatabase                      │  │
│  │    @Provides fun provideUserDao(): UserDao                           │  │
│  │  }                                                                    │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  @Module @InstallIn(SingletonComponent::class)                       │  │
│  │  abstract class RepositoryModule {                                    │  │
│  │    @Binds abstract fun bindAuthRepository(                           │  │
│  │      impl: AuthRepositoryImpl                                        │  │
│  │    ): AuthRepository                                                 │  │
│  │  }                                                                    │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                         NAVIGATION                                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  sealed class Screen(route: String)                                  │  │
│  │    - Splash    → "splash"                                            │  │
│  │    - Login     → "login"                                             │  │
│  │    - SignUp    → "signup"                                            │  │
│  │    - Dashboard → "dashboard"                                         │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  NavHost(navController, startDestination = Screen.Splash) {          │  │
│  │    composable(Splash) { SplashScreen() }                             │  │
│  │    composable(Login) { LoginScreen() }                               │  │
│  │    composable(SignUp) { SignUpScreen() }                             │  │
│  │    composable(Dashboard) { DashboardScreen() }                       │  │
│  │  }                                                                    │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘


═══════════════════════════════════════════════════════════════════════════════
                              DATA FLOW EXAMPLE
═══════════════════════════════════════════════════════════════════════════════

USER SIGNUP FLOW:
─────────────────

1. SignUpScreen
   ↓ User enters: "John Doe", "john@email.com", "password123"
   
2. User clicks "Sign Up" button
   ↓ Calls: viewModel.signUp()
   
3. SignUpViewModel
   ↓ Validates inputs (email has @, password >= 6, passwords match)
   ↓ Creates: User("john@email.com", "John Doe", "password123")
   ↓ Calls: repository.registerUser(user)
   
4. AuthRepositoryImpl
   ↓ Checks: userDao.isEmailExists("john@email.com")
   ↓ Transforms: User → UserEntity
   ↓ Calls: userDao.insertUser(userEntity)
   
5. Room Database (UserDao)
   ↓ SQL: INSERT INTO users VALUES (...)
   ↓ Returns: Success
   
6. AuthRepositoryImpl
   ↓ Returns: Result.success(user)
   
7. SignUpViewModel
   ↓ Updates: _signUpState.value = SignUpState.Success
   
8. SignUpScreen
   ↓ Observes state change
   ↓ Navigates back to Login
   

USER LOGIN FLOW:
────────────────

1. LoginScreen
   ↓ User enters: "john@email.com", "password123"
   
2. User clicks "Login" button
   ↓ Calls: viewModel.login()
   
3. LoginViewModel
   ↓ Validates inputs
   ↓ Calls: repository.loginUser("john@email.com", "password123")
   
4. AuthRepositoryImpl
   ↓ Calls: userDao.getUserByEmailAndPassword("john@email.com", "password123")
   
5. Room Database (UserDao)
   ↓ SQL: SELECT * FROM users WHERE email=? AND password=?
   ↓ Returns: UserEntity("john@email.com", "John Doe", "password123")
   
6. AuthRepositoryImpl
   ↓ Transforms: UserEntity → User
   ↓ Returns: Result.success(user)
   
7. LoginViewModel
   ↓ Calls: preferencesManager.saveLoginStatus(true, "John Doe", "john@email.com")
   ↓ Updates: _loginState.value = LoginState.Success("John Doe")
   
8. SharedPreferences
   ↓ Stores: is_logged_in=true, user_name="John Doe", user_email="john@email.com"
   
9. LoginScreen
   ↓ Observes state change
   ↓ Navigates to Dashboard


SPLASH SCREEN CHECK:
────────────────────

1. SplashScreen shows
   ↓ After 3 seconds
   
2. SplashViewModel
   ↓ Calls: preferencesManager.isLoggedIn()
   
3. SharedPreferences
   ↓ Returns: true (user is logged in)
   
4. SplashViewModel
   ↓ Updates: _isLoggedIn.value = true
   
5. SplashScreen
   ↓ Observes state change
   ↓ Navigates to Dashboard (skips Login)


═══════════════════════════════════════════════════════════════════════════════
                            DEPENDENCY GRAPH
═══════════════════════════════════════════════════════════════════════════════

SplashScreen     LoginScreen     SignUpScreen     DashboardScreen
     ↓                ↓                ↓                 ↓
SplashViewModel  LoginViewModel  SignUpViewModel  DashboardViewModel
     ↓                ↓                ↓                 ↓
PreferencesManager   AuthRepository   AuthRepository   PreferencesManager
                          ↓
                  AuthRepositoryImpl
                          ↓
                      UserDao
                          ↓
                    AppDatabase

All provided by Hilt automatically!
```
