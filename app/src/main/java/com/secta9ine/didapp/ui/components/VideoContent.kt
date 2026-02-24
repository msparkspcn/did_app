package com.secta9ine.didapp.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
fun VideoContent(
    contentUrl: String,
    localPath: String?,
    onVideoEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videoUri = localPath ?: contentUrl
    val latestOnVideoEnd = rememberUpdatedState(onVideoEnd)

    val exoPlayer = remember(videoUri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_OFF

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        latestOnVideoEnd.value()
                    }
                }
            })
        }
    }

    val playerView = remember(exoPlayer) {
        PlayerView(context).apply {
            player = exoPlayer
            useController = false
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    AndroidView(
        factory = { playerView },
        update = { it.player = exoPlayer },
        modifier = modifier
    )

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
}
