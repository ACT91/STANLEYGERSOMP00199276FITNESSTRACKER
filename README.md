# STANLEY GERSOMP Fitness Tracker

This repository contains an Android fitness-tracking app implemented in Kotlin. It includes an Android app (in `app/`) and a small PHP API (in `php_api/`) used for server-side endpoints during development.

## Project Structure

```
STANLEYGERSOMP00199276FITNESSTRACKER/
├── app/                                    # Android application module
│   ├── src/main/java/com/example/stanleygersomp00199276fitnesstracker/
│   │   ├── adapters/                       # RecyclerView adapters
│   │   ├── models/                         # Data models
│   │   ├── network/                        # API service and Retrofit client
│   │   ├── repository/                     # Data repository layer
│   │   ├── ui/                            # Activities and UI components
│   │   ├── utils/                         # Utility classes
│   │   └── viewmodel/                     # ViewModels for MVVM architecture
│   ├── src/main/res/                      # Android resources
│   │   ├── drawable/                      # Vector drawables and icons
│   │   ├── layout/                        # XML layout files
│   │   ├── values/                        # Colors, strings, themes
│   │   └── ...
│   └── build.gradle.kts                   # App module build configuration
├── php_api/                               # PHP backend API
│   ├── api/                              # API endpoints
│   │   ├── login.php
│   │   ├── register.php
│   │   ├── goals.php
│   │   ├── workouts.php
│   │   └── ...
│   └── config.php                        # Database configuration
├── bat files/                            # Windows batch scripts for setup
├── md_files/                             # Documentation files
├── gradle/                               # Gradle wrapper and dependencies
├── database_*.sql                        # Database schema and migrations
├── build.gradle.kts                      # Project build configuration
└── README.md                             # This file
```

## Quick start hehe

Requirements:
- Android Studio (Arctic Fox or later recommended)
- Java / JDK compatible with the Gradle toolchain used (see `gradle/wrapper/`)
- Android SDK (set `sdk.dir` in your local `local.properties` or via Android Studio)

To open and run the app:

1. Open the project in Android Studio.
2. Let Gradle sync and download dependencies.
3. Build and run on an emulator or device.

The app module is at `app/` and standard Gradle tasks work, e.g.:

    ./gradlew assembleDebug

On Windows (cmd.exe) use `gradlew.bat assembleDebug`.

## Branching / Git notes
- The repository default branch is `main`. Local development changes were migrated to `main`.
- I created a temporary branch `tmp-sync-master` during migration; it has been pushed to `origin` and can be used for a Pull Request if needed.

If you see an old `master` branch on remote and you are the repository admin and want to remove it, do so carefully (update remote default branch settings first).

## Security / Secrets
- Do NOT commit `local.properties` (contains SDK path) or any keystore files (`*.jks`, `*.keystore`). These are listed in `.gitignore`.
- If you have API keys or DB credentials in `php_api/config.php`, move them to environment variables or a non-committed `config.local.php` and commit a `config.php.example` with placeholders.

## .gitignore
This repo contains a `.gitignore` configured to exclude build outputs, IDE files, OS metadata, keystore files, and other local artifacts. Review it before committing.

## Contributing
- Create a branch from `main`, make changes, and open a Pull Request targeting `main`.
- Run and test on at least one emulator or device before submitting.

## Contact / Issues
If you find issues or need help with setup, create an issue in the repository describing your environment and steps to reproduce.

---
Generated README — edit as needed to add more detailed setup, development, or contribution sections.
