# 선풍기 시뮬레이터 앱 개발 명세서

> Android / Jetpack Compose 기반 선풍기 동작 시각화 앱
> 사용자가 팬 모양, 버튼 스타일, 색상 테마를 자유롭게 조합하여 커스터마이징 가능

---

## 1. 프로젝트 개요

### 1.1 목적
실제 동작하는 선풍기 모습을 화면에 시각적으로 보여주는 앱. 단순 시연용이며, 실제 디바이스 제어 기능은 없음. 사용자가 다양한 스타일을 골라 자신만의 선풍기를 구성하는 재미 요소 포함.

### 1.2 핵심 기능
- 회전하는 선풍기 날개 애니메이션
- 속도 단계 조절 (Off / 1 / 2 / 3단)
- 좌우 회전(Swing) 토글
- **팬 모양 / 버튼 스타일 / 색상 테마 커스터마이징**
- 선택 즉시 실시간 미리보기

### 1.3 비기능 요구사항
- 부드러운 60fps 애니메이션
- 세로 모드 우선 지원
- Android 8.0 (API 26) 이상

---

## 2. 기술 스택

| 항목 | 선택 |
|------|------|
| 언어 | Kotlin |
| UI 프레임워크 | Jetpack Compose |
| 최소 SDK | API 26 (Android 8.0) |
| 타겟 SDK | 최신 안정 버전 |
| 빌드 도구 | Gradle (Kotlin DSL) |
| 그래픽 | Compose Canvas API |
| 애니메이션 | Compose Animation API (`animateFloatAsState`, `infiniteRepeatable` 등) |
| 아키텍처 | 단일 화면 + State Hoisting |

---

## 3. 화면 구성

```
┌─────────────────────────────┐
│        앱 타이틀           │
├─────────────────────────────┤
│                             │
│                             │
│      [ 선풍기 미리보기 ]    │  ← 선택된 스타일로 실시간 렌더링
│                             │
│                             │
├─────────────────────────────┤
│  Speed: [Off][1][2][3]      │  ← 동작 컨트롤
│  Swing: [ Toggle ]          │
├─────────────────────────────┤
│  [팬 모양] [버튼] [테마]    │  ← 커스터마이징 탭
│                             │
│   ○ 빈티지   ○ 모던         │
│   ○ 다이슨   ○ 큐트         │
└─────────────────────────────┘
```

---

## 4. 커스터마이징 옵션

### 4.1 팬 모양 (FanStyle)
| 값 | 설명 |
|------|------|
| `VINTAGE` | 빈티지/레트로 — 둥근 그릴, 3개 금속 날개, 둥근 받침대 |
| `MODERN` | 모던 미니멀 — 얇은 라인 그릴, 5개 슬림 날개 |
| `BLADELESS` | 다이슨 스타일 — 날개 없는 원형 링 구조 |
| `CUTE` | 귀여운/장난감 느낌 — 둥글둥글한 형태, 눈/표정 추가 가능 |

### 4.2 버튼 스타일 (ButtonStyle)
| 값 | 설명 |
|------|------|
| `ROUND_DIAL` | 둥근 다이얼/노브 형태 |
| `NEON` | 네온/글로우 효과 버튼 |
| `MINIMAL` | 납작한 미니멀 칩 |
| `PHYSICAL` | 물리 버튼 같은 입체감 (그림자 강조) |

### 4.3 색상 테마 (ColorTheme)
| 값 | 주요 색상 |
|------|------|
| `DARK_NEON` | 검정 배경 + 시안/마젠타 액센트 |
| `PASTEL` | 연한 핑크/민트/라벤더 |
| `METAL` | 메탈/실버톤 그레이스케일 |
| `WOOD` | 원목/베이지 따뜻한 톤 |

**총 조합: 4 × 4 × 4 = 64가지**

---

## 5. 데이터 모델

```kotlin
enum class FanStyle { VINTAGE, MODERN, BLADELESS, CUTE }
enum class ButtonStyle { ROUND_DIAL, NEON, MINIMAL, PHYSICAL }
enum class ColorTheme { DARK_NEON, PASTEL, METAL, WOOD }

enum class FanSpeed(val rpm: Float) {
    OFF(0f),
    LOW(1f),    // 1.2초/회전
    MEDIUM(2f), // 0.6초/회전
    HIGH(3f)    // 0.3초/회전
}

data class FanState(
    val speed: FanSpeed = FanSpeed.OFF,
    val swinging: Boolean = false,
    val fanStyle: FanStyle = FanStyle.VINTAGE,
    val buttonStyle: ButtonStyle = ButtonStyle.ROUND_DIAL,
    val colorTheme: ColorTheme = ColorTheme.DARK_NEON
)

data class ThemeColors(
    val background: Color,
    val frame: Color,
    val blade: Color,
    val accent: Color,
    val text: Color
)
```

---

## 6. 파일 구조

```
app/src/main/java/com/example/fanapp/
├── MainActivity.kt              # 앱 진입점
├── ui/
│   ├── FanAppScreen.kt          # 최상위 화면 컴포저블
│   ├── components/
│   │   ├── FanCanvas.kt         # 팬 렌더링 (스타일 분기)
│   │   ├── fan/
│   │   │   ├── VintageFan.kt    # 빈티지 팬 렌더링
│   │   │   ├── ModernFan.kt
│   │   │   ├── BladelessFan.kt
│   │   │   └── CuteFan.kt
│   │   ├── ControlPanel.kt      # Speed/Swing 컨트롤
│   │   ├── CustomButton.kt      # 스타일 분기 버튼
│   │   └── StyleSelector.kt     # 커스터마이징 탭/선택지
│   └── theme/
│       ├── Theme.kt             # ColorTheme → ThemeColors 매핑
│       └── Type.kt              # 폰트 정의
├── model/
│   └── FanModels.kt             # enum, data class
└── util/
    └── AnimationUtils.kt        # 회전 애니메이션 헬퍼
```

---

## 7. 주요 컴포저블 설계

### 7.1 FanAppScreen
- 최상위 State 보유 (`var fanState by remember { mutableStateOf(FanState()) }`)
- ThemeColors 매핑하여 하위로 전달
- FanCanvas, ControlPanel, StyleSelector 배치

### 7.2 FanCanvas
- `fanStyle`에 따라 VintageFan / ModernFan / BladelessFan / CuteFan 분기 호출
- 좌우 회전(swing)을 위해 `Modifier.graphicsLayer { rotationZ = swingAngle }` 적용
- swingAngle은 `infiniteTransition.animateFloat()`로 -25° ↔ 25° 왕복

### 7.3 각 팬 렌더링 (예: VintageFan)
- `Canvas(modifier)` 안에서 `drawCircle`, `drawArc`, `rotate {}` 등 활용
- 날개 회전: `infiniteTransition.animateFloat(0f, 360f, durationMillis = speed에 따라 가변)`
- speed가 OFF일 때 회전 정지, 변경 시 부드러운 가/감속 (`animateFloatAsState`)

### 7.4 ControlPanel
- Speed 버튼 4개 (CustomButton 재사용)
- Swing 토글 (Switch 또는 커스텀)
- `onSpeedChange`, `onSwingToggle` 콜백으로 상위 State 갱신

### 7.5 StyleSelector
- 3개 탭 (Fan / Button / Theme)
- 각 탭에서 enum 값 리스트를 가로 스크롤 가능한 칩 또는 카드로 표시
- 선택 시 `onFanStyleChange` 등 콜백 호출

### 7.6 CustomButton
- `buttonStyle` 파라미터에 따라 분기 렌더링
- ROUND_DIAL: `Box` + `drawCircle` + 그림자
- NEON: 글로우 효과 (블러, 색상 그라데이션)
- MINIMAL: 단순 `Surface` + 보더
- PHYSICAL: 입체 그림자 + 눌렸을 때 z축 이동

---

## 8. 애니메이션 명세

| 대상 | 방식 | 파라미터 |
|------|------|----------|
| 날개 회전 | `infiniteTransition.animateFloat(0f, 360f)` | LOW: 1200ms, MEDIUM: 600ms, HIGH: 300ms |
| 속도 변경 | `animateFloatAsState` | 300ms ease-in-out (부드러운 가/감속) |
| 좌우 회전 | `infiniteTransition.animateFloat(-25f, 25f)` | 4000ms, ease-in-out, reverse |
| 버튼 눌림 | `animateFloatAsState` (scale) | 100ms |
| 스타일 전환 | `AnimatedContent` 또는 `Crossfade` | 400ms |

---

## 9. 개발 단계

### Phase 1: 기본 구조 (1~2일)
- [ ] 프로젝트 셋업 (Compose, Material3)
- [ ] FanModels.kt (enum, data class) 정의
- [ ] FanAppScreen 골격 + State 관리
- [ ] VINTAGE 팬 1종만 Canvas로 구현 + 회전 애니메이션

### Phase 2: 컨트롤 (1일)
- [ ] Speed 버튼 동작 (속도별 회전 속도 변경)
- [ ] Swing 토글 (좌우 회전 애니메이션)
- [ ] 부드러운 가/감속 처리

### Phase 3: 커스터마이징 (2~3일)
- [ ] 나머지 3종 팬 디자인 구현 (MODERN, BLADELESS, CUTE)
- [ ] 4종 버튼 스타일 구현
- [ ] 4종 컬러 테마 적용
- [ ] StyleSelector UI 및 실시간 미리보기

### Phase 4: 마무리 (1일)
- [ ] 전환 애니메이션 다듬기
- [ ] 다양한 화면 크기 대응
- [ ] 앱 아이콘, 스플래시 등 마감

---

## 10. 확장 아이디어 (선택)

- 선택한 조합을 즐겨찾기로 저장 (DataStore)
- 바람 소리 효과 (속도별 다른 사운드)
- 햅틱 피드백 (버튼 누를 때 진동)
- 타이머 기능 (30분 후 자동 OFF)
- 화면 캡처 / 공유 기능

---

## 11. 참고 사항

- 모든 그래픽은 Canvas로 직접 그려 벡터 기반으로 확장성 확보
- 이미지 리소스 의존성 최소화 (아이콘 정도만 사용)
- 다크/라이트 모드는 색상 테마로 흡수 (DARK_NEON 등)
- 한 화면 안에서 모든 작업 완결 (네비게이션 없음)
