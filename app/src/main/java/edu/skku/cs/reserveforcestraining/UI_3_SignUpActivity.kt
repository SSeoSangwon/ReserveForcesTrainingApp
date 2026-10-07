package edu.skku.cs.reserveforcestraining

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UI_3_SignUpActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextBirth: EditText
    private lateinit var editTextArmynum: EditText
    private lateinit var editTextPhonenum: EditText
    private lateinit var editTextPw: EditText
    private lateinit var buttonSignup: Button
    private lateinit var imageButtonBack: View
    private lateinit var buttonSerialNumberDuplicateCheck: Button

    private lateinit var dataBaseAll: DataBase_ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_3_signup)

        editTextName = findViewById(R.id.edittext_name)
        editTextBirth = findViewById(R.id.edittext_birth)
        editTextArmynum = findViewById(R.id.edittext_armynum)
        editTextPhonenum = findViewById(R.id.edittext_phonenum)
        editTextPw = findViewById(R.id.edittext_pw)
        buttonSignup = findViewById(R.id.button_signup)
        imageButtonBack = findViewById(R.id.imageButton_back)
        buttonSerialNumberDuplicateCheck = findViewById(R.id.button_numcheck)

        dataBaseAll = DataBase_ALL()

        var flag = 0 // 군번 중복 확인 flag
        buttonSerialNumberDuplicateCheck.setOnClickListener {
            val serialNumber = editTextArmynum.text.toString()
            isSerialNumberDuplicate(serialNumber) { isDuplicate ->
                runOnUiThread {
                    if (isDuplicate) { // 군번 중복 시
                        Toast.makeText(this, "이미 등록된 군번입니다.", Toast.LENGTH_SHORT).show()
                    } else { // 군번 사용 가능 시
                        flag = 1 // flag 업데이트
                        // 버튼 색상 변경 및 클릭 비활성화, EditText 비활성화
                        buttonSerialNumberDuplicateCheck.setBackgroundColor(Color.parseColor("#404040"))
                        buttonSerialNumberDuplicateCheck.isClickable = false
                        editTextArmynum.isEnabled = false
                        Toast.makeText(this, "군번 확인 완료", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 가입 버튼
        buttonSignup.setOnClickListener {
            val name = editTextName.text.toString()
            val birth = editTextBirth.text.toString()
            val phonenum = editTextPhonenum.text.toString()
            val armynum = editTextArmynum.text.toString()
            val password = editTextPw.text.toString()

            if ((name == "") || (birth == "") || (phonenum == "") || (armynum == "") || (password == "")) { // 정보 미입력 시
                Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (flag == 0) { // 군번 중복 미실시 시
                    Toast.makeText(this, "군번 중복 확인해주세요.", Toast.LENGTH_SHORT).show()
                } else { // 장병 등록
                    val user = DataBase_ALL.User(
                        username = name,
                        userflag = 0,
                        userbirth = birth,
                        userarmynum = armynum,
                        userphonenum = phonenum,
                        userpw = password,
                        userapply = 0,
                        userplace = "",
                        training1time = "",
                        training1score = 0,
                        training2time = "",
                        training2score = 0,
                        training3time = "",
                        training3score = 0,
                        training4time = "",
                        training4score = 0,
                        training5time = "",
                        training5score = 0,
                        training6time = "",
                        training6score = 0,
                        training7time = "",
                        training7score = 0,
                    )
                    dataBaseAll.insertUser(user)
                    flag = 0
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
            }
        }

        // 뒤로가기 버튼
        imageButtonBack.setOnClickListener {
            onBackPressed()
        }
    }

    // 군번 중복 확인
    private fun isSerialNumberDuplicate(serialNumber: String, callback: (Boolean) -> Unit) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        val query = usersRef.orderByChild("userarmynum").equalTo(serialNumber)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isDuplicate = snapshot.exists()
                callback(isDuplicate)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}