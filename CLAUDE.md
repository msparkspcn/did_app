# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Android DID (Digital Information Display / Digital Signage) player app. Renders multi-zone dynamic content (images, videos, text) on display devices with real-time updates via WebSocket.

Package: `com.secta9ine.didapp`

## Build & Test Commands

```bash
# Build
./gradlew assembleDebug
./gradlew assembleRelease

# Install to device
./gradlew installDebug

# Tests
./gradlew test                      # Unit tests
./gradlew testDebugUnitTest         # Debug unit tests only
./gradlew connectedAndroidTest      # Instrumented tests (requires device/emulator)

# Mock server (for development)
cd mock-server && npm install && npm start   # Runs on port 8080
```

## Architecture

**MVVM + Repository pattern** with Hilt DI, Jetpack Compose UI, Room local DB, Retrofit + WebSocket networking.

### V2 패키지 (미사용 — 참고용)

`v2/` 패키지는 외부 API/WebSocket 연동 전에 웹, WS, API를 직접 구현하여 테스트한 코드입니다. **현재 사용하지 않으며**, 향후 외부 API 연동 시 구조 참고용으로 코드를 남겨두고 있습니다 (주석 또는 미사용 상태).

포함 내용: `v2/contract/` (DTO), `v2/data/remote/` (Retrofit, WebSocket), `v2/data/local/` (Room), `v2/repository/`, `v2/ui/` (멀티존 렌더러)

### Key Packages (활성)

- `data/` - V1 데이터 레이어 (Repository, Room DB, Retrofit, WebSocket/STOMP)
- `ui/MainActivity.kt` - 키오스크 전체 화면 + Stage 기반 UI 상태 머신
- `ui/viewmodel/DidViewModel.kt` - 디바이스 인증 흐름, WebSocket 이벤트 처리, 네트워크 복구
- `ui/components/` - `VideoContent` (ExoPlayer), `ImageContent` (Coil), `TextContent`
- `di/AppModule` - Hilt module (DB, APIs, OkHttpClient with JWT interceptor)
- `system/` - Crash recovery (`AppRelaunchScheduler`), boot receiver, kiosk mode, QuberAgentManager

### Device Access Flow

1. Check device status → if NOT_FOUND, auto-register with approval code
2. Wait for admin approval via WebSocket (`Activated` event)
3. On authenticated: connect WebSocket for real-time events (screen update, queue call, power control, etc.)
4. Offline fallback: local DB에 인증 이력 있으면 OFFLINE_ACTIVE, 없으면 SERVER_UNREACHABLE
5. Network 복구 시 자동 재인증

## Environment

- Android SDK 34, minSdk 24, Java/Kotlin 17
- Compose compiler 1.5.8, Compose BOM 2024.04.01
- Hilt 2.48, Room 2.6.0, Retrofit 2.9.0, Media3/ExoPlayer 1.2.0
- Dev API base URL: `http://10.212.44.212:8080/api/` (hardcoded in `AppModule.kt`)
- JWT dev token hardcoded in `AppModule.kt` interceptor
- Java home: sdkman Java 17.0.17-tem (`gradle.properties`)
- Cleartext traffic enabled for dev (`AndroidManifest.xml`)

## Mock Server

Node.js Express server at `mock-server/server.js`. Stores data in `snapshot-store.json` and `device-store.json`. Admin endpoints at `/api/admin/*`. WebSocket at `/ws/player-snapshot`. Static admin UI at `/admin`.

## Testing

Unit tests use **MockK** for mocking + **kotlinx-coroutines-test**. Test location: `app/src/test/`. Run a single test class: `./gradlew test --tests "com.secta9ine.didapp.data.repository.DidRepositoryTest"`.
