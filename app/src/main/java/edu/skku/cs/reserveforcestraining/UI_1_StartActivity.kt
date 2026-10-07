package edu.skku.cs.reserveforcestraining

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class UI_1_StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_1_start)

        // 로그인 버튼 - 로그인 액티비티로 이동
        val loginButton: Button = findViewById(R.id.button_login)
        loginButton.setOnClickListener {
            val loginintent = Intent(this@UI_1_StartActivity, UI_2_LogInActivity::class.java)
            startActivity(loginintent)
        }

        // 회원 가입 버튼 - 가입 액티비티로 이동
        val signupButton: Button = findViewById(R.id.button_signup)
        signupButton.setOnClickListener {
            val signupintent = Intent(this@UI_1_StartActivity, UI_3_SignUpActivity::class.java)
            startActivity(signupintent)
        }

        // 관리자 버튼
        val adminButton: Button = findViewById(R.id.button_manager)
        adminButton.setOnClickListener {
            val passwordDialog = UI_4_CheckManagerActivity()
            passwordDialog.show(supportFragmentManager, "PasswordDialog")
        }
    }
}
