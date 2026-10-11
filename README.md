# first-project

Android app shell for **Bowling Training**.

- Kotlin + Jetpack Compose (Material 3)
- System splash via `androidx.core:core-splashscreen`, followed by an animated
  "Bowling Training" splash screen
- Home screen score tracker: tap the pins knocked down each roll; a 10-frame
  scorecard shows strikes, spares and running totals, with undo and new game
  (scoring logic in `scoring/BowlingScorer.kt`)
- minSdk 26, targetSdk 37

## Build

Open the folder in Android Studio, or from the command line (JDK 17 + Android SDK required):

```
./gradlew assembleDebug
./gradlew test
```
