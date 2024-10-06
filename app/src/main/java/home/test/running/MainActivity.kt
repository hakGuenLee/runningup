package home.test.running

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        permissionRequestChecker()

    }

    //권한 허용 요청
    private fun permissionRequestChecker(){
        var permissionList = mutableMapOf<String, String>()
        permissionList["location"] = Manifest.permission.ACCESS_COARSE_LOCATION
        permissionList["fineLocation"] = Manifest.permission.ACCESS_FINE_LOCATION
        permissionList["audio"] = Manifest.permission.READ_MEDIA_AUDIO

        //권한 허용 여부 확인
        var permissionDeny = permissionList.count{ ContextCompat.checkSelfPermission(this,it.value) == PackageManager.PERMISSION_DENIED}

        if(permissionDeny > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissionList.values.toTypedArray(),REQUEST_PERMISSIONS)
        }


    }





}