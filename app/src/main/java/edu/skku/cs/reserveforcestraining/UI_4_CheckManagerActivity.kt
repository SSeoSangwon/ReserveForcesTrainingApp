package edu.skku.cs.reserveforcestraining

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.*

class UI_4_CheckManagerActivity : DialogFragment() {
    private lateinit var dataBaseAll: DataBase_ALL
    private lateinit var adminPassword: String

    // 관리자 확인
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBaseAll = DataBase_ALL()
        dataBaseAll.retrieveManagerPW(AppConfig.MANAGER_ID) { password ->
            adminPassword = password ?: ""
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.ui_4_check, null)

        // 관리자 로그인
        builder.setView(dialogView)
            .setTitle("관리자 비밀번호 입력")
            .setPositiveButton("확인") { dialog, _ ->
                val passwordEditText = dialogView.findViewById<EditText>(R.id.editText_password)
                val enteredPassword = passwordEditText.text.toString()
                if (enteredPassword == adminPassword) {
                    val intent = Intent(requireContext(), Manager_1_MainActivity::class.java)
                    intent.putExtra("PASSWORD", adminPassword)
                    startActivity(intent)
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }

        return builder.create()
    }
}