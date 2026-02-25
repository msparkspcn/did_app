package com.secta9ine.didapp.v2.mock

import com.secta9ine.didapp.v2.contract.AssetDto
import com.secta9ine.didapp.v2.contract.AssetType
import com.secta9ine.didapp.v2.contract.CanvasDto
import com.secta9ine.didapp.v2.contract.CoordinateSystem
import com.secta9ine.didapp.v2.contract.LayoutDto
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.contract.ZoneDto
import com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto

object MockSnapshots {
    fun threeZoneDemo(): PlayerSnapshotDto {
        return PlayerSnapshotDto(
            version = 1L,
            layout = LayoutDto(
                id = "layout_three_zone",
                canvas = CanvasDto(width = 1920, height = 1080),
                coordinateSystem = CoordinateSystem.CANVAS_PIXEL,
                zones = listOf(
                    ZoneDto(
                        id = "left",
                        x = 0f,
                        y = 0f,
                        width = 360f,
                        height = 1080f,
                        zIndex = 0,
                        backgroundHex = "#111111"
                    ),
                    ZoneDto(
                        id = "center",
                        x = 360f,
                        y = 0f,
                        width = 960f,
                        height = 1080f,
                        zIndex = 1,
                        backgroundHex = "#000000"
                    ),
                    ZoneDto(
                        id = "right",
                        x = 1320f,
                        y = 0f,
                        width = 600f,
                        height = 1080f,
                        zIndex = 0,
                        backgroundHex = "#111111"
                    )
                )
            ),
            zonePlaylists = mapOf(
                "left" to listOf(
                    ZonePlaylistItemDto(assetId = "video_left_1", order = 0, durationSec = 30)
                ),
                "center" to listOf(
                    ZonePlaylistItemDto(assetId = "image_center_1", order = 0, durationSec = 10),
                    ZonePlaylistItemDto(assetId = "image_center_2", order = 1, durationSec = 10),
                    ZonePlaylistItemDto(assetId = "text_center_1", order = 2, durationSec = 8)
                ),
                "right" to listOf(
                    ZonePlaylistItemDto(assetId = "video_right_1", order = 0, durationSec = 30)
                )
            ),
            assets = mapOf(
                "video_left_1" to AssetDto(
                    id = "video_left_1",
                    type = AssetType.VIDEO,
                    source = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                    defaultDurationSec = 20
                ),
                "video_right_1" to AssetDto(
                    id = "video_right_1",
                    type = AssetType.VIDEO,
                    source = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                    defaultDurationSec = 20
                ),
                "image_center_1" to AssetDto(
                    id = "image_center_1",
                    type = AssetType.IMAGE,
                    source = "https://images.unsplash.com/photo-1502134249126-9f3755a50d78?q=80&w=1920&h=1080&auto=format&fit=crop",
                    defaultDurationSec = 10
                ),
                "image_center_2" to AssetDto(
                    id = "image_center_2",
                    type = AssetType.IMAGE,
                    source = "https://images.unsplash.com/photo-1542281286-9e0a16bb7366?q=80&w=1920&h=1080&auto=format&fit=crop",
                    defaultDurationSec = 10
                ),
                "text_center_1" to AssetDto(
                    id = "text_center_1",
                    type = AssetType.TEXT,
                    source = "DID V2 Mock Snapshot: Left/Right Video + Center Image/Text Rotation",
                    defaultDurationSec = 8
                )
            )
        )
    }
}
