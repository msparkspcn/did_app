package com.secta9ine.didapp.v2.contract

// LEGACY (v1) response shape kept as comment for rollback reference:
// data class DidResponse(val items: List<DidItemDto>)
// data class DidItemDto(val id: String, val type: String, val content: String)

data class PlayerSnapshotDto(
    val version: Long,
    val validFromEpochSec: Long? = null,
    val validToEpochSec: Long? = null,
    val layout: LayoutDto,
    val zonePlaylists: Map<String, List<ZonePlaylistItemDto>>,
    val assets: Map<String, AssetDto>
)

data class LayoutDto(
    val id: String,
    val canvas: CanvasDto = CanvasDto(1920, 1080),
    val coordinateSystem: CoordinateSystem = CoordinateSystem.CANVAS_PIXEL,
    val zones: List<ZoneDto>
)

data class CanvasDto(
    val width: Int,
    val height: Int
)

data class ZoneDto(
    val id: String,
    // Coordinates are interpreted by LayoutDto.coordinateSystem.
    // CANVAS_PIXEL: x/y/width/height are absolute editor-canvas pixels.
    // RELATIVE_RATIO: x/y/width/height are 0.0..1.0 ratios.
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val zIndex: Int = 0,
    val backgroundHex: String? = null,
    val fitMode: FitMode = FitMode.COVER
)

data class ZonePlaylistItemDto(
    val assetId: String,
    val order: Int,
    val durationSec: Int? = null,
    val transition: TransitionType = TransitionType.NONE
)

data class AssetDto(
    val id: String,
    val type: AssetType,
    val source: String,
    val localPath: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val defaultDurationSec: Int? = null
)

enum class AssetType {
    IMAGE,
    VIDEO,
    TEXT
}

enum class FitMode {
    COVER,
    CONTAIN,
    STRETCH
}

enum class TransitionType {
    NONE,
    FADE,
    SLIDE_UP
}

enum class CoordinateSystem {
    CANVAS_PIXEL,
    RELATIVE_RATIO
}
