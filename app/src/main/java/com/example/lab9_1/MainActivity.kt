
package com.example.lab9_1

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

class MainActivity : AppCompatActivity() {

    // 兔子與烏龜的目前進度
    private var progressRabbit = 0
    private var progressTurtle = 0

    // 是否已經有動物抵達終點
    private var raceFinished = false

    // 綁定畫面元件
    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    // 兔子偷懶的機率
    private val sleepProbability = arrayOf(true, true, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // 綁定 XML 元件
        btnStart = findViewById(R.id.btnStart)
        sbRabbit = findViewById(R.id.sbRabbit)
        sbTurtle = findViewById(R.id.sbTurtle)

        // 設定開始按鈕
        btnStart.setOnClickListener {
            startRace()
        }
    }

    // 開始比賽
    private fun startRace() {

        // 避免重複開始
        btnStart.isEnabled = false

        // 重設比賽資料
        progressRabbit = 0
        progressTurtle = 0
        raceFinished = false

        // 重設進度條
        sbRabbit.progress = 0
        sbTurtle.progress = 0

        // 使用 Coroutine 執行兩個動物的賽跑
        lifecycleScope.launch {

            coroutineScope {

                // 兔子開始跑
                launch {
                    runRabbit()
                }

                // 烏龜開始跑
                launch {
                    runTurtle()
                }
            }

            // 比賽結束後重新開放按鈕
            btnStart.isEnabled = true
        }
    }

    // 兔子賽跑
    private suspend fun runRabbit() {

        while (progressRabbit < 100 && !raceFinished) {

            // 延遲 0.1 秒
            delay(100)

            // 兔子有三分之二的機率偷懶
            if (sleepProbability.random()) {
                delay(300)
            }

            // 檢查是否已經有其他動物獲勝
            if (raceFinished) break

            // 兔子每次前進三步
            progressRabbit = (progressRabbit + 3).coerceAtMost(100)

            // 更新畫面
            sbRabbit.progress = progressRabbit

            // 判斷兔子是否抵達終點
            if (progressRabbit >= 100 && !raceFinished) {
                raceFinished = true
                showToast("兔子勝利！")
            }
        }
    }

    // 烏龜賽跑
    private suspend fun runTurtle() {

        while (progressTurtle < 100 && !raceFinished) {

            // 延遲 0.1 秒
            delay(100)

            // 檢查是否已經有其他動物獲勝
            if (raceFinished) break

            // 烏龜每次前進一步
            progressTurtle = (progressTurtle + 1).coerceAtMost(100)

            // 更新畫面
            sbTurtle.progress = progressTurtle

            // 判斷烏龜是否抵達終點
            if (progressTurtle >= 100 && !raceFinished) {
                raceFinished = true
                showToast("烏龜勝利！")
            }
        }
    }

    // 顯示 Toast
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
