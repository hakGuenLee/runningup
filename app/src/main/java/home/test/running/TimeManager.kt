package home.test.running

import android.os.Handler
import java.util.concurrent.TimeUnit


//타이머 객체
class TimeManager(private val callback: TimerCallback) {

    private var isTimerRunning = false
    private var elapsedTime: Long = 0
    private val handler = Handler()

    interface TimerCallback{
        fun onTick(time: String) // 타이머가 1초마다 호출하는 콜백
        fun onTimerStop() // 타이머 중지시 호출되는 콜백
    }

    //타이머 작업 수행 runnable
    private val runnable = object : Runnable {
        override fun run() {
            if (isTimerRunning) {
                elapsedTime += 1000 // 1초 증가
                updateTimerText()
                handler.postDelayed(this, 1000) // 1초 후에 다시 실행
            }
        }
    }

    //타이머 시작 메서드
    fun startTimer(){
        isTimerRunning = true
        handler.post(runnable)
    }

    //타이머 정지 메서드
    fun stopTimer(){
        isTimerRunning = false

    }

    // 타이머를 1초마다 업데이트하여 텍스트로 변환
    private fun updateTimerText() {
        val hours = TimeUnit.MILLISECONDS.toHours(elapsedTime)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
        val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        callback.onTick(timeFormatted) // onTick 콜백 호출
    }

    //FaceRunning에서 Finish 버튼을 누르고 결과페이지로 넘어갈때.
    //타이머 기능 종료
    fun stopAllTimer(){
        stopTimer()
        isTimerRunning = false
        handler.removeCallbacks(runnable)
        elapsedTime = 0
        updateTimerText()
    }





}