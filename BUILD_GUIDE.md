# Build and Run Guide

## 🚀 Steps to Build and Run

### 1. Sync Gradle Dependencies
After opening the project in Android Studio:

1. Click **"Sync Now"** in the notification bar, OR
2. Go to **File → Sync Project with Gradle Files**

**Wait for sync to complete** (may take a few minutes on first sync)

### 2. Build the Project

1. Go to **Build → Make Project**, OR
2. Press **Ctrl+F9** (Windows/Linux) or **Cmd+F9** (Mac)

**Check for build errors** in the Build tab at the bottom

### 3. Run on Emulator

1. Click **Run → Run 'app'**, OR
2. Press **Shift+F10** (Windows/Linux) or **Ctrl+R** (Mac)
3. Select an emulator or connected device

### 4. Testing the App

#### First Time (No Account)
1. **Splash Screen** appears for 3 seconds → Navigates to **Login**
2. Click **"Don't have an account? Sign Up"**
3. Fill in the **Sign Up** form:
   - Full Name: `John Doe`
   - Email: `john@example.com` (must have @)
   - Password: `password123` (min 6 characters)
   - Confirm Password: `password123`
4. Click **Sign Up** → Navigates back to **Login**
5. Enter credentials:
   - Email: `john@example.com`
   - Password: `password123`
6. Click **Login** → Navigates to **Dashboard**
7. See welcome message: **"Welcome, John Doe"**

#### Second Time (Already Logged In)
1. **Splash Screen** appears for 3 seconds → Directly navigates to **Dashboard**
2. No need to login again!

#### Logout
1. On **Dashboard**, click **Logout**
2. Confirmation dialog appears
3. Click **Yes** → Navigates back to **Login**
4. Login status cleared from SharedPreferences

#### Testing Validation
**Login Screen**:
- Try login without @ in email → Error: "Please enter a valid email with @"
- Try password less than 6 chars → Error: "Password must be at least 6 characters"
- Try wrong credentials → Error: "Invalid email or password"

**SignUp Screen**:
- Try empty full name → Error: "Please enter your full name"
- Try email without @ → Error: "Please enter a valid email with @"
- Try password less than 6 chars → Error: "Password must be at least 6 characters"
- Try mismatched passwords → Error: "Passwords do not match"
- Try existing email → Error: "Email already registered"

## 🐛 Troubleshooting

### Error: "Unresolved reference: hilt"
**Solution**: 
1. File → Invalidate Caches → Invalidate and Restart
2. Build → Clean Project
3. Build → Rebuild Project

### Error: "Cannot access database on the main thread"
**Solution**: Already handled! All database operations use `suspend` functions

### Error: "Plugin [id: 'com.google.devtools.ksp'] was not found"
**Solution**: 
1. Check internet connection
2. Sync Gradle again
3. Update Gradle version if needed

### Error: No connected devices
**Solution**: 
1. Create an emulator: Tools → Device Manager → Create Device
2. Or connect a physical device with USB debugging enabled

### App crashes on launch
**Solution**: 
1. Check Logcat for errors
2. Verify all files are created correctly
3. Clean and Rebuild project

## 📱 Database Location

The Room database is stored at:
```
/data/data/com.example.auththentication/databases/auth_database
```

To view database (requires rooted device/emulator):
1. View → Tool Windows → Device File Explorer
2. Navigate to path above
3. Or use Android Studio Database Inspector

## 🔍 Debugging Tips

### View Database Inspector
1. Run the app
2. View → Tool Windows → App Inspection
3. Select "Database Inspector" tab
4. View `users` table

### View SharedPreferences
1. Run the app
2. Device File Explorer → data → data → com.example.auththentication → shared_prefs
3. Open `auth_preferences.xml`

### View Logcat
1. Run the app
2. View → Tool Windows → Logcat
3. Filter by package name: `com.example.auththentication`

## 📊 Project Structure Check

Make sure these files exist:

### Data Layer
- ✅ `data/local/entity/UserEntity.kt`
- ✅ `data/local/dao/UserDao.kt`
- ✅ `data/local/database/AppDatabase.kt`
- ✅ `data/preferences/PreferencesManager.kt`
- ✅ `data/repository/AuthRepositoryImpl.kt`

### Domain Layer
- ✅ `domain/model/User.kt`
- ✅ `domain/repository/AuthRepository.kt`

### Presentation Layer
- ✅ `presentation/splash/SplashViewModel.kt`
- ✅ `presentation/splash/SplashScreen.kt`
- ✅ `presentation/login/LoginViewModel.kt`
- ✅ `presentation/login/LoginState.kt`
- ✅ `presentation/login/LoginScreen.kt`
- ✅ `presentation/signup/SignUpViewModel.kt`
- ✅ `presentation/signup/SignUpState.kt`
- ✅ `presentation/signup/SignUpScreen.kt`
- ✅ `presentation/dashboard/DashboardViewModel.kt`
- ✅ `presentation/dashboard/DashboardScreen.kt`
- ✅ `presentation/navigation/Screen.kt`
- ✅ `presentation/navigation/NavGraph.kt`

### DI Layer
- ✅ `di/DatabaseModule.kt`
- ✅ `di/RepositoryModule.kt`

### Main Files
- ✅ `AuthApp.kt`
- ✅ `MainActivity.kt`
- ✅ `AndroidManifest.xml` (with android:name=".AuthApp")

### Build Files
- ✅ `app/build.gradle.kts` (with all dependencies)
- ✅ `gradle/libs.versions.toml` (with version catalog)

## ✨ Features to Test

- [ ] Splash screen shows for 3 seconds
- [ ] First launch goes to Login
- [ ] Can create account via Sign Up
- [ ] Email validation (@ required)
- [ ] Password validation (6+ chars)
- [ ] Confirm password validation
- [ ] Duplicate email prevention
- [ ] Login with correct credentials
- [ ] Login fails with wrong credentials
- [ ] Dashboard shows user's name
- [ ] Logout clears session
- [ ] Second launch (after login) goes directly to Dashboard
- [ ] Back button handling (can't go back from Dashboard to Login)

## 🎯 Next Steps After Running

1. **Explore the code** - Open each file and read the comments
2. **Try breaking it** - Enter invalid data, see how validation works
3. **Check Database Inspector** - See how data is stored
4. **Modify UI** - Change colors, add icons, etc.
5. **Add features** - Implement the "Next Steps to Learn" from README.md

---

**Need help? Check the README.md and MVVM_GUIDE.md for detailed explanations!**
