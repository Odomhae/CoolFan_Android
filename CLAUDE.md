# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
# Debug build
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Unit tests
./gradlew test

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Single unit test class
./gradlew test --tests "com.odom.coolfan.ExampleUnitTest"
```

- Min SDK: 26 (Android 8.0), Target SDK: 36
- Kotlin 2.0.21, Compose BOM 2024.09.00

## Architecture

Single-screen app with **State Hoisting**. All mutable state lives in `FanAppScreen` as a single `FanState` value; child composables receive state and fire callbacks upward.

```
FanAppScreen          ← holds FanState, animates ThemeColors
├── FanCanvas         ← frame-loop animation (LaunchedEffect + withFrameNanos)
│   ├── *FanBody      ← static: pole + base (never rotates)
│   └── *FanHead      ← rotates on Y-axis (swing) wrapping blade Z-rotation
├── ControlPanel      ← Speed buttons + Swing toggle
└── StyleSelector     ← 3-tab selector for Fan/Button/Theme customization
```

### Key Design Decisions

**Frame-based animation** — `FanCanvas` uses `LaunchedEffect(Unit)` with `withFrameNanos` instead of `infiniteTransition`, so blade speed reacts immediately to speed changes without restarting the animation.

**Body/Head split** — Each fan style has two composables: `*FanBody` (pole + base, always static) and `*FanHead` (grille + blades, subject to Y-axis swing rotation via `graphicsLayer { rotationY = swingAngle }`).

**Swing behavior** — ON: continuous 360° Y rotation (one revolution per 4 s). OFF: returns to 0° via shortest path at 2× speed.

**Theme color animation** — `FanAppScreen` calls `ThemeColors.animate()` (a private extension in `FanAppScreen.kt`) which animates each of the 5 color fields individually with `animateColorAsState`. The animated `ThemeColors` object is passed to all children, so color transitions ripple everywhere without recreating composables.

### Data Model (`model/FanModels.kt`)

```kotlin
enum class FanStyle   { VINTAGE, MODERN, BLADELESS, CUTE }
enum class ButtonStyle { ROUND_DIAL, NEON, MINIMAL, PHYSICAL }
enum class ColorTheme  { DARK_NEON, PASTEL, METAL, WOOD }
enum class FanSpeed(val rpm: Float) { OFF, LOW(1.2s/rev), MEDIUM(0.6s), HIGH(0.3s) }

data class FanState(speed, swinging, fanStyle, buttonStyle, colorTheme)
data class ThemeColors(background, frame, blade, accent, text)
```

`ColorTheme.toThemeColors()` maps each theme to its `ThemeColors`. `FanSpeed.rotationDurationMs()` lives in `util/AnimationUtils.kt`.

### Fan Style Files

Each style lives under `ui/components/fan/`:
- `VintageFan.kt` — Body + Head composables
- `ModernFan.kt`, `BladelessFan.kt`, `CuteFan.kt` — same pattern

### CustomButton Styles (`ui/components/CustomButton.kt`)

`ROUND_DIAL`, `NEON`, `MINIMAL`, `PHYSICAL` are rendered as separate private composables. Press scale animation (`animateFloatAsState`, 100 ms) is shared across all styles.

## Package

`com.odom.coolfan`
