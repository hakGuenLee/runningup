package home.test.running


import android.location.LocationManager
import android.os.Bundle
import android.content.Intent
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import home.test.running.databinding.FaceRunningBinding

//달리기 옵션 설정 화면 담당 엑티비티 (핵심기능)
//천천히 뛰기 : 8~9분 페이스
//적당히 뛰기 : 6~7분 페이스
//질주 : 5분 페이스
class FaceRunningActivity : AppCompatActivity(), TimeManager.TimerCallback, LocationTracker.LocationCallback {

    private lateinit var binding:FaceRunningBinding
    private lateinit var locationTracker: LocationTracker
    private lateinit var gestureDetector: GestureDetector
    private var timeManager : TimeManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = FaceRunningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //faceSelectActivity에서 넘겨준 목표 face 값 받기
        val selectFace = intent?.getStringExtra("face")
        binding.targetText.text = selectFace

        //locationTracker 생성/초기화
        locationTracker = LocationTracker(this, getSystemService(LOCATION_SERVICE) as LocationManager, this)
        //타이머 객체 생성
        timeManager = TimeManager(this)

        binding.viewFlipper.displayedChild = 0  // 0: face_running, 1: face_map, 2: face_counter

        // GestureDetector 초기화
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null) return false
                return handleSwipeGesture(e1, e2)
            }
        })

        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        //faceRunningController 버튼 클릭 시 달리기 시작, 타이머 제어.
        binding.faceRunningController.setOnClickListener {
            var status = binding.faceRunningController.text

            if(status == "START"){
                timeManager?.startTimer()
                locationTracker.startTracking()
                binding.faceRunningController.text = "STOP"
            }else if(status == "STOP"){
                timeManager?.stopTimer()
                binding.faceRunningController.text = "START"
            }
        }

        //FINISH 버튼 클릭 시 결과 화면으로 넘어가도록 수정
        binding.faceRunningStopper.setOnClickListener {
            timeManager?.stopAllTimer()
            locationTracker.stopTracking()
            val intent = Intent(this, FaceResultActivity::class.java)
            startActivity(intent)
        }
    }





    //스와이프 처리 메서드
    private fun handleSwipeGesture(e1: MotionEvent, e2: MotionEvent): Boolean {
        val diffX = e2.x - e1.x

        // 수평 방향 스와이프만 처리
        if (Math.abs(diffX) > Math.abs(e2.y - e1.y)) {
            when (binding.viewFlipper.displayedChild) {
                0 -> {
                    // 첫 번째 화면(0, 달리기 실행 화면)에서는 좌우 모두 가능
                    if (diffX > 0) {
                        // 오른쪽 스와이프
                        binding.viewFlipper.setInAnimation(this, R.anim.slide_left_in)
                        binding.viewFlipper.showPrevious()
                    } else {
                        // 왼쪽 스와이프
                        binding.viewFlipper.setInAnimation(this, R.anim.slide_right_in)
                        binding.viewFlipper.showNext()
                    }
                }
                1 -> {
                    // 두 번째 화면(1, 측청치 화면)에서는 오른쪽 스와이프만 가능
                    if (diffX > 0) {
                        binding.viewFlipper.setInAnimation(this, R.anim.slide_left_in)
                        binding.viewFlipper.setDisplayedChild(0)
                    }
                }
                2 -> {
                    // 세 번째 화면(2, 맵 화면)에서는 왼쪽 스와이프만 가능
                    if (diffX < 0) {
                        binding.viewFlipper.setInAnimation(this, R.anim.slide_right_in)
                        binding.viewFlipper.setDisplayedChild(0)
                    }
                }
            }
            return true
        }
        return false
    }




    override fun onLocationUpdated(distance: Double, pace: String) {
        binding.runningFaceText.text = pace
        binding.distance.text = String.format("%.1f km", distance)
    }

    override fun onTick(time: String) {
        binding.timer.text = time
    }

    override fun onTimerStop() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
//        stopLocationUpdates()
        locationTracker.stopTracking()
    }


}