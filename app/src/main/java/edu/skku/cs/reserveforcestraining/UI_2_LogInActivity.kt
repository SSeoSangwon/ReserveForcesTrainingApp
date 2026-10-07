package edu.skku.cs.reserveforcestraining

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UI_2_LogInActivity : AppCompatActivity() {

    private lateinit var dataBaseAll: DataBase_ALL
    private lateinit var editTextArmynum: EditText
    private lateinit var editTextPw: EditText
    private lateinit var buttonCheck: Button
    private lateinit var imageButtonBack: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_2_login)

        dataBaseAll = DataBase_ALL()

        editTextArmynum = findViewById(R.id.edittext_armynum)
        editTextPw = findViewById(R.id.edittext_pw)
        buttonCheck = findViewById(R.id.button_check)
        imageButtonBack = findViewById(R.id.imageButton_back)

        // 로그인 버튼
        buttonCheck.setOnClickListener {
            val userSerialnum = editTextArmynum.text.toString()
            val password = editTextPw.text.toString()

            isValidCredentials(userSerialnum, password) { isValid ->
                if (isValid) { // 아이디 비밀번호 일치 시
                    if (userSerialnum != null) {
                        dataBaseAll.retrieveField(
                            userSerialnum,
                            "userflag"
                        ) { userFlag -> // 중복 접속 방지
                            var flag = userFlag?.toIntOrNull() ?: 0
                            if (flag == 1) {
                                Toast.makeText(this, "다른 기기에서 이미 이용중인 군번입니다.", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                dataBaseAll.updateUserField(userSerialnum, "userflag", 1)
                                val usermainintent = Intent(this, User_1_MainActivity::class.java)
                                usermainintent.putExtra("USERARMYNUM", userSerialnum)
                                startActivity(usermainintent)
                                finish()
                            }
                        }
                    }
                } else { // 아이디 비밀번호 불일치 시
                    Toast.makeText(this, "다시 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
        editTextPw.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        imageButtonBack.setOnClickListener {
            onBackPressed()
        }
    }

    // 아이디 비밀번호 일치 확인
    private fun isValidCredentials(
        serialnum: String,
        password: String,
        callback: (Boolean) -> Unit
    ) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        val userRef = usersRef.child(serialnum)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(DataBase_ALL.User::class.java)
                val storedPassword = user?.userpw?.toString()

                val isValid = storedPassword != null && password == storedPassword
                callback(isValid)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }
}