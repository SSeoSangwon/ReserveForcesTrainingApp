package edu.skku.cs.reserveforcestraining

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Manager_3_ManageActivity : AppCompatActivity() {

    private lateinit var listViewManage: ListView
    private lateinit var dataBaseAll: DataBase_ALL
    private lateinit var manageList: MutableList<DataBase_ALL.User2>
    private lateinit var manageListAdapter: ManageListAdapter
    private lateinit var button_refresh: ImageButton
    private lateinit var button_back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manager_3_manage)
        listViewManage = findViewById(R.id.listview_managelist)
        button_refresh = findViewById(R.id.imageButton_refresh)
        button_back = findViewById(R.id.imageButton_back)

        dataBaseAll = DataBase_ALL()
        manageList = mutableListOf()
        manageListAdapter = ManageListAdapter(this, manageList, dataBaseAll)
        listViewManage.adapter = manageListAdapter

        // 새로고침 버튼
        button_refresh.setOnClickListener {
            refreshPage()
        }

        // 뒤로가기 버튼
        button_back.setOnClickListener {
            onBackPressed()
        }

        // 관리 리스트 출력
        loadManageList()
    }

    // 관리 리스트 출력
    private fun loadManageList() {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                manageList.clear()
                for (userSnapshot in snapshot.children) {
                    val userarmynum = userSnapshot.key
                    val username = userSnapshot.child("username").getValue(String::class.java)
                    val userapply = userSnapshot.child("userapply").getValue(Int::class.java)
                    val userplace = userSnapshot.child("userplace").getValue(String::class.java)
                    if (userarmynum != null && username != null && userapply != null && userplace != null) {
                        val user = DataBase_ALL.User2(userarmynum, username, userapply, userplace)
                        if (user.userapply == 0 || user.userapply == 1 || user.userapply == 2) { // 0:기본 / 1:등록 요청 / 2: 등록 처리
                            manageList.add(user)
                        }
                    }
                }
                manageListAdapter.notifyDataSetChanged()
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

class ManageListAdapter(
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
                LayoutInflater.from(context).inflate(R.layout.manager_3_manage_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val user = userList[position]

        viewHolder.nameTextView.text = user.username
        viewHolder.armynumTextView.text = user.userarmynum
        viewHolder.placeTextView.text = user.userplace

        if (user.userapply == 0 || user.userapply == 1) { // 기본, 등록 요청 상태일 시 버튼색 회색으로 변경
            viewHolder.buttonInput.setBackgroundColor(Color.parseColor("#404040"))
            viewHolder.buttonCorrection.setBackgroundColor(Color.parseColor("#404040"))
        }

        // 입력 버튼
        viewHolder.buttonInput.setOnClickListener {
            if (user.userapply == 0 || user.userapply == 1) { // 액티비티 이동 X -> 등록 우선
                Toast.makeText(context, "아직 등록되지 않은 장병입니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 점수 입력 액티비티로 이동
                val scoreinputintent =
                    Intent(context, Manager_3_ManageScoreInputActivity::class.java)
                scoreinputintent.putExtra("USERNAME", user.username)
                scoreinputintent.putExtra("USERARMYNUM", user.userarmynum)
                context.startActivity(scoreinputintent)
            }
        }

        // 수정 버튼
        viewHolder.buttonCorrection.setOnClickListener {
            val userSerialnum = user.userarmynum
            if (userSerialnum != null) {
                if (user.userapply == 0 || user.userapply == 1) { // 액티비티 이동 X -> 등록 우선
                    Toast.makeText(context, "아직 등록되지 않은 장병입니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val place = viewHolder.placeTextView.text.toString()
                    // 부대 수정
                    val dialogBuilder = AlertDialog.Builder(context)
                    dialogBuilder.setTitle("수정")
                    dialogBuilder.setMessage("부대 수정 내용을 입력하세요.")
                    val editText = EditText(context)
                    editText.setText(place)
                    dialogBuilder.setView(editText)
                    dialogBuilder.setPositiveButton("확인") { dialog, which ->
                        val updatedPlace = editText.text.toString()
                        dataBaseAll.updateUserField(userSerialnum, "userplace", updatedPlace)
                        // 업데이트 후 리스트뷰 갱신
                        notifyDataSetChanged()
                        val intent = Intent(context, Manager_3_ManageActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                    dialogBuilder.setNegativeButton("취소", null)
                    val dialog = dialogBuilder.create()
                    dialog.setOnShowListener {
                        val layoutParams = editText.layoutParams as ViewGroup.MarginLayoutParams
                        layoutParams.marginStart =
                            context.resources.getDimensionPixelSize(R.dimen.dialog_edittext_margin)
                        layoutParams.marginEnd =
                            context.resources.getDimensionPixelSize(R.dimen.dialog_edittext_margin)
                        editText.layoutParams = layoutParams
                    }
                    dialog.show()
                }
            }
        }

        // 삭제 버튼
        viewHolder.buttonDelete.setOnClickListener {
            val userSerialnum = user.userarmynum
            if (userSerialnum != null) {
                val deletedialogBuilder = AlertDialog.Builder(context)
                deletedialogBuilder.setTitle("삭제")
                deletedialogBuilder.setMessage("정말로 삭제하시겠습니까?")
                deletedialogBuilder.setPositiveButton("확인") { dialog, which ->
                    dataBaseAll.deleteFirebaseUser(userSerialnum)
                    notifyDataSetChanged()
                    val intent = Intent(context, Manager_3_ManageActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
                deletedialogBuilder.setNegativeButton("취소", null)
                val deletedialog = deletedialogBuilder.create()
                deletedialog.show()
            }
        }
        return view
    }

    private class ViewHolder(view: View) {
        val nameTextView: TextView = view.findViewById(R.id.textView_name)
        val armynumTextView: TextView = view.findViewById(R.id.textView_armynum)
        val placeTextView: TextView = view.findViewById(R.id.textView_place)
        val buttonDelete: Button = view.findViewById(R.id.button_delete)
        val buttonCorrection: Button = view.findViewById(R.id.button_correction)
        val buttonInput: Button = view.findViewById(R.id.button_input)
    }
}