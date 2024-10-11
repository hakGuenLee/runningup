package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat


//앱 실행 첫 화면
class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS = 1


//    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //권한 요청 호출
       permissionRequestChecker()

        //첫 화면 가운데 버튼 얻어오기
        val appStarter = findViewById<Button>(R.id.main_button)

        //첫 화면 버튼 클릭 시 다음 화면 전환
        appStarter.setOnClickListener{
            val intent = Intent(this, VolumeSelectActivity::class.java)
            startActivity(intent)
        }
    }



    //권한 허용 요청
//    @RequiresApi(Build.VERSION_CODES.Q)
    private fun permissionRequestChecker(){
        val permissionList = mutableMapOf<String, String>()
        permissionList["location"] = Manifest.permission.ACCESS_COARSE_LOCATION
        permissionList["fineLocation"] = Manifest.permission.ACCESS_FINE_LOCATION
        permissionList["audio"] = Manifest.permission.READ_MEDIA_AUDIO


        //권한 허용 여부 확인
        val permissionDeny = permissionList.count{ ContextCompat.checkSelfPermission(this,it.value) == PackageManager.PERMISSION_DENIED}

        if(permissionDeny > 0){
            requestPermissions(permissionList.values.toTypedArray(),REQUEST_PERMISSIONS)
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_PERMISSIONS){

            grantResults.forEach {
                if(it == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(applicationContext,"앱 실행에 필요한 권한입니다. 동의해주세요.",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

        }


    }





}