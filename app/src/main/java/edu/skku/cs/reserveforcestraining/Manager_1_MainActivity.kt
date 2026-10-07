package edu.skku.cs.reserveforcestraining

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class Manager_1_MainActivity : AppCompatActivity() {

    private lateinit var dataBaseAll: DataBase_ALL
    private lateinit var applyButton: ImageButton
    private lateinit var textViewDate: TextView
    private lateinit var trainingButton: ImageButton
    private lateinit var imageButtonManage: ImageButton
    private lateinit var imageButtonLogout: ImageButton
    //private lateinit var setmanagerButton:Button // 초기에만 사용

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
        setContentView(R.layout.manager_1_main)

        applyButton = findViewById(R.id.imageButton_apply)
        textViewDate = findViewById(R.id.textView_date)
        trainingButton = findViewById(R.id.imageButton_training)
        imageButtonManage = findViewById(R.id.imageButton_manage)
        imageButtonLogout = findViewById(R.id.imageButton_logout)
        //setmanagerButton = findViewById(R.id.button_manager) // 초기에만 사용

        dataBaseAll = DataBase_ALL()
        handler.postDelayed(updateTimeTask, 0)

        // 등록요청 액티비티로 이동
        applyButton.setOnClickListener {
            val Applyintent = Intent(this@Manager_1_MainActivity, Manager_2_ApplyActivity::class.java)
            startActivity(Applyintent)
        }

        // 장병관리 액티비티로 이동
        trainingButton.setOnClickListener {
            val Manageintent = Intent(this@Manager_1_MainActivity, Manager_3_ManageActivity::class.java)
            startActivity(Manageintent)
        }

        // 관리자 비밀번호 변경 버튼
        imageButtonManage.setOnClickListener {
            val adminpassword = intent.getStringExtra("PASSWORD")
            val passwordEditText = EditText(this@Manager_1_MainActivity)

            // 현재 관리자 비밀번호 확인
            val dialog = AlertDialog.Builder(this@Manager_1_MainActivity)
                .setTitle("비밀번호 확인")
                .setMessage("현재 비밀번호를 입력하세요.")
                .setView(passwordEditText)
                .setPositiveButton("확인") { dialog, _ ->
                    val enteredPassword = passwordEditText.text.toString()
                    if (enteredPassword == adminpassword) { // 일치 시
                        showPasswordChangeDialog()
                    } else { // 불일치 시
                        Toast.makeText(this@Manager_1_MainActivity, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.setOnShowListener {
                val layoutParams = passwordEditText.layoutParams as ViewGroup.MarginLayoutParams
                val marginSize = resources.getDimensionPixelSize(R.dimen.dialog_edittext_margin)
                layoutParams.marginStart = marginSize
                layoutParams.marginEnd = marginSize
                passwordEditText.layoutParams = layoutParams
            }
            dialog.show()
        }

        // 관리자 모드 로그아웃 버튼
        imageButtonLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    // 관리자 비밀번호 변경 다이어로그
    private fun showPasswordChangeDialog() {
        val builder = AlertDialog.Builder(this@Manager_1_MainActivity)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.manager_1_main_changepassword, null)
        builder.setView(dialogView)
            .setTitle("비밀번호 변경")
            .setPositiveButton("확인") { dialog, _ ->
                val newPasswordEditText = dialogView.findViewById<EditText>(R.id.editText_new_password)
                val newPassword = newPasswordEditText.text.toString()

                dataBaseAll.updateManagerPW(AppConfig.MANAGER_ID, newPassword)

                Toast.makeText(this@Manager_1_MainActivity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }

        builder.create().show()
    }

    // 로그아웃 확인 다이어로그
    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        .setTitle("로그아웃")
        .setMessage("로그아웃하시겠습니까?")
        .setPositiveButton("확인") { _, _ ->
            performLogout()
        }
       .setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    // 로그아웃 실행
    private fun performLogout() {
        val logoutIntent = Intent(applicationContext, UI_1_StartActivity::class.java)
        logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(logoutIntent)
        finish()
    }

    // 현재 시각 출력 코드(서울)
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            .apply { timeZone = TimeZone.getTimeZone("Asia/Seoul") }
        return dateFormat.format(Date())
    }
}