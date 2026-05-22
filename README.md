# CoolFan

An Android fan simulator built with Jetpack Compose. Features multiple fan styles, button skins, and color themes — all animated in real time.

## Features

### Fan Styles
| Style | Description |
|---|---|
| **Vintage** | 3-blade fan with wire grill and radial-gradient blades |
| **Modern** | 5-blade sleek fan with a minimal ring grill |
| **Dyson** | Bladeless ring fan with animated airflow markers |
| **Cute** | 4-blade fan with a smiley-face hub |

### Controls
- **Speed** — Off / 1 / 2 / 3 (blade revolution: 1.2 s / 0.6 s / 0.3 s per rotation)
- **Swing** — Toggles ±45° sinusoidal head oscillation (4 s period). Turning off smoothly returns the head to center.

### Customization
- **4 button styles** — Dial, Neon, Minimal, Physical
- **6 color themes** — Neon, Pastel, Metal, Wood, Cool Blue, Sky (all transitions are animated)

## Requirements

- Android 8.0+ (API 26)
- JDK 11+ to build (see note below)

## Building

The system Java may default to Java 8, which is incompatible with AGP 8.x. Prefix Gradle commands with the correct `JAVA_HOME`:

```bash
JAVA_HOME="C:/Program Files/Java/jdk-24" PATH="C:/Program Files/Java/jdk-24/bin:$PATH" ./gradlew assembleDebug
JAVA_HOME="C:/Program Files/Java/jdk-24" PATH="C:/Program Files/Java/jdk-24/bin:$PATH" ./gradlew installDebug
```

## Tech Stack

- **Kotlin** 2.0.21
- **Jetpack Compose** (BOM 2024.09.00) — UI and animation
- **Material3** — theming primitives
- **Canvas API** — all fan graphics drawn procedurally (no image assets)
- **`withFrameNanos`** — frame-locked animation loop for immediate speed response

## Project Structure

```
app/src/main/java/com/odom/coolfan/
├── model/FanModels.kt          — enums + data classes + theme color maps
├── util/AnimationUtils.kt      — FanSpeed → rotation duration
├── ui/
│   ├── FanAppScreen.kt         — root composable, holds all state
│   └── components/
│       ├── FanCanvas.kt        — single frame loop driving blade + swing animation
│       ├── ControlPanel.kt     — speed buttons + swing toggle
│       ├── StyleSelector.kt    — 3-tab customization panel
│       ├── CustomButton.kt     — 4 button style variants
│       └── fan/
│           ├── VintageFan.kt
│           ├── ModernFan.kt
│           ├── BladelessFan.kt
│           └── CuteFan.kt
```
