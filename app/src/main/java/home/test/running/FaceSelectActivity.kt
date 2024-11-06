package home.test.running

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import home.test.running.databinding.FaceSelectorBinding


//달리기 옵션 설정 화면 담당 엑티비티
//천천히 뛰기 : 8~9분 페이스
//적당히 뛰기 : 6~7분 페이스
//질주 : 5분 페이스
class FaceSelectActivity : AppCompatActivity() {

    private lateinit var binding:FaceSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = FaceSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, FaceRunningActivity::class.java)

        //천천히 뛰기
        binding.slowFace.setOnClickListener {
            val slowFace = "08'00/km"
            intent.putExtra("face",slowFace)
            startActivity(intent)

        }
        //적당히 뛰기
        binding.regularFace.setOnClickListener { 
            val regularFace = "06'00/km"
            intent.putExtra("face",regularFace)
            startActivity(intent)
        }
        //질주
        binding.fastFace.setOnClickListener { 
            val fastFace = "05'00/km"
            intent.putExtra("face",fastFace)
            startActivity(intent)
        }

    }


}