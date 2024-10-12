package home.test.running

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager


//달리기 속도 측정과 볼륨 조절을 수행하는 공통화면.
//RunningService와 통신하면서 넘어오는 데이터를 화면에 출력.
class RunningStartActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var speedTextView: TextView
    private lateinit var musicVolumeTextView: TextView

    private val speedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val speed = intent?.getFloatExtra("speed", 0f) ?: 0f
            val adjustedVolume = intent?.getIntExtra("adjustedVolume", 0) ?: 0

            speedTextView.text = String.format("현재 속도 : %.2f km/h", speed)
            musicVolumeTextView.text = String.format("조절된 볼륨 : %d", adjustedVolume)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_start)

        speedTextView = findViewById(R.id.speedchecktext)
        musicVolumeTextView = findViewById(R.id.music)

        // RunningSelectActivity에서 받은 값들
        val userSelectVolumeValue = intent.getIntExtra("userSelectVolume", 0)
        val userSelectLowVolumeValue = intent.getIntExtra("userSelectLowVolume", 0)
        val speedThreshold = intent.getFloatExtra("speedValue", 0.0f)

        // 서비스 시작 시 인텐트에 값 추가
        val serviceIntent = Intent(this, RunningService::class.java).apply {
            putExtra("userSelectVolume", userSelectVolumeValue)
            putExtra("userSelectLowVolume", userSelectLowVolumeValue)
            putExtra("speedThreshold", speedThreshold)
        }
        startService(serviceIntent)

        // 위치 권한 요청
        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        }
    }

    override fun onStart() {
        super.onStart()
        // LocalBroadcastReceiver 등록
        LocalBroadcastManager.getInstance(this).registerReceiver(speedReceiver, IntentFilter("SPEED_UPDATE"))
    }

    override fun onStop() {
        super.onStop()
        // LocalBroadcastReceiver 해제
        LocalBroadcastManager.getInstance(this).unregisterReceiver(speedReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
        val serviceIntent = Intent(this, RunningService::class.java)
        stopService(serviceIntent)
    }

    // LocationListener 메서드 구현
    override fun onLocationChanged(location: Location) {
        // 위치 변화 시 처리
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // 상태 변경 시 처리
    }

    override fun onProviderEnabled(provider: String) {
        // 제공자가 활성화될 때 처리
    }

    override fun onProviderDisabled(provider: String) {
        // 제공자가 비활성화될 때 처리
    }
}