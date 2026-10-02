# BOT2000

Android 13 MVP inspired by Pyla RL, built as a clean Kotlin/Jetpack Compose project scaffold.

## Project status

This repository now contains a working Android app skeleton for a basic BOT2000 experience:

- Android 13 target configuration
- Jetpack Compose base UI
- chat assistant panel
- embedded terminal simulator
- floating panel overlay service
- minimal permissions and app manifest

## Structure

- `app/src/main/java/com/bot2000/app/MainActivity.kt` – main UI screen
- `app/src/main/java/com/bot2000/app/FloatingPanelService.kt` – overlay service
- `app/src/main/res/layout/floating_panel.xml` – floating panel layout
- `app/build.gradle.kts` – app dependencies and configuration

## Notes

This is a functional foundation for the requested adaptation, not a full external clone of Pyla-RL. It keeps the same overall style direction — compact terminal + AI assistant + floating control surface — while staying Android-native and safe for a basic MVP.

## Build

On a machine with the Android SDK installed:

```bash
./gradlew assembleDebug
```

Or open the project in Android Studio and build the APK from there.
