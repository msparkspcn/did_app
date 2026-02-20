package com.example.didapp.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.didapp.data.local.DidEntity
import com.example.didapp.ui.components.ImageContent
import com.example.didapp.ui.components.TextContent
import com.example.didapp.ui.components.VideoContent
import com.example.didapp.ui.viewmodel.DidViewModel
import kotlinx.coroutines.delay

@Composable
fun DidScreen(viewModel: DidViewModel) {
    val items by viewModel.didItems.collectAsState()

    if (items.isEmpty()) {
        TextContent(text = "No content available")
    } else {
        var currentIndex by remember { mutableIntStateOf(0) }
        
        // Auto-rolling logic
        LaunchedEffect(items) {
            if (items.isNotEmpty()) {
                while (true) {
                    delay(10000) // 5 seconds display
                    currentIndex = (currentIndex + 1) % items.size
                }
            }
        }

        AnimatedContent(
            targetState = currentIndex,
            transitionSpec = {
                // Rolling Up Effect: Next item comes from bottom, current item exits top
                slideInVertically { height -> height } + fadeIn() togetherWith
                slideOutVertically { height -> -height } + fadeOut()
            },
            label = "ContentRolling",
            modifier = Modifier.fillMaxSize()
        ) { index ->
            val item = items[index]
            ContentItem(item)
        }
    }
}

@Composable
fun ContentItem(item: DidEntity) {
    when (item.type) {
        "IMAGE" -> ImageContent(contentUrl = item.content, localPath = item.localPath)
        "VIDEO" -> VideoContent(contentUrl = item.content, localPath = item.localPath)
        "TEXT" -> TextContent(text = item.content)
    }
}
