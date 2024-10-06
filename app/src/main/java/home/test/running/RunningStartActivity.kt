package home.test.running

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


//센서, GPS로 속도 측정처리 하는 엑티비티
//속도 측정 완료 시 속도 옵션별로 기준 속도를 넘겨 받아서 미디어 볼륨 조절하도록 수정 예정
class RunningStartActivity : AppCompatActivity(), SensorEventListener, LocationListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var locationManager: LocationManager
    private lateinit var speedTextView: TextView

    private var lastTimestamp: Long = 0
    private var speed: Float = 0f // 현재 속도 (m/s)
    private var isMoving: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.running_start)

        speedTextView = findViewById(R.id.speedchecktext)

        // Sensor setup
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Location setup
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        locationManager.removeUpdates(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            if (lastTimestamp > 0) {
                val timeInterval = (currentTime - lastTimestamp) / 1000.0f
                val accelerationX = event.values[0]
                val accelerationY = event.values[1]
                val accelerationZ = event.values[2]

                // 속도 계산
                speed += Math.sqrt((accelerationX * accelerationX + accelerationY * accelerationY + accelerationZ * accelerationZ).toDouble()).toFloat() * timeInterval

                // 이동 여부 판단
                isMoving = speed >= 0.5f // 0.5 m/s 이상의 움직임을 이동으로 간주
            }
            lastTimestamp = currentTime
        }
    }

    override fun onLocationChanged(location: Location) {
        // GPS 속도 계산 (m/s)
        val gpsSpeedInMps = location.speed

        // 최종 속도 업데이트
        if (isMoving) {
            // 가속도 센서와 GPS 속도를 합산
            speed = speed * 0.9f + gpsSpeedInMps * 0.1f // 가속도에 GPS 속도 반영 (부드럽게)
        } else {
            speed = gpsSpeedInMps // GPS 속도만 사용
        }

        // 속도 표시 (두번째 화면 텍스트)
        speedTextView.text = String.format("속도: %.2f km/h", speed * 3.6f) // km/h로 변환하여 출력

        // 속도 느려지면 줄어들도록 처리
        if (speed < 0.1f) {
            speed = 0f // 일정 속도 이하로 떨어지면 0으로 설정
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        locationManager.removeUpdates(this)
    }
}