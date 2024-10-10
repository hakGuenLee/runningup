package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.provider.MediaStore.Audio
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import org.w3c.dom.Text


//볼륨 설정 화면 담당 Activity
class VolumeSelectActivity : AppCompatActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var volumeSelectBar: SeekBar
    private lateinit var lowVolumeSelectBar: SeekBar
    private lateinit var volumeValueText: TextView
    private lateinit var lowVolumeText: TextView
    private lateinit var volumeSetupBtn: Button
    private lateinit var ringtone: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.volume_select)

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        volumeSelectBar = findViewById(R.id.volumeSelectBar)
        lowVolumeSelectBar = findViewById(R.id.lowVolumeSelectBar)

        volumeValueText = findViewById(R.id.volumeValueText)
        lowVolumeText = findViewById(R.id.lowVolumeValueText)

        volumeSetupBtn = findViewById(R.id.volumeSetupBtn)

        //현재 기기의 최대 볼륨 얻기
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        volumeSelectBar.max = maxVolume
        lowVolumeSelectBar.max = maxVolume


        //휴대폰 기본 벨소리 매니저 객체 얻어오기
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)


        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        volumeSelectBar.progress = currentVolume

        // 최대볼륨바의 변경 리스너 설정
        volumeSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 현재 볼륨을 조정하고 벨소리 재생
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                volumeValueText.text = "설정한 최대 볼륨: $progress"
                ringtone.play() // 벨소리 재생
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 터치를 시작했을 때
                ringtone.play()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 터치를 멈췄을 때
                ringtone.stop() // 터치 멈출 시 벨소리 정지
            }
        })

        //최소볼륨바의 변경 리스너 설정
        lowVolumeSelectBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 현재 볼륨을 조정하고 벨소리 재생
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                lowVolumeText.text = "설정한 최소 볼륨: $progress"
                ringtone.play() // 벨소리 재생
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 터치를 시작했을 때
                ringtone.play()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 사용자가 터치를 멈췄을 때
                ringtone.stop() // 터치 멈출 시 벨소리 정지
            }
        })

        // 버튼 클릭 시 볼륨 값 전달
        volumeSetupBtn.setOnClickListener {
            println("btnClick!!")
            val volumeValue = volumeSelectBar.progress
            val lowVolumeValue = lowVolumeSelectBar.progress
            println(volumeValue)
            println(lowVolumeValue)
            val intent = Intent(this, RunningSelectActivity::class.java).apply {
                putExtra("volumeValue", volumeValue)
                putExtra("lowVolumeValue",lowVolumeValue)
            }
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone.stop() // 액티비티 종료 시 벨소리 정지
    }


    }







