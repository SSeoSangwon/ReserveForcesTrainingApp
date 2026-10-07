package edu.skku.cs.reserveforcestraining

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class User_1_MainActivity : AppCompatActivity() {
    private lateinit var textViewName: TextView
    private lateinit var textViewArmynum: TextView
    private lateinit var textViewDate: TextView
    private lateinit var textViewComplete: TextView
    private lateinit var imageButtonApply: ImageButton
    private lateinit var imageButtonTraining: ImageButton
    private lateinit var imageButtonPlace: ImageButton
    private lateinit var imageButtonLogout: ImageButton
    private lateinit var imageButtonMap: ImageButton
    private lateinit var imageButtonCall: ImageButton
    private lateinit var imageButtonYoutube: ImageButton
    private lateinit var dataBaseAll: DataBase_ALL

    // 현재 시간 출력
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeTask = object : Runnable {
        override fun run() {
            val currentDate = getCurrentDateTime()
            textViewDate.text = currentDate

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBaseAll = DataBase_ALL()

        setContentView(R.layout.user_1_main)

        textViewName = findViewById(R.id.textView_name)
        textViewArmynum = findViewById(R.id.textView_armynum)
        textViewDate = findViewById(R.id.textView_date)
        textViewComplete = findViewById(R.id.textView_complete)
        imageButtonApply = findViewById(R.id.imageButton_apply)
        imageButtonTraining = findViewById(R.id.imageButton_training)
        imageButtonPlace = findViewById(R.id.imageButton_place)
        imageButtonLogout = findViewById(R.id.imageButton_logout)
        imageButtonMap = findViewById(R.id.imageButton_map)
        imageButtonCall = findViewById(R.id.imageButton_call)
        imageButtonYoutube = findViewById(R.id.imageButton_youtube)


        val userSerialnum = intent.getStringExtra("USERARMYNUM")
        textViewArmynum.setText(userSerialnum)

        // 장병 이름 반환
        var userName: String? = null
        if (userSerialnum != null) {
            dataBaseAll.retrieveField(userSerialnum, "username") { retrievedName ->
                userName = retrievedName
                runOnUiThread {
                    textViewName.setText(userName)
                }
            }
        }

        // 장병 부대 반환
        var userPlace: String? = null
        if (userSerialnum != null) {
            dataBaseAll.retrieveField(userSerialnum, "userplace") { retrievedPlace ->
                userPlace = retrievedPlace
            }
        }

        handler.postDelayed(updateTimeTask, 0)

        // 등록 요청 버튼
        imageButtonApply.setOnClickListener {
            val userApply = userSerialnum?.let { it1 ->
                dataBaseAll.retrieveField(userSerialnum, "userapply") { retrieveuserApply ->
                    var userApply = retrieveuserApply?.toIntOrNull() ?: 0
                    if (userApply == 4 || userApply == 6) { // 4:퇴소 / 6:중도 퇴소
                        val dialogBuilder = AlertDialog.Builder(this)
                            .setTitle("퇴소 기록 존재")
                            .setMessage("퇴소 기록이 있습니다.\n재등록하시겠습니까?")
                            .setPositiveButton("등록 신청") { dialog, _ ->
                                val dialogBuilder2 =
                                    AlertDialog.Builder(this) // 재확인
                                        .setTitle("등록 신청")
                                        .setMessage("기록이 삭제되고 재등록됩니다!")
                                        .setPositiveButton("확인") { dialog, _ ->
                                            if (userSerialnum != null) {
                                                dataBaseAll.updateUserApply(userSerialnum, 1)
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "userplace",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training1time",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training1score",
                                                    0
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training2time",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training2score",
                                                    0
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training3time",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training3score",
                                                    0
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training4time",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training4score",
                                                    0
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training5time",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training5score",
                                                    0
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training6time",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training6score",
                                                    0
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training7time",
                                                    ""
                                                )
                                                dataBaseAll.updateUserField(
                                                    userSerialnum,
                                                    "training7score",
                                                    0
                                                )
                                            }
                                            refreshPage()
                                            dialog.dismiss()
                                        }
                                        .setNegativeButton("취소") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                val dialog = dialogBuilder2.create()
                                dialog.show()
                            }
                            .setNegativeButton("취소") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog = dialogBuilder.create()
                        dialog.show()
                    } else if (userApply == 0) { // 기본 상태
                        if (userSerialnum != null) {
                            dataBaseAll.updateUserApply(userSerialnum, 1) // 등록 요청
                        }
                        val dialogBuilder = AlertDialog.Builder(this)
                            .setTitle("등록 신청 완료")
                            .setMessage("신청이 정상적으로 완료되었습니다.")
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog = dialogBuilder.create()
                        dialog.show()
                    } else if (userApply == 1) { // 등록 대기
                        val dialogBuilder = AlertDialog.Builder(this)
                            .setTitle("등록 대기")
                            .setMessage("아직 승인되지 않았습니다.")
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog = dialogBuilder.create()
                        dialog.show()
                    } else { // 등록
                        val dialogBuilder = AlertDialog.Builder(this)
                            .setTitle("등록 완료")
                            .setMessage("이미 등록되어있습니다.")
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog = dialogBuilder.create()
                        dialog.show()
                    }
                }
            }
        }

        // 훈련경과 버튼
        imageButtonTraining.setOnClickListener {
            val userApply = userSerialnum?.let { it1 ->
                dataBaseAll.retrieveField(userSerialnum, "userapply") { retrieveuserApply ->
                    var userApply = retrieveuserApply?.toIntOrNull() ?: 0
                    if (userApply == 0) { // 기본 -> 등록 우선
                        val dialogBuilder = AlertDialog.Builder(this)
                            .setTitle("등록 필요")
                            .setMessage("등록이 필요합니다.")
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog = dialogBuilder.create()
                        dialog.show()
                    } else if (userApply == 1) { // 등록 요청 -> 등록 승인 우선
                        val dialogBuilder = AlertDialog.Builder(this)
                            .setTitle("등록 대기")
                            .setMessage("아직 승인되지 않았습니다.")
                            .setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog = dialogBuilder.create()
                        dialog.show()
                    } else {
                        val trainingintent = Intent(this, User_3_TrainingActivity::class.java)
                        trainingintent.putExtra("NAME", userName)
                        trainingintent.putExtra("ARMYNUM", userSerialnum)
                        trainingintent.putExtra("ARMYPLACE", userPlace)
                        startActivity(trainingintent)
                    }
                }
            }
        }

        // 등록 현황 버튼
        imageButtonPlace.setOnClickListener {
            val userApply = userSerialnum?.let { it1 ->
                dataBaseAll.retrieveField(userSerialnum, "userapply") { retrieveuserApply ->
                    var userApply = retrieveuserApply?.toIntOrNull() ?: 0
                    val userPlace = userSerialnum?.let { it2 ->
                        dataBaseAll.retrieveField(it2, "userplace") { userPlace ->
                            if (userApply == 0) { // 기본 -> 등록 우선
                                val dialogBuilder = AlertDialog.Builder(this)
                                    .setTitle("등록 필요")
                                    .setMessage("먼저 등록해주세요.")
                                    .setPositiveButton("확인") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                val dialog = dialogBuilder.create()
                                dialog.show()
                            } else if (userApply == 1) { // 등록 요청 -> 등록 승인 우선
                                val dialogBuilder = AlertDialog.Builder(this)
                                    .setTitle("등록 대기")
                                    .setMessage("아직 승인되지 않았습니다.")
                                    .setPositiveButton("확인") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                val dialog = dialogBuilder.create()
                                dialog.show()
                            } else { // 등록 상태
                                val dialogBuilder = AlertDialog.Builder(this)
                                    .setTitle("등록 현황")
                                    .setMessage("소속은 \"$userPlace\" 입니다.")
                                    .setPositiveButton("확인") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                val dialog = dialogBuilder.create()
                                dialog.show()
                            }
                        }
                    }
                }
            }
        }

        // 전화번호부 버튼
        imageButtonCall.setOnClickListener {
            val callintent = Intent(this, User_4_CallActivity::class.java)
            startActivity(callintent)
        }

        // 지도 버튼
        imageButtonMap.setOnClickListener {
            val mapintent = Intent(this, User_5_MapActivity::class.java)
            startActivity(mapintent)
        }

        // 국방부채널 버튼
        imageButtonYoutube.setOnClickListener {
            val youtubeIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/@ROK_MND"))
            startActivity(youtubeIntent)
        }

        // 로그아웃 버튼
        imageButtonLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // 재로그인 시 상태 출력
        userSerialnum?.let { it1 ->
            dataBaseAll.retrieveField(userSerialnum, "userapply") { retrieveuserApply ->
                var userApply = retrieveuserApply?.toIntOrNull() ?: 0
                runOnUiThread {
                    if (userApply == 4) {
                        textViewComplete.text = "퇴소 완료"
                        textViewComplete.setTextColor(Color.GREEN)
                    } else if (userApply == 6) {
                        textViewComplete.text = "중도 퇴소"
                        textViewComplete.setTextColor(Color.RED)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeTask)
    }

    // 로그아웃 확인 다이어로그
    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("로그아웃")
        alertDialogBuilder.setMessage("로그아웃하시겠습니까?")
        alertDialogBuilder.setPositiveButton("확인") { _, _ ->
            performLogout()
        }
        alertDialogBuilder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // 로그아웃 처리
    private fun performLogout() {
        val userSerialnum = intent.getStringExtra("USERARMYNUM")
        if (userSerialnum != null) {
            dataBaseAll.updateUserField(userSerialnum, "userflag", 0) // 중복 로그인 방지
        }
        val logoutIntent = Intent(applicationContext, UI_1_StartActivity::class.java)
        logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(logoutIntent)
        finish()
    }

    // 현재 시간 출력(서울)
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            .apply { timeZone = TimeZone.getTimeZone("Asia/Seoul") }
        return dateFormat.format(Date())
    }

    // 핸드폰 뒤로가기 버튼시에도 로그아웃 확인 -> 종료 처리 -> 중복 로그인 방지
    override fun onBackPressed() {
        showLogoutConfirmationDialog()
    }

    // 새로고침
    private fun refreshPage() {
        val intent = intent
        finish()
        startActivity(intent)
    }
}
