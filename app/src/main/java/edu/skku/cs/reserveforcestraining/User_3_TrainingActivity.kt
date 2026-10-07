package edu.skku.cs.reserveforcestraining

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class User_3_TrainingActivity : AppCompatActivity() {

    private lateinit var dataBaseAll: DataBase_ALL
    private lateinit var currentDate: String
    private lateinit var textViewName: TextView
    private lateinit var textViewArmynum: TextView
    private lateinit var textViewPlace: TextView
    private lateinit var button_refresh: ImageButton
    private lateinit var button_back: ImageButton
    private lateinit var button_discharge: Button

    private lateinit var textViewTime1: TextView
    private lateinit var textViewScore1: TextView
    private lateinit var textViewCheck1: TextView
    private lateinit var textViewTime2: TextView
    private lateinit var textViewScore2: TextView
    private lateinit var textViewCheck2: TextView
    private lateinit var textViewTime3: TextView
    private lateinit var textViewScore3: TextView
    private lateinit var textViewCheck3: TextView
    private lateinit var textViewTime4: TextView
    private lateinit var textViewScore4: TextView
    private lateinit var textViewCheck4: TextView
    private lateinit var textViewTime5: TextView
    private lateinit var textViewScore5: TextView
    private lateinit var textViewCheck5: TextView
    private lateinit var textViewTime6: TextView
    private lateinit var textViewScore6: TextView
    private lateinit var textViewCheck6: TextView
    private lateinit var textViewTime7: TextView
    private lateinit var textViewScore7: TextView
    private lateinit var textViewCheck7: TextView

    private var score1: Int by Delegates.notNull()
    private var score2: Int by Delegates.notNull()
    private var score3: Int by Delegates.notNull()
    private var score4: Int by Delegates.notNull()
    private var score5: Int by Delegates.notNull()
    private var score6: Int by Delegates.notNull()
    private var score7: Int by Delegates.notNull()

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeTask = object : Runnable {
        override fun run() {
            currentDate = getCurrentDateTime()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_3_training)

        textViewName = findViewById(R.id.textView_name)
        textViewArmynum = findViewById(R.id.textView_armynum)
        textViewPlace = findViewById(R.id.textView_place)
        button_refresh = findViewById(R.id.imageButton_refresh)
        button_back = findViewById(R.id.imageButton_back)
        button_discharge = findViewById(R.id.button_dischargeapply)

        textViewTime1 = findViewById(R.id.textView_time1)
        textViewScore1 = findViewById(R.id.textView_score1)
        textViewCheck1 = findViewById(R.id.textView_check1)
        textViewTime2 = findViewById(R.id.textView_time2)
        textViewScore2 = findViewById(R.id.textView_score2)
        textViewCheck2 = findViewById(R.id.textView_check2)
        textViewTime3 = findViewById(R.id.textView_time3)
        textViewScore3 = findViewById(R.id.textView_score3)
        textViewCheck3 = findViewById(R.id.textView_check3)
        textViewTime4 = findViewById(R.id.textView_time4)
        textViewScore4 = findViewById(R.id.textView_score4)
        textViewCheck4 = findViewById(R.id.textView_check4)
        textViewTime5 = findViewById(R.id.textView_time5)
        textViewScore5 = findViewById(R.id.textView_score5)
        textViewCheck5 = findViewById(R.id.textView_check5)
        textViewTime6 = findViewById(R.id.textView_time6)
        textViewScore6 = findViewById(R.id.textView_score6)
        textViewCheck6 = findViewById(R.id.textView_check6)
        textViewTime7 = findViewById(R.id.textView_time7)
        textViewScore7 = findViewById(R.id.textView_score7)
        textViewCheck7 = findViewById(R.id.textView_check7)

        dataBaseAll = DataBase_ALL()
        currentDate = getCurrentDateTime()
        handler.postDelayed(updateTimeTask, 0)

        val userName = intent.getStringExtra("NAME")
        val userSerialnum = intent.getStringExtra("ARMYNUM")
        if (userSerialnum != null) {
            dataBaseAll.retrieveField(userSerialnum, "userplace") { retrieveplace ->
                val place = retrieveplace
                textViewPlace.text = place
            }
        }

        textViewName.text = userName
        textViewArmynum.text = userSerialnum

        // 훈련 초기 점수
        score1 = 0
        score2 = 0
        score3 = 0
        score4 = 0
        score5 = 0
        score6 = 0
        score7 = 0

        // 훈련
        userSerialnum?.let { serialNumber ->
            dataBaseAll.retrieveField(serialNumber, "training1time") { time1 ->
                dataBaseAll.retrieveField(userSerialnum, "training1score") { score1String ->
                    score1 = score1String?.toIntOrNull() ?: 0

                    // 완료 시간 및 점수
                    if (time1.isNullOrEmpty()) {
                        textViewTime1.text = time1
                        textViewScore1.text = score1String
                    } else {
                        val datatext1: String = "$currentDate\n$time1" // 현재 날짜와 함께 출력
                        textViewTime1.text = datatext1
                        textViewScore1.text = score1String
                    }
                    // 합격 여부 상태 표시
                    if (score1 != null && time1 != null) {
                        if (score1 >= 80 && !time1.isNullOrEmpty()) {
                            textViewCheck1.text = "O"
                            textViewCheck1.setTextColor(Color.GREEN)
                        } else if (score1 < 80 && !time1.isNullOrEmpty()) {
                            textViewCheck1.text = "X"
                            textViewCheck1.setTextColor(Color.RED)
                        }
                    }
                }
            }

            dataBaseAll.retrieveField(serialNumber, "training2time") { time2 ->
                dataBaseAll.retrieveField(userSerialnum, "training2score") { score2String ->
                    score2 = score2String?.toIntOrNull() ?: 0

                    if (time2.isNullOrEmpty()) {
                        textViewTime2.text = time2
                        textViewScore2.text = score2String
                    } else {
                        val datatext2: String = "$currentDate\n$time2"
                        textViewTime2.text = datatext2
                        textViewScore2.text = score2String
                    }

                    if (score2 != null && time2 != null) {
                        if (score2 >= 80 && !time2.isNullOrEmpty()) {
                            textViewCheck2.text = "O"
                            textViewCheck2.setTextColor(Color.GREEN)
                        } else if (score2 < 80 && !time2.isNullOrEmpty()) {
                            textViewCheck2.text = "X"
                            textViewCheck2.setTextColor(Color.RED)
                        }
                    }
                }
            }

            dataBaseAll.retrieveField(serialNumber, "training3time") { time3 ->
                dataBaseAll.retrieveField(userSerialnum, "training3score") { score3String ->
                    score3 = score3String?.toIntOrNull() ?: 0

                    if (time3.isNullOrEmpty()) {
                        textViewTime3.text = time3
                        textViewScore3.text = score3String
                    } else {
                        val datatext3: String = "$currentDate\n$time3"
                        textViewTime3.text = datatext3
                        textViewScore3.text = score3String
                    }

                    if (score3 != null && time3 != null) {
                        if (score3 >= 80 && !time3.isNullOrEmpty()) {
                            textViewCheck3.text = "O"
                            textViewCheck3.setTextColor(Color.GREEN)
                        } else if (score3 < 80 && !time3.isNullOrEmpty()) {
                            textViewCheck3.text = "X"
                            textViewCheck3.setTextColor(Color.RED)
                        }
                    }
                }
            }

            dataBaseAll.retrieveField(serialNumber, "training4time") { time4 ->
                dataBaseAll.retrieveField(userSerialnum, "training4score") { score4String ->
                    score4 = score4String?.toIntOrNull() ?: 0

                    if (time4.isNullOrEmpty()) {
                        textViewTime4.text = time4
                        textViewScore4.text = score4String
                    } else {
                        val datatext4: String = "$currentDate\n$time4"
                        textViewTime4.text = datatext4
                        textViewScore4.text = score4String
                    }

                    if (score4 != null && time4 != null) {
                        if (score4 >= 80 && !time4.isNullOrEmpty()) {
                            textViewCheck4.text = "O"
                            textViewCheck4.setTextColor(Color.GREEN)
                        } else if (score4 < 80 && !time4.isNullOrEmpty()) {
                            textViewCheck4.text = "X"
                            textViewCheck4.setTextColor(Color.RED)
                        }
                    }
                }
            }

            dataBaseAll.retrieveField(serialNumber, "training5time") { time5 ->
                dataBaseAll.retrieveField(userSerialnum, "training5score") { score5String ->
                    score5 = score5String?.toIntOrNull() ?: 0

                    if (time5.isNullOrEmpty()) {
                        textViewTime5.text = time5
                        textViewScore5.text = score5String
                    } else {
                        val datatext5: String = "$currentDate\n$time5"
                        textViewTime5.text = datatext5
                        textViewScore5.text = score5String
                    }

                    if (score5 != null && time5 != null) {
                        if (score5 >= 80 && !time5.isNullOrEmpty()) {
                            textViewCheck5.text = "O"
                            textViewCheck5.setTextColor(Color.GREEN)
                        } else if (score5 < 80 && !time5.isNullOrEmpty()) {
                            textViewCheck5.text = "X"
                            textViewCheck5.setTextColor(Color.RED)
                        }
                    }
                }
            }

            dataBaseAll.retrieveField(serialNumber, "training6time") { time6 ->
                dataBaseAll.retrieveField(userSerialnum, "training6score") { score6String ->
                    score6 = score6String?.toIntOrNull() ?: 0

                    if (time6.isNullOrEmpty()) {
                        textViewTime6.text = time6
                        textViewScore6.text = score6String
                    } else {
                        val datatext6: String = "$currentDate\n$time6"
                        textViewTime6.text = datatext6
                        textViewScore6.text = score6String
                    }

                    if (score6 != null && time6 != null) {
                        if (score6 >= 80 && !time6.isNullOrEmpty()) {
                            textViewCheck6.text = "O"
                            textViewCheck6.setTextColor(Color.GREEN)
                        } else if (score6 < 80 && !time6.isNullOrEmpty()) {
                            textViewCheck6.text = "X"
                            textViewCheck6.setTextColor(Color.RED)
                        }
                    }
                }
            }

            dataBaseAll.retrieveField(serialNumber, "training7time") { time7 ->
                dataBaseAll.retrieveField(userSerialnum, "training7score") { score7String ->
                    score7 = score7String?.toIntOrNull() ?: 0

                    if (time7.isNullOrEmpty()) {
                        textViewTime7.text = time7
                        textViewScore7.text = score7String
                    } else {
                        val datatext7: String = "$currentDate\n$time7"
                        textViewTime7.text = datatext7
                        textViewScore7.text = score7String
                    }

                    if (score7 != null && time7 != null) {
                        if (score7 >= 80 && !time7.isNullOrEmpty()) {
                            textViewCheck7.text = "O"
                            textViewCheck7.setTextColor(Color.GREEN)
                        } else if (score7 < 80 && !time7.isNullOrEmpty()) {
                            textViewCheck7.text = "X"
                            textViewCheck7.setTextColor(Color.RED)
                        }
                    }
                }
            }
        }

        // 퇴소 신청 버튼 처리
        if (userSerialnum != null) {
            dataBaseAll.retrieveField(userSerialnum, "training1score") { score1String ->
                score1 = score1String?.toIntOrNull() ?: 0
                dataBaseAll.retrieveField(userSerialnum, "training2score") { score2String ->
                    score2 = score2String?.toIntOrNull() ?: 0
                    dataBaseAll.retrieveField(userSerialnum, "training3score") { score3String ->
                        score3 = score3String?.toIntOrNull() ?: 0
                        dataBaseAll.retrieveField(userSerialnum, "training4score") { score4String ->
                            score4 = score4String?.toIntOrNull() ?: 0
                            dataBaseAll.retrieveField(
                                userSerialnum,
                                "training5score"
                            ) { score5String ->
                                score5 = score5String?.toIntOrNull() ?: 0
                                dataBaseAll.retrieveField(
                                    userSerialnum,
                                    "training6score"
                                ) { score6String ->
                                    score6 = score6String?.toIntOrNull() ?: 0
                                    dataBaseAll.retrieveField(
                                        userSerialnum,
                                        "training7score"
                                    ) { score7String ->
                                        score7 = score7String?.toIntOrNull() ?: 0
                                        // 합격 조건 달성 시
                                        if ((score1 >= 80) && (score2 >= 80) && (score3 >= 80) && (score4 >= 80) && (score5 >= 80) && (score6 >= 80) && (score7 >= 80)) {
                                            button_discharge.setBackgroundColor(Color.parseColor("#4040ff"))
                                            button_discharge.setOnClickListener {
                                                val dialogBuilder = AlertDialog.Builder(this)
                                                    .setTitle("퇴소 신청")
                                                    .setMessage("퇴소 신청 완료")
                                                    .setPositiveButton("확인") { dialog, _ ->
                                                        if (userSerialnum != null) {
                                                            dataBaseAll.updateUserApply(
                                                                userSerialnum,
                                                                3
                                                            ) // 퇴소 요청
                                                        }
                                                        dialog.dismiss()
                                                    }
                                                val dialog = dialogBuilder.create()
                                                dialog.show()
                                            }
                                        } else { // 합격 조건 미달성 시
                                            button_discharge.setBackgroundColor(Color.parseColor("#404040"))
                                            button_discharge.setOnClickListener {
                                                val dialogBuilder = AlertDialog.Builder(this)
                                                    .setTitle("조건 충족 필요")
                                                    .setMessage("퇴소 조건이 충족되지 않았습니다.\n 그래도 퇴소하시겠습니까?")
                                                    .setPositiveButton("퇴소 신청") { dialog, _ ->
                                                        val dialogBuilder2 =
                                                            AlertDialog.Builder(this) // 재확인
                                                                .setTitle("퇴소 신청")
                                                                .setMessage("정말 퇴소하시겠습니까?")
                                                                .setPositiveButton("확인") { dialog, _ ->
                                                                    if (userSerialnum != null) {
                                                                        dataBaseAll.updateUserApply(
                                                                            userSerialnum,
                                                                            5
                                                                        ) // 중도 퇴소 요청
                                                                    }
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
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 새로고침
        button_refresh.setOnClickListener {
            refreshPage()
        }

        // 뒤로가기
        button_back.setOnClickListener {
            onBackPressed()
        }
    }

    // 새로고침
    private fun refreshPage() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    // 현재 시간 출력(서울)
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA)
            .apply { timeZone = TimeZone.getTimeZone("Asia/Seoul") }
        return dateFormat.format(Date())
    }
}