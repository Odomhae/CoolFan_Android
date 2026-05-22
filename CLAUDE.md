# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

Gradle requires JDK 11+. The system default may be Java 8, so set `JAVA_HOME` explicitly:

```bash
# Debug build
JAVA_HOME="C:/Program Files/Java/jdk-24" PATH="C:/Program Files/Java/jdk-24/bin:$PATH" ./gradlew assembleDebug

# Install on connected device
JAVA_HOME="C:/Program Files/Java/jdk-24" PATH="C:/Program Files/Java/jdk-24/bin:$PATH" ./gradlew installDebug

# Unit tests
JAVA_HOME="C:/Program Files/Java/jdk-24" PATH="C:/Program Files/Java/jdk-24/bin:$PATH" ./gradlew test

# Instrumented tests (requires connected device/emulator)
JAVA_HOME="C:/Program Files/Java/jdk-24" PATH="C:/Program Files/Java/jdk-24/bin:$PATH" ./gradlew connectedAndroidTest

# Single unit test class
JAVA_HOME="C:/Program Files/Java/jdk-24" PATH="C:/Program Files/Java/jdk-24/bin:$PATH" ./gradlew test --tests "com.odom.coolfan.ExampleUnitTest"
```

- Min SDK: 26 (Android 8.0), Target SDK: 36
- Kotlin 2.0.21, Compose BOM 2024.09.00

## Architecture

Single-screen app with **State Hoisting**. All mutable state lives in `FanAppScreen` as a single `FanState` value; child composables receive state and fire callbacks upward.

```
FanAppScreen          ← holds FanState, animates ThemeColors
├── FanCanvas         ← single frame-loop (LaunchedEffect + withFrameNanos)
│   ├── *FanBody      ← static: pole + base (never rotates)
│   └── *FanHead      ← swing Y-rotation wrapping blade Z-rotation
├── ControlPanel      ← Speed buttons + Swing toggle
└── StyleSelector     ← 3-tab selector for Fan/Button/Theme customization
```

### Key Design Decisions

**Single frame loop** — `FanCanvas` uses one `LaunchedEffect(Unit)` with `withFrameNanos` that drives both blade rotation and swing in the same tick. `rememberUpdatedState` is used for speed and swinging so changes take effect immediately without restarting the coroutine.

**Body/Head split** — Each fan style has two composables: `*FanBody` (pole + base, always static) and `*FanHead` (grille + blades, subject to Y-axis swing rotation via `graphicsLayer { rotationY = swingAngle }`). Inside each head, blades get an additional `rotationY` tilt derived from `sin(rotationAngle)` to create a propeller-depth illusion.

**Swing behavior** — ON: ±45° sinusoidal oscillation over 4 s, phase resets to 0 on each toggle-on. OFF: returns to 0° at a fixed rate (`RETURN_DEG_PER_MS`).

**Theme color animation** — `FanAppScreen` calls `ThemeColors.animate()` (a private extension in `FanAppScreen.kt`) which animates each of the 5 color fields individually with `animateColorAsState`. The animated `ThemeColors` is passed to all children so transitions ripple everywhere without recreating composables.

**Canvas sizing** — All drawing coordinates inside fan composables are relative to `r = minOf(size.width, size.height) * 0.42f`. Never use raw pixel literals; always derive dimensions from `r` so the UI scales correctly across screen densities.

### Data Model (`model/FanModels.kt`)

```kotlin
enum class FanStyle    { VINTAGE, MODERN, BLADELESS, CUTE }
enum class ButtonStyle { ROUND_DIAL, NEON, MINIMAL, PHYSICAL }
enum class ColorTheme  { DARK_NEON, PASTEL, METAL, WOOD, COOL_BLUE, SKY }
enum class FanSpeed    { OFF, LOW, MEDIUM, HIGH }  // durations in util/AnimationUtils.kt

data class FanState(speed, swinging, fanStyle, buttonStyle, colorTheme, selectedStyleTab)
data class ThemeColors(background, frame, blade, accent, text)
```

`ColorTheme.toThemeColors()` maps each theme to its `ThemeColors`. `FanSpeed.rotationDurationMs()` in `util/AnimationUtils.kt` returns ms-per-revolution (OFF→0, LOW→1200, MEDIUM→600, HIGH→300). `selectedStyleTab` in `FanState` holds the active tab index for `StyleSelector` (hoisted so it survives recomposition).

### Fan Style Files

Each style lives under `ui/components/fan/` as a `*FanBody` + `*FanHead` composable pair:
- `VintageFan.kt` — 3-blade, radial-gradient, wire grill
- `ModernFan.kt` — 5-blade, linear-gradient, ring grill
- `BladelessFan.kt` — Dyson-style ring with flow markers; no blade tilt effect
- `CuteFan.kt` — 4-blade with face hub and dot-decorated ring

### CustomButton Styles (`ui/components/CustomButton.kt`)

`ROUND_DIAL`, `NEON`, `MINIMAL`, `PHYSICAL` are rendered as separate private composables inside the file. Press scale animation (`animateFloatAsState`, 100 ms) is shared across all styles.

## Package

`com.odom.coolfan`
