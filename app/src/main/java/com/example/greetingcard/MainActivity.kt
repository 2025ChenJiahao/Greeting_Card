package com.example.simplemoodtracker // 重要：请确保这行与你的项目包名一致

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

// 1. 定义心情数据模型
data class MoodEntry(val emoji: String, val time: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 开启全屏沉浸式体验
        enableEdgeToEdge()

        setContent {
            // 使用 Material Design 3 主题
            MaterialTheme {
                // Scaffold 帮我们处理好屏幕内边距（如躲开顶部的摄像头）
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MoodApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MoodApp(modifier: Modifier = Modifier) {
    // 2. 状态管理：使用 mutableStateOf 让 Compose 能够观察列表变化并自动刷新界面
    var moodList by remember { mutableStateOf(listOf<MoodEntry>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 标题
        Text(
            text = "我的心情日志",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 3. 表情选择区
        Text(text = "今天感觉如何？", style = MaterialTheme.typography.bodyLarge)

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val emojis = listOf("🌟", "😊", "😐", "😢", "😴")
            emojis.forEach { emoji ->
                Button(
                    onClick = {
                        // 获取当前时间
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        // 更新列表（创建新列表以触发界面重绘）
                        moodList = listOf(MoodEntry(emoji, currentTime)) + moodList
                    }
                ) {
                    Text(text = emoji, fontSize = 20.sp)
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // 4. 历史记录列表
        Text(
            text = "历史记录",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )

        // LazyColumn 相当于高效的滚动列表
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(moodList) { entry ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "感觉 ${entry.emoji}", fontSize = 18.sp)
                        Text(
                            text = entry.time,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}