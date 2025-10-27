# MVVM Architecture Guide - Step by Step

## 🎯 What is MVVM?

**MVVM = Model - View - ViewModel**

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│    View     │ ←────→  │  ViewModel  │ ←────→  │    Model    │
│  (Screen)   │         │   (Logic)   │         │   (Data)    │
└─────────────┘         └─────────────┘         └─────────────┘
     ↑                         ↑                       ↑
   UI Layer            Presentation Layer         Data Layer
```

## 📱 Our App's MVVM Flow

### Login Example

```
┌────────────────────────────────────────────────────────────────┐
│                         Login Flow                              │
└────────────────────────────────────────────────────────────────┘

1. USER ENTERS EMAIL AND PASSWORD
   LoginScreen.kt (View)
   ↓
   
2. CLICK LOGIN BUTTON
   Calls: viewModel.login()
   ↓
   
3. VIEWMODEL VALIDATES INPUT
   LoginViewModel.kt
   - Check email has @
   - Check password length >= 6
   ↓
   
4. VIEWMODEL CALLS REPOSITORY
   viewModel → authRepository.loginUser(email, password)
   ↓
   
5. REPOSITORY QUERIES DATABASE
   AuthRepositoryImpl → userDao.getUserByEmailAndPassword()
   ↓
   
6. DATABASE RETURNS RESULT
   Room Database → UserEntity or null
   ↓
   
7. REPOSITORY RETURNS TO VIEWMODEL
   Result.success(user) or Result.failure(error)
   ↓
   
8. VIEWMODEL UPDATES STATE
   _loginState.value = LoginState.Success(user.fullName)
   ↓
   
9. VIEW OBSERVES STATE AND UPDATES UI
   LoginScreen observes loginState
   → Shows success message
   → Navigates to Dashboard
```

## 🔄 Data Flow Layers

### Layer 1: View (UI)
**Files**: LoginScreen.kt, SignUpScreen.kt, etc.
**Purpose**: Display UI and handle user interactions
**What it does**:
- Shows TextFields, Buttons, etc.
- Collects user input
- Observes ViewModel state
- Updates UI when state changes
**What it DOESN'T do**:
- No business logic
- No direct database access
- No data validation (just calls ViewModel)

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val email by viewModel.email.collectAsState()  // Observe
    val loginState by viewModel.loginState.collectAsState()  // Observe
    
    OutlinedTextField(
        value = email,
        onValueChange = { viewModel.onEmailChange(it) }  // Update
    )
    
    Button(onClick = { viewModel.login() })  // Action
}
```

### Layer 2: ViewModel (Presentation Logic)
**Files**: LoginViewModel.kt, SignUpViewModel.kt, etc.
**Purpose**: Hold UI state and handle user actions
**What it does**:
- Validates input
- Manages UI state
- Calls Repository for data
- Exposes data via StateFlow
- Survives configuration changes (screen rotation)
**What it DOESN'T do**:
- No direct database access
- No Android framework dependencies (Context, etc.)

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository  // Depends on Repository
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    fun login() {
        viewModelScope.launch {
            // Validate
            if (!isEmailValid(email)) { 
                _loginState.value = LoginState.Error("Invalid email")
                return@launch
            }
            
            // Call repository
            val result = repository.loginUser(email, password)
            
            // Update state
            result.fold(
                onSuccess = { user -> 
                    _loginState.value = LoginState.Success(user.fullName)
                },
                onFailure = { error -> 
                    _loginState.value = LoginState.Error(error.message)
                }
            )
        }
    }
}
```

### Layer 3: Repository (Data Abstraction)
**Files**: AuthRepositoryImpl.kt
**Purpose**: Abstract data sources, provide clean API
**What it does**:
- Decides where to get data (database, network, cache)
- Transforms data (Entity ↔ Domain Model)
- Handles errors
- Single source of truth
**What it DOESN'T do**:
- No UI logic
- Doesn't know about ViewModels or Views

```kotlin
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao  // Depends on DAO
) : AuthRepository {
    
    override suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            // Get from database
            val userEntity = userDao.getUserByEmailAndPassword(email, password)
            
            if (userEntity != null) {
                // Transform Entity to Domain Model
                val user = User(
                    email = userEntity.email,
                    fullName = userEntity.fullName,
                    password = userEntity.password
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Layer 4: Data Source (Database)
**Files**: UserDao.kt, AppDatabase.kt, UserEntity.kt
**Purpose**: Actual data storage and retrieval
**What it does**:
- Defines database schema (Entity)
- Performs CRUD operations (DAO)
- Manages database (Database class)

```kotlin
// Entity - Table structure
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val fullName: String,
    val password: String
)

// DAO - Operations
@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?
}
```

## 🔌 Hilt Dependency Injection

### What is Dependency Injection?

**Without DI** (Manual creation):
```kotlin
class LoginViewModel {
    private val database = Room.databaseBuilder(...)  // Need Context!
    private val dao = database.userDao()
    private val repository = AuthRepositoryImpl(dao)  // Manual creation
}
```
**Problems**:
- Hard to test
- Tightly coupled
- Need to pass Context everywhere

**With Hilt DI**:
```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository  // Hilt provides this!
) : ViewModel()
```
**Benefits**:
- Hilt creates everything automatically
- Easy to swap implementations
- Easy to test (can provide mock repository)

### How Hilt Works

```
1. @HiltAndroidApp on AuthApp
   ↓ Initializes Hilt

2. @AndroidEntryPoint on MainActivity
   ↓ Enables injection in Activity

3. @HiltViewModel on ViewModels
   ↓ Enables injection in ViewModels

4. @Module defines how to create objects
   ↓ DatabaseModule, RepositoryModule

5. @Inject tells Hilt to provide dependencies
   ↓ Hilt creates and injects automatically
```

### Hilt Flow Example

```kotlin
// 1. Application class
@HiltAndroidApp
class AuthApp : Application()

// 2. Module - Tells Hilt HOW to create Database
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "auth_db").build()
    }
    
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}

// 3. Module - Tells Hilt which implementation to use
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}

// 4. Repository - Receives UserDao from Hilt
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao  // Hilt provides this
) : AuthRepository

// 5. ViewModel - Receives Repository from Hilt
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository  // Hilt provides this
) : ViewModel()

// 6. Screen - Receives ViewModel from Hilt
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()  // Hilt provides this
)
```

## 🌊 StateFlow vs LiveData

### StateFlow (Used in this project)

```kotlin
// ViewModel
private val _state = MutableStateFlow(0)
val state: StateFlow<Int> = _state.asStateFlow()

// Screen (Compose)
val state by viewModel.state.collectAsState()
```

**Benefits**:
- Works great with Compose
- Kotlin-first (coroutines)
- Type-safe
- Always has a value

### LiveData (Traditional)

```kotlin
// ViewModel
private val _state = MutableLiveData(0)
val state: LiveData<Int> = _state

// Fragment/Activity
viewModel.state.observe(this) { value ->
    // Update UI
}
```

## 🎭 Sealed Classes for States

### Why Sealed Classes?

**Without Sealed Class**:
```kotlin
var isLoading = false
var error: String? = null
var data: User? = null

// Problem: Multiple states at once! 
// isLoading=true AND error!=null - Which one is correct?
```

**With Sealed Class**:
```kotlin
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

// Only ONE state at a time!
// Type-safe - compiler ensures all states are handled
```

**Usage**:
```kotlin
when (loginState) {
    is LoginState.Idle -> { /* Show initial UI */ }
    is LoginState.Loading -> { /* Show loading */ }
    is LoginState.Success -> { /* Navigate */ }
    is LoginState.Error -> { /* Show error */ }
}
```

## 🚀 Coroutines in MVVM

### What are Coroutines?

**Without Coroutines** (Blocking):
```kotlin
fun login() {
    val user = repository.loginUser(email, password)  // FREEZES UI!
}
```

**With Coroutines** (Non-blocking):
```kotlin
fun login() {
    viewModelScope.launch {  // Runs in background
        val user = repository.loginUser(email, password)
        // UI still responsive!
    }
}
```

### viewModelScope

```kotlin
viewModelScope.launch {
    // Automatically cancelled when ViewModel is destroyed
    // No memory leaks!
}
```

### suspend functions

```kotlin
suspend fun loginUser(...): Result<User> {
    // Can only be called from coroutine or another suspend function
    // Can perform long-running operations without blocking
}
```

## 📦 Summary: Data Flow for Login

```
┌─────────────────────────────────────────────────────────────────┐
│                      COMPLETE FLOW                               │
└─────────────────────────────────────────────────────────────────┘

USER
  ↓ enters email/password
LoginScreen (View)
  ↓ clicks login
viewModel.login()
  ↓ validates input
LoginViewModel (ViewModel)
  ↓ calls repository
authRepository.loginUser(email, password)
  ↓ queries database
AuthRepositoryImpl (Repository)
  ↓ uses DAO
userDao.getUserByEmailAndPassword(email, password)
  ↓ SQL query
Room Database (Data Source)
  ↓ returns UserEntity or null
AuthRepositoryImpl
  ↓ transforms to User, returns Result
LoginViewModel
  ↓ updates state
_loginState.value = LoginState.Success(user)
  ↓ emits via StateFlow
LoginScreen observes state
  ↓ UI recomposes
Shows success, navigates to Dashboard
```

## 🎓 Key Takeaways

1. **Separation of Concerns**: Each layer has ONE job
2. **Testability**: Easy to test each layer independently
3. **Maintainability**: Easy to modify one layer without affecting others
4. **Scalability**: Easy to add new features
5. **Hilt**: Automatic dependency management
6. **StateFlow**: Reactive state management
7. **Coroutines**: Non-blocking operations
8. **Sealed Classes**: Type-safe state handling

---

**Remember**: MVVM is about **organizing code**, not adding complexity!
