package edu.skku.cs.reserveforcestraining

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class Manager_3_ManageScoreInputActivity : AppCompatActivity() {

    private lateinit var dataBaseAll: DataBase_ALL
    private lateinit var username: String
    private lateinit var userSerialnum: String
    private lateinit var usernameTextView: TextView
    private lateinit var userarmynumTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var buttonInput: Button
    private lateinit var button_back: ImageButton

    private lateinit var training1time: EditText
    private lateinit var training1score: EditText
    private lateinit var training2time: EditText
    private lateinit var training2score: EditText
    private lateinit var training3time: EditText
    private lateinit var training3score: EditText
    private lateinit var training4time: EditText
    private lateinit var training4score: EditText
    private lateinit var training5time: EditText
    private lateinit var training5score: EditText
    private lateinit var training6time: EditText
    private lateinit var training6score: EditText
    private lateinit var training7time: EditText
    private lateinit var training7score: EditText

    // 현재 시간 출력
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeTask = object : Runnable {
        override fun run() {
            val currentDate = getCurrentDateTime()
            dateTextView.text = currentDate

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_3_manage_score_input)

        dataBaseAll = DataBase_ALL()
        handler.postDelayed(updateTimeTask, 0)

        username = intent.getStringExtra("USERNAME").toString()
        userSerialnum = intent.getStringExtra("USERARMYNUM").toString()

        usernameTextView = findViewById(R.id.textView_name)
        userarmynumTextView = findViewById(R.id.textView_armynum)
        dateTextView = findViewById(R.id.textView_date)
        buttonInput = findViewById(R.id.buttonInput)
        button_back = findViewById(R.id.imageButton_back)

        training1time = findViewById(R.id.edittext_time1)
        training1score = findViewById(R.id.edittext_score1)
        training2time = findViewById(R.id.edittext_time2)
        training2score = findViewById(R.id.edittext_score2)
        training3time = findViewById(R.id.edittext_time3)
        training3score = findViewById(R.id.edittext_score3)
        training4time = findViewById(R.id.edittext_time4)
        training4score = findViewById(R.id.edittext_score4)
        training5time = findViewById(R.id.edittext_time5)
        training5score = findViewById(R.id.edittext_score5)
        training6time = findViewById(R.id.edittext_time6)
        training6score = findViewById(R.id.edittext_score6)
        training7time = findViewById(R.id.edittext_time7)
        training7score = findViewById(R.id.edittext_score7)

        usernameTextView.text = username
        userarmynumTextView.text = userSerialnum

        val usersRef = FirebaseDatabase.getInstance().getReference("users")

        usersRef.child(userSerialnum).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(DataBase_ALL.User::class.java)
                if (user != null) {
                    // 점수가 80 이상(합격)이라면 EditText 수정 불가 / 점수 고정
                    if (user.training1score >= 80) {
                        training1time.setText(user.training1time)
                        training1time.isEnabled = false
                        training1score.setText(user.training1score.toString())
                        training1score.isEnabled = false
                    }
                    if (user.training2score >= 80) {
                        training2time.setText(user.training2time)
                        training2time.isEnabled = false
                        training2score.setText(user.training2score.toString())
                        training2score.isEnabled = false
                    }
                    if (user.training3score >= 80) {
                        training3time.setText(user.training3time)
                        training3time.isEnabled = false
                        training3score.setText(user.training3score.toString())
                        training3score.isEnabled = false
                    }
                    if (user.training4score >= 80) {
                        training4time.setText(user.training4time)
                        training4time.isEnabled = false
                        training4score.setText(user.training4score.toString())
                        training4score.isEnabled = false
                    }
                    if (user.training5score >= 80) {
                        training5time.setText(user.training5time)
                        training5time.isEnabled = false
                        training5score.setText(user.training5score.toString())
                        training5score.isEnabled = false
                    }
                    if (user.training6score >= 80) {
                        training6time.setText(user.training6time)
                        training6time.isEnabled = false
                        training6score.setText(user.training6score.toString())
                        training6score.isEnabled = false
                    }
                    if (user.training7score >= 80) {
                        training7time.setText(user.training7time)
                        training7time.isEnabled = false
                        training7score.setText(user.training7score.toString())
                        training7score.isEnabled = false
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        // 입력 버튼
        buttonInput.setOnClickListener {
            if (userSerialnum != null) {
                // 같은 라인에 점수는 있으나 완료 시간 미입력 시
                if ((training1time.text.toString().isEmpty() && training1score.text.toString()
                        .isNotEmpty())
                    || (training2time.text.toString().isEmpty() && training2score.text.toString()
                        .isNotEmpty())
                    || (training3time.text.toString().isEmpty() && training3score.text.toString()
                        .isNotEmpty())
                    || (training4time.text.toString().isEmpty() && training4score.text.toString()
                        .isNotEmpty())
                    || (training5time.text.toString().isEmpty() && training5score.text.toString()
                        .isNotEmpty())
                    || (training6time.text.toString().isEmpty() && training6score.text.toString()
                        .isNotEmpty())
                    || (training7time.text.toString().isEmpty() && training7score.text.toString()
                        .isNotEmpty())
                ) {
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "완료시간을 입력해 주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // 같은 라인에 완료 시간은 있으나 점수 미입력 시
                if ((training1time.text.toString().isNotEmpty() && training1score.text.toString()
                        .isEmpty())
                    || (training2time.text.toString().isNotEmpty() && training2score.text.toString()
                        .isEmpty())
                    || (training3time.text.toString().isNotEmpty() && training3score.text.toString()
                        .isEmpty())
                    || (training4time.text.toString().isNotEmpty() && training4score.text.toString()
                        .isEmpty())
                    || (training5time.text.toString().isNotEmpty() && training5score.text.toString()
                        .isEmpty())
                    || (training6time.text.toString().isNotEmpty() && training6score.text.toString()
                        .isEmpty())
                    || (training7time.text.toString().isNotEmpty() && training7score.text.toString()
                        .isEmpty())
                ) {
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "점수를 입력해 주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // 점수 입력 시 완료 시간과 점수 모두 등록되었고 아직 합격이 아닐 시 점수 입력 후 확인 메세지 출력
                if (training1time.text.toString().isNotEmpty() && training1score.text.toString()
                        .isNotEmpty() && training1score.isEnabled
                ) {
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training1time",
                        training1time.text.toString()
                    )
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training1score",
                        training1score.text.toString().toInt()
                    )
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "안보교육 점수가 입력되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (training2time.text.toString().isNotEmpty() && training2score.text.toString()
                        .isNotEmpty() && training2score.isEnabled
                ) {
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training2time",
                        training2time.text.toString()
                    )
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training2score",
                        training2score.text.toString().toInt()
                    )
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "검문소 점수가 입력되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (training3time.text.toString().isNotEmpty() && training3score.text.toString()
                        .isNotEmpty() && training3score.isEnabled
                ) {
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training3time",
                        training3time.text.toString()
                    )
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training3score",
                        training3score.text.toString().toInt()
                    )
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "화생방 점수가 입력되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (training4time.text.toString().isNotEmpty() && training4score.text.toString()
                        .isNotEmpty() && training4score.isEnabled
                ) {
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training4time",
                        training4time.text.toString()
                    )
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training4score",
                        training4score.text.toString().toInt()
                    )
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "구급법 점수가 입력되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (training5time.text.toString().isNotEmpty() && training5score.text.toString()
                        .isNotEmpty() && training5score.isEnabled
                ) {
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training5time",
                        training5time.text.toString()
                    )
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training5score",
                        training5score.text.toString().toInt()
                    )
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "총기손질 점수가 입력되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (training6time.text.toString().isNotEmpty() && training6score.text.toString()
                        .isNotEmpty() && training6score.isEnabled
                ) {
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training6time",
                        training6time.text.toString()
                    )
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training6score",
                        training6score.text.toString().toInt()
                    )
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "사격훈련 점수가 입력되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (training7time.text.toString().isNotEmpty() && training7score.text.toString()
                        .isNotEmpty() && training7score.isEnabled
                ) {
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training7time",
                        training7time.text.toString()
                    )
                    dataBaseAll.updateUserField(
                        userSerialnum,
                        "training7score",
                        training7score.text.toString().toInt()
                    )
                    Toast.makeText(
                        this@Manager_3_ManageScoreInputActivity,
                        "시가지 전투 점수가 입력되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        // 뒤로가기 버튼
        button_back.setOnClickListener {
            onBackPressed()
        }
    }

    // 현재 시간 출력(서울)
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.KOREA)
            .apply { timeZone = TimeZone.getTimeZone("Asia/Seoul") }
        return dateFormat.format(Date())
    }
}