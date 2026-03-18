package com.secta9ine.didapp.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.secta9ine.didapp.system.QuberAgentManager
import com.secta9ine.didapp.ui.theme.DidAppTheme
import com.secta9ine.didapp.ui.viewmodel.DidViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var quberAgentManager: QuberAgentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        quberAgentManager.bind()

        setContent {

            DidAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: DidViewModel = hiltViewModel()
                    val uiState = viewModel.uiState.collectAsState().value

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        when (uiState.stage) {
                            DidViewModel.Stage.CHECKING_DEVICE -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(48.dp),
                                        color = Color.White,
                                        strokeWidth = 3.dp
                                    )
                                    Spacer(modifier = Modifier.height(32.dp))
                                    Text(
                                        text = "디바이스 상태 확인 중",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            DidViewModel.Stage.PENDING_APPROVAL -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "승인 대기 중",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(48.dp))
                                    Text(
                                        text = "인증코드",
                                        color = Color.Gray,
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = uiState.approvalCode ?: "",
                                        color = Color.White,
                                        fontSize = 72.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(48.dp))
                                    Text(
                                        text = "관리자 페이지에서 위 코드를 입력하여\n디바이스를 활성화해주세요",
                                        color = Color.Gray,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            DidViewModel.Stage.AUTHENTICATED -> {
                                AsyncImage(
                                    model = "https://picsum.photos/1920/1080",
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            DidViewModel.Stage.OFFLINE_ACTIVE -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "오프라인 모드",
                                        color = Color(0xFFFFB74D),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = "서버 연결 없이 동작 중\n연결 복구 시 자동 동기화됩니다",
                                        color = Color.Gray,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                // TODO: 마지막 스냅샷 기반 컨텐츠 표시
                            }

                            DidViewModel.Stage.SERVER_UNREACHABLE -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "서버 연결 필요",
                                        color = Color(0xFFFF6B6B),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = uiState.message ?: "서버에 연결할 수 없습니다.",
                                        color = Color.Gray,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(32.dp))
                                    Text(
                                        text = "네트워크 연결 후 자동으로 재시도합니다",
                                        color = Color.DarkGray,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            DidViewModel.Stage.ERROR -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "오류 발생",
                                        color = Color(0xFFFF6B6B),
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = uiState.message ?: "알 수 없는 오류가 발생했습니다.",
                                        color = Color.Gray,
                                        fontSize = 18.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        quberAgentManager.unbind()
        super.onDestroy()
    }
}
