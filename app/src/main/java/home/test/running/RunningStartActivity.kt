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
import android.media.AudioManager


//속도 측정과 미디어 볼륨 조절하는 공통 화면&컴포넌트
class RunningStartActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private lateinit var speedTextView: TextView
    private lateinit var musicVolumeTextView: TextView
    private var lastLocation: Location? = null
    private val speedThreshold = 1.944f // 속도 임계값 (3.6을 곱했을 때 7km/h)
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_start)

        speedTextView = findViewById(R.id.speedchecktext)
        musicVolumeTextView = findViewById(R.id.music)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // 위치 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        }
    }

    override fun onLocationChanged(location: Location) {
        val speed = location.speed * 3.6f // m/s를 km/h로 변환
        speedTextView.text = String.format("현재 속도 : %.2f km/h", speed)

        // 속도에 따라 볼륨 조절
        musicVolumeController(speed)

        lastLocation = location
    }

    // 미디어 볼륨 조절 메소드
    private fun musicVolumeController(speed: Float) {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        // 속도가 0에 가까울수록 볼륨을 줄이고, 임계값 이상일 경우 볼륨을 높임
        val newVolume: Int = when {
            speed < speedThreshold -> 1 // 속도가 임계값 이하일 때 볼륨을 0으로
            speed < 5 -> maxVolume / 4 // 속도가 5km/h 미만일 때 볼륨을 최대 볼륨의 1/4로 설정.
            speed < 7 -> maxVolume / 2 // 속도가 7km/h 미만일 때, 최대 볼륨의 1/2로 설정.
            else -> maxVolume // 속도가 7km/h 이상일 때 최대 볼륨
        }

        // 볼륨 설정
        if (currentVolume != newVolume) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
            musicVolumeTextView.text = String.format("현재 볼륨 : %d", newVolume)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }
}