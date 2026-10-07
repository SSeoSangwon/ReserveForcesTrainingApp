package edu.skku.cs.reserveforcestraining

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Manager_2_ApplyActivity : AppCompatActivity() {

    private lateinit var listViewApply: ListView
    private lateinit var dataBaseAll: DataBase_ALL
    private lateinit var applyList: MutableList<DataBase_ALL.User2>
    private lateinit var applyListAdapter: ApplyListAdapter
    private lateinit var buttonRefresh: ImageButton
    private lateinit var buttonBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_2_apply)

        listViewApply = findViewById(R.id.listview_applylist)
        buttonRefresh = findViewById(R.id.imageButton_refresh)
        buttonBack = findViewById(R.id.imageButton_back)

        dataBaseAll = DataBase_ALL()
        applyList = mutableListOf()
        applyListAdapter = ApplyListAdapter(this, applyList, dataBaseAll)
        listViewApply.adapter = applyListAdapter

        // 새로고침 버튼
        buttonRefresh.setOnClickListener {
            refreshPage()
        }

        // 뒤로가기 버튼
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        // 등록 요청 리스트 출력
        loadApplyList()
    }

    // 등록 요청 리스트 출력
    private fun loadApplyList() {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                applyList.clear()
                for (userSnapshot in snapshot.children) {
                    val userarmynum = userSnapshot.key
                    val username = userSnapshot.child("username").getValue(String::class.java)
                    val userapply = userSnapshot.child("userapply").getValue(Int::class.java)
                    val userplace = userSnapshot.child("userplace").getValue(String::class.java)
                    if (userarmynum != null && username != null && userapply != null && userplace != null) {
                        val user = DataBase_ALL.User2(userarmynum, username, userapply, userplace)
                        if (user.userapply == 1 || user.userapply == 3 || user.userapply == 5) { // 1:등록 요청 / 3:퇴소 요청 / 5:중도퇴소 요청
                            applyList.add(user)
                        }
                    }
                }
                applyListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 새로고침
    private fun refreshPage() {
        val intent = intent
        finish()
        startActivity(intent)
    }
}

class ApplyListAdapter(
    private val context: Context,
    private val userList: List<DataBase_ALL.User2>,
    private val dataBaseAll: DataBase_ALL
) : BaseAdapter() {

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.manager_2_apply_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val user = userList[position]

        viewHolder.nameTextView.text = user.username
        viewHolder.armynumTextView.text = user.userarmynum
        val userSerialnum = user.userarmynum

        // 군번을 매개로 요청 상태 반환
        dataBaseAll.retrieveField(userSerialnum, "userapply") { retrieveuserApply ->
            var userApply = retrieveuserApply?.toIntOrNull() ?: 0
            if (userApply == 3) { // 퇴소 요청
                dataBaseAll.retrieveField(userSerialnum, "userplace") { placeText ->
                    viewHolder.placeEditText.setText(placeText)
                    viewHolder.placeEditText.isEnabled = false
                    viewHolder.buttonApply.setBackgroundColor(Color.parseColor("#4040ff"))
                    viewHolder.buttonApply.text = "퇴소"
                }
            }

            if (userApply == 5) { // 중도퇴소 요청
                dataBaseAll.retrieveField(userSerialnum, "userplace") { placeText ->
                    viewHolder.placeEditText.setText(placeText)
                    viewHolder.placeEditText.isEnabled = false
                    viewHolder.buttonApply.setBackgroundColor(Color.parseColor("#ff4040"))
                    viewHolder.buttonApply.text = "중도\n퇴소"
                }
            }

            viewHolder.buttonApply.setOnClickListener {
                if (userApply == 1) { // 등록 요청
                    if (userSerialnum != null) {
                        val place = viewHolder.placeEditText.text.toString()
                        if (place == "") { // 관리자 부대 미입력 시
                            Toast.makeText(context, "부대를 등록해주세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            dataBaseAll.updateUserApply(userSerialnum, 2) // 등록 처리
                            dataBaseAll.updateUserField(userSerialnum, "userplace", place)
                            // 업데이트 후 리스트뷰 갱신
                            notifyDataSetChanged()
                            val intent = Intent(context, Manager_2_ApplyActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }
                    }
                }
                if (userApply == 3) {
                    dataBaseAll.updateUserApply(userSerialnum, 4) // 퇴소 처리
                    // 업데이트 후 리스트뷰 갱신
                    notifyDataSetChanged()
                    val intent = Intent(context, Manager_2_ApplyActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                if (userApply == 5) {
                    // 업데이트 후 리스트뷰 갱신
                    dataBaseAll.updateUserApply(userSerialnum, 6) // 중도 퇴소 처리
                    notifyDataSetChanged()
                    val intent = Intent(context, Manager_2_ApplyActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        }
        return view
    }

    private class ViewHolder(view: View) {
        val nameTextView: TextView = view.findViewById(R.id.textView_name)
        val armynumTextView: TextView = view.findViewById(R.id.textView_armynum)
        val placeEditText: EditText = view.findViewById(R.id.edittext_place)
        val buttonApply: Button = view.findViewById(R.id.button_apply)
    }
}