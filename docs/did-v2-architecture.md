# DID v2 Multi-Zone Design

## 1) Legacy v1 (commented reference)

```kotlin
// data class DidResponse(val items: List<DidItemDto>)
// data class DidItemDto(val id: String, val type: String, val content: String)
// @Entity(tableName = "did_items")
// data class DidEntity(val id: String, val type: String, val content: String, ...)
// Single-zone renderer: one item fills full screen and rotates sequentially.
```

## 2) Target v2 model

- `assets`: reusable content metadata (`image`, `video`, `text`)
- `layout`: canvas + zones (`x/y/width/height` as editor-canvas absolute pixels)
- `coordinateSystem`: `CANVAS_PIXEL`(권장) / `RELATIVE_RATIO`(하위호환)
- `zonePlaylists`: per-zone playback sequence and duration overrides
- `snapshot(version)`: player receives full state in one payload

## 3) Backend storage shape (recommended)

- `asset`
  - `id`, `type`, `source`, `metadata_json`, `default_duration_sec`
- `layout_template`
  - `id`, `name`, `canvas_width`, `canvas_height`, `updated_at`
- `layout_zone`
  - `id`, `layout_id`, `zone_key`, `x`, `y`, `width`, `height`, `z_index`, `bg_hex`, `fit_mode`
- `playlist`
  - `id`, `layout_id`, `name`, `priority`, `start_at`, `end_at`, `status`
- `playlist_zone_item`
  - `id`, `playlist_id`, `zone_key`, `asset_id`, `sort_order`, `duration_sec`, `transition`
- `player_assignment`
  - `player_id`, `playlist_id`, `version`, `updated_at`

## 4) Player API contract

- `GET /players/{playerId}/snapshot`
- response fields:
  - `version`
  - `layout`
  - `zonePlaylists`
  - `assets`
  - `validFromEpochSec`, `validToEpochSec`

## 5) Player rendering rules

- Render zones sorted by `zIndex`.
- Each zone has independent playback cursor.
- `IMAGE/TEXT`: timer-based next.
- `VIDEO`: `onVideoEnd` next.
- If snapshot fetch fails, keep last successful snapshot and continue playback.

## 6) Rollout plan

- Phase A: introduce v2 DTO/DB tables in parallel with v1.
- Phase B: backend serves v2 snapshot for selected players.
- Phase C: app switches renderer feature-flag (`v2_enabled`).
- Phase D: remove v1 after stable operations.
