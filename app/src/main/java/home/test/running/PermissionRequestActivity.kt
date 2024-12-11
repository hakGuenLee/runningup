package home.test.running

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import home.test.running.databinding.FaceSelectorBinding
import home.test.running.databinding.PermissionPageBinding


//달리기 옵션 설정 화면 담당 엑티비티
//천천히 뛰기 : 8~9분 페이스
//적당히 뛰기 : 6~7분 페이스
//질주 : 5분 페이스
class PermissionRequestActivity : AppCompatActivity() {

    private lateinit var binding:PermissionPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PermissionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}