package com.secta9ine.didapp.v2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.secta9ine.didapp.ui.components.ImageContent
import com.secta9ine.didapp.ui.components.TextContent
import com.secta9ine.didapp.ui.components.VideoContent
import com.secta9ine.didapp.v2.contract.AssetDto
import com.secta9ine.didapp.v2.contract.AssetType
import com.secta9ine.didapp.v2.contract.CoordinateSystem
import com.secta9ine.didapp.v2.contract.LayoutDto
import com.secta9ine.didapp.v2.contract.PlayerSnapshotDto
import com.secta9ine.didapp.v2.contract.ZoneDto
import kotlinx.coroutines.delay

@Composable
fun DynamicDidScreenV2(
    snapshot: PlayerSnapshotDto,
    modifier: Modifier = Modifier,
    showZoneDebugBorder: Boolean = false
) {
    Box(modifier = modifier.fillMaxSize()) {
        snapshot.layout.zones
            .sortedBy { it.zIndex }
            .forEach { zone ->
                val zoneItems = snapshot.zonePlaylists[zone.id].orEmpty()
                val assets = zoneItems.mapNotNull { item -> snapshot.assets[item.assetId] }
                ZoneViewport(
                    layout = snapshot.layout,
                    zone = zone,
                    assets = assets,
                    showZoneDebugBorder = showZoneDebugBorder
                )
            }
    }
}

@Composable
private fun ZoneViewport(
    layout: LayoutDto,
    zone: ZoneDto,
    assets: List<AssetDto>,
    showZoneDebugBorder: Boolean
) {
    val config = LocalConfiguration.current
    val screenWidthPx = config.screenWidthDp.toFloat()
    val screenHeightPx = config.screenHeightDp.toFloat()

    val xDp: Float
    val yDp: Float
    val widthDp: Float
    val heightDp: Float
    if (layout.coordinateSystem == CoordinateSystem.CANVAS_PIXEL) {
        val canvasWidth = layout.canvas.width.coerceAtLeast(1).toFloat()
        val canvasHeight = layout.canvas.height.coerceAtLeast(1).toFloat()
        xDp = screenWidthPx * (zone.x / canvasWidth)
        yDp = screenHeightPx * (zone.y / canvasHeight)
        widthDp = screenWidthPx * (zone.width / canvasWidth)
        heightDp = screenHeightPx * (zone.height / canvasHeight)
    } else {
        xDp = screenWidthPx * zone.x
        yDp = screenHeightPx * zone.y
        widthDp = screenWidthPx * zone.width
        heightDp = screenHeightPx * zone.height
    }

    var currentIndex by remember(zone.id, assets) { mutableIntStateOf(0) }

    if (assets.isNotEmpty() && currentIndex !in assets.indices) {
        currentIndex = 0
    }
    val currentAsset = assets.getOrNull(currentIndex)

    LaunchedEffect(zone.id, currentIndex, currentAsset?.id) {
        if (currentAsset != null && currentAsset.type != AssetType.VIDEO) {
            val durationSec = (currentAsset.defaultDurationSec ?: 10).coerceAtLeast(1)
            delay(durationSec * 1000L)
            currentIndex = (currentIndex + 1) % assets.size
        }
    }

    var zoneModifier = Modifier
        .offset(x = xDp.dp, y = yDp.dp)
        .size(width = widthDp.dp, height = heightDp.dp)
        .clipToBounds()
        .background(parseColor(zone.backgroundHex))
    if (showZoneDebugBorder) {
        zoneModifier = zoneModifier.border(2.dp, Color.Yellow)
    }

    Box(modifier = zoneModifier) {
        currentAsset?.let { asset ->
            when (asset.type) {
                AssetType.IMAGE -> ImageContent(
                    contentUrl = asset.source,
                    localPath = asset.localPath,
                    modifier = Modifier.fillMaxSize()
                )
                AssetType.VIDEO -> VideoContent(
                    contentUrl = asset.source,
                    localPath = asset.localPath,
                    loop = assets.size == 1,
                    modifier = Modifier.fillMaxSize(),
                    onVideoEnd = {
                        currentIndex = (currentIndex + 1) % assets.size
                    }
                )
                AssetType.TEXT -> TextContent(text = asset.source, modifier = Modifier.fillMaxSize())
                AssetType.PRODUCT -> TextContent(text = asset.source, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

private fun parseColor(hex: String?): Color {
    if (hex.isNullOrBlank()) return Color.Transparent
    return runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(Color.Transparent)
}
