package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


//달리기 옵션 설정 화면 담당 엑티비티
class RunningSelectActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.running_select)

        //사용자가 전 화면에서 선택한 최대볼륨
        val userSelectVolume = intent.getIntExtra("volumeValue",0)
        val userSelectLowVolume = intent.getIntExtra("lowVolumeValue",0)
        println(userSelectVolume)
        println(userSelectLowVolume)

        //각 버튼 얻어오기
        val slowRunner = findViewById<Button>(R.id.slow_runner) //조금느리게뛰기
        val reqularRunner = findViewById<Button>(R.id.running_selector) //평균속도
        val fastRunner = findViewById<Button>(R.id.fastest_runner) //전력질주

        //느리게 뛰기 속도 기준값 : 6km/h
        val slowSpeed = 6.0f
        //평균 속도 기준값 : 9km/h
        val regularSpeed = 9.0f
        //전력질주 속도 기준값 : 12km/h
        val fastSpeed = 12.0f

        //속도측정 엑티비티로 넘어가주는 인텐트 생성
        val intent = Intent(this, RunningStartActivity::class.java)



        //각 버튼 클릭마다 속도 기준값 담아서 보내기
        slowRunner.setOnClickListener{
            intent.putExtra("speedValue",slowSpeed)
            intent.putExtra("userSelectVolume",userSelectVolume)
            intent.putExtra("userSelectLowVolume",userSelectLowVolume)
            startActivity(intent)
        }

        reqularRunner.setOnClickListener {
            intent.putExtra("speedValue",regularSpeed)
            intent.putExtra("userSelectVolume",userSelectVolume)
            intent.putExtra("userSelectLowVolume",userSelectLowVolume)
            startActivity(intent)
        }

        fastRunner.setOnClickListener {
            intent.putExtra("speedValue",fastSpeed)
            intent.putExtra("userSelectVolume",userSelectVolume)
            intent.putExtra("userSelectLowVolume",userSelectLowVolume)
            startActivity(intent)
        }

      
    }








}