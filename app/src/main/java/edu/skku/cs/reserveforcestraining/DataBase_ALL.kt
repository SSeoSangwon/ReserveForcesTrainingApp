package edu.skku.cs.reserveforcestraining

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataBase_ALL {

    // 유저 클래스
    data class User(
        val username: String = "",
        val userflag: Int = 0,
        val userbirth: String = "",
        val userarmynum: String = "",
        val userphonenum: String = "",
        val userpw: String = "",
        val userapply: Int = 0,
        val userplace: String = "",
        val training1time: String = "",
        val training1score: Int = 0,
        val training2time: String = "",
        val training2score: Int = 0,
        val training3time: String = "",
        val training3score: Int = 0,
        val training4time: String = "",
        val training4score: Int = 0,
        val training5time: String = "",
        val training5score: Int = 0,
        val training6time: String = "",
        val training6score: Int = 0,
        val training7time: String = "",
        val training7score: Int = 0
    )

    data class User2(
        val userarmynum: String,
        val username: String,
        var userapply: Int,
        val userplace: String
    )

    // 데이터 삽입
    fun insertUser(user: User) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        val userRef = usersRef.child(user.userarmynum) // 군번을 사용하여 참조 경로 생성
        userRef.child("username").setValue(user.username)
        userRef.child("userflag").setValue(user.userflag)
        userRef.child("userbirth").setValue(user.userbirth)
        userRef.child("userarmynum").setValue(user.userarmynum)
        userRef.child("userphonenum").setValue(user.userphonenum)
        userRef.child("userpw").setValue(user.userpw)
        userRef.child("userapply").setValue(user.userapply)
        userRef.child("userplace").setValue(user.userplace)
        userRef.child("training1time").setValue(user.training1time)
        userRef.child("training1score").setValue(user.training1score)
        userRef.child("training2time").setValue(user.training2time)
        userRef.child("training2score").setValue(user.training2score)
        userRef.child("training3time").setValue(user.training3time)
        userRef.child("training3score").setValue(user.training3score)
        userRef.child("training4time").setValue(user.training4time)
        userRef.child("training4score").setValue(user.training4score)
        userRef.child("training5time").setValue(user.training5time)
        userRef.child("training5score").setValue(user.training5score)
        userRef.child("training6time").setValue(user.training6time)
        userRef.child("training6score").setValue(user.training6score)
        userRef.child("training7time").setValue(user.training7time)
        userRef.child("training7score").setValue(user.training7score)
    }

    // 데이터 삭제
    fun deleteFirebaseUser(userSerialnum: String) {
        val firebaseRef = FirebaseDatabase.getInstance().getReference("users")
        firebaseRef.child(userSerialnum).removeValue()
    }

    //요청데이터 업데이트 0:기본 1:등록신청상태 2:등록완료 3:조건완료퇴소신청 4:퇴소 5:조건미완료퇴소신청 6:중도퇴소
    fun updateUserApply(serialNumber: String, userApply: Int) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        val userRef = usersRef.child(serialNumber)
        userRef.child("userapply").setValue(userApply)
    }

    // 데이터 반환
    fun retrieveField(serialNumber: String, field: String, callback: (String?) -> Unit) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        val userRef = usersRef.child(serialNumber)
        userRef.child(field).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue()?.toString()
                callback(value)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 데이터 업데이트
    fun updateUserField(serialNumber: String, field: String, value: Any) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        val userRef = usersRef.child(serialNumber)
        userRef.child(field).setValue(value)
    }

    // 관리자 - 초기에만 사용
    data class Manager(
        val managerid: String,
        val managerpw: String
    )

    // 관리자 삽입 - 초기에만 사용
    fun insertManager(manager: Manager) {
        val managersRef = FirebaseDatabase.getInstance().getReference("manager")
        val managerRef = managersRef.child(manager.managerid)
        managerRef.child("managerid").setValue(manager.managerid)
        managerRef.child("managerpw").setValue(manager.managerpw)
    }

    // 관리자 비밀번호 반환
    fun retrieveManagerPW(managerid: String, callback: (String?) -> Unit) {
        val managersRef = FirebaseDatabase.getInstance().getReference("manager")
        val managerRef = managersRef.child(managerid)
        managerRef.child("managerpw").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue()?.toString()
                callback(value)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    // 관리자 비밀번호 업데이트
    fun updateManagerPW(managerid: String, value: Any) {
        val managersRef = FirebaseDatabase.getInstance().getReference("manager")
        val managerRef = managersRef.child(managerid)
        managerRef.child("managerpw").setValue(value)
    }
}

// firebase로 만들기 전 SQLiteDB 코드
/*
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "mydatabase.db"
        const val DATABASE_VERSION = 1

        // 테이블 이름
        const val TABLE_NAME = "users"

        // 컬럼 이름
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_USERBIRTH = "userbirth"
        const val COLUMN_USERARMYNUM = "userarmynum"
        const val COLUMN_USERPHONENUM = "userphonenum"
        const val COLUMN_USERPW = "userpw"
        const val COLUMN_USERAPPLY = "userapply"
        const val COLUMN_USERPLACE = "userplace"
        const val COLUMN_TRAINING1TIME = "training1time"
        const val COLUMN_TRAINING1SCORE = "training1score"
        const val COLUMN_TRAINING2TIME = "training2time"
        const val COLUMN_TRAINING2SCORE = "training2score"
        const val COLUMN_TRAINING3TIME = "training3time"
        const val COLUMN_TRAINING3SCORE = "training3score"
        const val COLUMN_TRAINING4TIME = "training4time"
        const val COLUMN_TRAINING4SCORE = "training4score"
        const val COLUMN_TRAINING5TIME = "training5time"
        const val COLUMN_TRAINING5SCORE = "training5score"
        const val COLUMN_TRAINING6TIME = "training6time"
        const val COLUMN_TRAINING6SCORE = "training6score"
        const val COLUMN_TRAINING7TIME = "training7time"
        const val COLUMN_TRAINING7SCORE = "training7score"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_USERBIRTH TEXT, " +
                "$COLUMN_USERARMYNUM TEXT, " +
                "$COLUMN_USERPHONENUM TEXT, " +
                "$COLUMN_USERPW TEXT, " +
                "$COLUMN_USERAPPLY INTEGER, " +
                "$COLUMN_USERPLACE TEXT, " +
                "$COLUMN_TRAINING1TIME TEXT, " +
                "$COLUMN_TRAINING1SCORE INTEGER, " +
                "$COLUMN_TRAINING2TIME TEXT, " +
                "$COLUMN_TRAINING2SCORE INTEGER, " +
                "$COLUMN_TRAINING3TIME TEXT, " +
                "$COLUMN_TRAINING3SCORE INTEGER, " +
                "$COLUMN_TRAINING4TIME TEXT, " +
                "$COLUMN_TRAINING4SCORE INTEGER, " +
                "$COLUMN_TRAINING5TIME TEXT, " +
                "$COLUMN_TRAINING5SCORE INTEGER, " +
                "$COLUMN_TRAINING6TIME TEXT, " +
                "$COLUMN_TRAINING6SCORE INTEGER, " +
                "$COLUMN_TRAINING7TIME TEXT, " +
                "$COLUMN_TRAINING7SCORE INTEGER)"
        db.execSQL(createTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(user: User, context: Context) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, user.username)
            put(COLUMN_USERBIRTH, user.userbirth)
            put(COLUMN_USERARMYNUM, user.userarmynum)
            put(COLUMN_USERPHONENUM, user.userphonenum)
            put(COLUMN_USERPW, user.userpw)
            put(COLUMN_USERAPPLY, user.userapply)
            put(COLUMN_USERPLACE, user.userplace)
            put(COLUMN_TRAINING1TIME, user.training1time)
            put(COLUMN_TRAINING1SCORE, user.training1score)
            put(COLUMN_TRAINING2TIME, user.training2time)
            put(COLUMN_TRAINING2SCORE, user.training2score)
            put(COLUMN_TRAINING3TIME, user.training3time)
            put(COLUMN_TRAINING3SCORE, user.training3score)
            put(COLUMN_TRAINING4TIME, user.training4time)
            put(COLUMN_TRAINING4SCORE, user.training4score)
            put(COLUMN_TRAINING5TIME, user.training5time)
            put(COLUMN_TRAINING5SCORE, user.training5score)
            put(COLUMN_TRAINING6TIME, user.training6time)
            put(COLUMN_TRAINING6SCORE, user.training6score)
            put(COLUMN_TRAINING7TIME, user.training7time)
            put(COLUMN_TRAINING7SCORE, user.training7score)
        }
        db.insert(TABLE_NAME, null, values)
        updateUserIds()
        migrateDataFromSQLiteToFirebase(context)
    }

    data class User(
        val id: Int,
        val username: String,
        val userbirth: String,
        val userarmynum: String,
        val userphonenum: String,
        val userpw: String,
        val userapply: Int,
        val userplace: String,
        val training1time: String,
        val training1score: Int,
        val training2time: String,
        val training2score: Int,
        val training3time: String,
        val training3score: Int,
        val training4time: String,
        val training4score: Int,
        val training5time: String,
        val training5score: Int,
        val training6time: String,
        val training6score: Int,
        val training7time: String,
        val training7score: Int,
    )

    fun getNextUserId(): Int {
        val db = readableDatabase
        val query = "SELECT MAX($COLUMN_ID) FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        val nextUserId = if (cursor.moveToFirst()) {
            val maxUserId = cursor.getInt(0)
            maxUserId + 1
        } else {
            1
        }
        cursor.close()
        return nextUserId
    }

    fun updateUserApply1(serialNumber: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERAPPLY, 1) // userapply 값을 1로 업데이트
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun updateUserApply2(serialNumber: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERAPPLY, 2) // userapply 값을 2로 업데이트
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun updateUserApply3(serialNumber: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERAPPLY, 3) // userapply 값을 3로 업데이트
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun updateUserApply4(serialNumber: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERAPPLY, 4) // userapply 값을 4로 업데이트
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun updateUserApply5(serialNumber: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERAPPLY, 5) // userapply 값을 4로 업데이트
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun updateUserApply6(serialNumber: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERAPPLY, 6) // userapply 값을 6로 업데이트
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun retrieveUserApply(serialNumber: String): Int {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_USERAPPLY),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val userApply = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex(COLUMN_USERAPPLY))
        } else {
            0
        }
        cursor.close()
        return userApply
    }

    @SuppressLint("Range")
    fun retrieveName(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_USERNAME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val name = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
        } else {
            null
        }
        cursor.close()
        return name
    }

    @SuppressLint("Range")
    fun retrieveplace(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_USERPLACE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val place = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_USERPLACE))
        } else {
            null
        }
        cursor.close()
        return place
    }

    @SuppressLint("Range")
    fun updateUserPlace(userarmynum: String, place: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERPLACE, place)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserTime1(userarmynum: String, time1: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING1TIME, time1)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserScore1(userarmynum: String, score1: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING1SCORE, score1)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserTime2(userarmynum: String, time2: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING2TIME, time2)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)

    }

    @SuppressLint("Range")
    fun updateUserScore2(userarmynum: String, score2: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING2SCORE, score2)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserTime3(userarmynum: String, time3: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING3TIME, time3)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserScore3(userarmynum: String, score3: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING3SCORE, score3)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserTime4(userarmynum: String, time4: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING4TIME, time4)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)

    }

    @SuppressLint("Range")
    fun updateUserScore4(userarmynum: String, score4: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING4SCORE, score4)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserTime5(userarmynum: String, time5: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING5TIME, time5)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserScore5(userarmynum: String, score5: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING5SCORE, score5)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserTime6(userarmynum: String, time6: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING6TIME, time6)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserScore6(userarmynum: String, score6: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING6SCORE, score6)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserTime7(userarmynum: String, time7: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING7TIME, time7)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun updateUserScore7(userarmynum: String, score7: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TRAINING7SCORE, score7)
        }
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(userarmynum)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun retrievetime1(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING1TIME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val time1 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING1TIME))
        } else {
            null
        }
        cursor.close()
        return time1
    }

    @SuppressLint("Range")
    fun retrievescore1(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING1SCORE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val score1 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING1SCORE))
        } else {
            null
        }
        cursor.close()
        return score1
    }

    @SuppressLint("Range")
    fun retrievetime2(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING2TIME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val time2 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING2TIME))
        } else {
            null
        }
        cursor.close()
        return time2
    }

    @SuppressLint("Range")
    fun retrievescore2(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING2SCORE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val score2 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING2SCORE))
        } else {
            null
        }
        cursor.close()
        return score2
    }

    @SuppressLint("Range")
    fun retrievetime3(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING3TIME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val time3 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING3TIME))
        } else {
            null
        }
        cursor.close()
        return time3
    }

    @SuppressLint("Range")
    fun retrievescore3(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING3SCORE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val score3 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING3SCORE))
        } else {
            null
        }
        cursor.close()
        return score3
    }

    @SuppressLint("Range")
    fun retrievetime4(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING4TIME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val time4 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING4TIME))
        } else {
            null
        }
        cursor.close()
        return time4
    }

    @SuppressLint("Range")
    fun retrievescore4(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING4SCORE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val score4 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING4SCORE))
        } else {
            null
        }
        cursor.close()
        return score4
    }

    @SuppressLint("Range")
    fun retrievetime5(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING5TIME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val time5 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING5TIME))
        } else {
            null
        }
        cursor.close()
        return time5
    }

    @SuppressLint("Range")
    fun retrievescore5(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING5SCORE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val score5 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING5SCORE))
        } else {
            null
        }
        cursor.close()
        return score5
    }

    @SuppressLint("Range")
    fun retrievetime6(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING6TIME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val time6 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING6TIME))
        } else {
            null
        }
        cursor.close()
        return time6
    }

    @SuppressLint("Range")
    fun retrievescore6(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING6SCORE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val score6 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING6SCORE))
        } else {
            null
        }
        cursor.close()
        return score6
    }

    @SuppressLint("Range")
    fun retrievetime7(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING7TIME),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val time7 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING7TIME))
        } else {
            null
        }
        cursor.close()
        return time7
    }

    @SuppressLint("Range")
    fun retrievescore7(serialNumber: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERARMYNUM = ?"
        val selectionArgs = arrayOf(serialNumber)
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_TRAINING7SCORE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val score7 = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndex(COLUMN_TRAINING7SCORE))
        } else {
            null
        }
        cursor.close()
        return score7
    }

    @SuppressLint("Range")
    fun getAllUsers(): List<User2> {
        val userList = mutableListOf<User2>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val userarmynum = it.getString(it.getColumnIndex(COLUMN_USERARMYNUM))
                    val username = it.getString(it.getColumnIndex(COLUMN_USERNAME))
                    val userapply = it.getInt(it.getColumnIndex(COLUMN_USERAPPLY))
                    val userplace = it.getString(it.getColumnIndex(COLUMN_USERPLACE))
                    val user = User2(userarmynum, username, userapply, userplace)
                    userList.add(user)
                } while (it.moveToNext())
            }
            it.close()
        }

        return userList
    }

    fun deleteUser(userSerialnum: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_USERARMYNUM = ?", arrayOf(userSerialnum))
        db.close()
        updateUserIds() // 아이디 재조정
    }

    private fun updateUserIds() {
        val db = writableDatabase
        val userList = getAllUsers()

        for (i in userList.indices) {
            val user = userList[i]
            val values = ContentValues().apply {
                put(COLUMN_ID, i + 1)
            }
            val selection = "$COLUMN_USERARMYNUM = ?"
            val selectionArgs = arrayOf(user.userarmynum)
            db.update(TABLE_NAME, values, selection, selectionArgs)
        }
        db.close()
    }

    data class User2(
        val userarmynum: String,
        val username: String,
        var userapply: Int,
        val userplace: String
    )


    @SuppressLint("Range")
    fun migrateDataFromSQLiteToFirebase(context: Context) {
        val dbHelper = DataBase_ALL(context)
        val db = dbHelper.readableDatabase
        val cursor = db.query(DataBase_ALL.TABLE_NAME, null, null, null, null, null, null)

        val firebaseRef = FirebaseDatabase.getInstance().getReference("users")

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_ID))
                val username = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_USERNAME))
                val userbirth = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_USERBIRTH))
                val userarmynum = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_USERARMYNUM))
                val userphonenum = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_USERPHONENUM))
                val userpw = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_USERPW))
                val userapply = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_USERAPPLY))
                val userplace = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_USERPLACE))
                val training1time = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING1TIME))
                val training1score = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING1SCORE))
                val training2time = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING2TIME))
                val training2score = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING2SCORE))
                val training3time = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING3TIME))
                val training3score = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING3SCORE))
                val training4time = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING4TIME))
                val training4score = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING4SCORE))
                val training5time = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING5TIME))
                val training5score = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING5SCORE))
                val training6time = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING6TIME))
                val training6score = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING6SCORE))
                val training7time = cursor.getString(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING7TIME))
                val training7score = cursor.getInt(cursor.getColumnIndex(DataBase_ALL.COLUMN_TRAINING7SCORE))

                val user = User(
                    id,
                    username,
                    userbirth,
                    userarmynum,
                    userphonenum,
                    userpw,
                    userapply,
                    userplace,
                    training1time,
                    training1score,
                    training2time,
                    training2score,
                    training3time,
                    training3score,
                    training4time,
                    training4score,
                    training5time,
                    training5score,
                    training6time,
                    training6score,
                    training7time,
                    training7score
                )

                // Firebase Realtime Database에 데이터 저장
                firebaseRef.child(id.toString()).setValue(user)

                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()
        dbHelper.close()
    }

    fun migrateDataFromFirebaseToSQLite(context: Context) {
        val dbHelper = DataBase_ALL(context)
        val firebaseRef = FirebaseDatabase.getInstance().getReference("users")

        firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()

                for (dataSnapshot in snapshot.children) {
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.let { userList.add(it) }
                }

                dbHelper.writableDatabase.use { db ->
                    // Clear existing data in SQLite table
                    db.delete(DataBase_ALL.TABLE_NAME, null, null)

                    // Insert new data into SQLite table
                    for (user in userList) {
                        val values = ContentValues().apply {
                            put("username", user.username)
                            put("userbirth", user.userbirth)
                            put("userarmynum", user.userarmynum)
                            put("userphonenum", user.userphonenum)
                            put("userpw", user.userpw)
                            put("userapply", user.userapply)
                            put("userplace", user.userplace)
                            put("training1time", user.training1time)
                            put("training1score", user.training1score)
                            put("training2time", user.training2time)
                            put("training2score", user.training2score)
                            put("training3time", user.training3time)
                            put("training3score", user.training3score)
                            put("training4time", user.training4time)
                            put("training4score", user.training4score)
                            put("training5time", user.training5time)
                            put("training5score", user.training5score)
                            put("training6time", user.training6time)
                            put("training6score", user.training6score)
                            put("training7time", user.training7time)
                            put("training7score", user.training7score)
                        }
                        db.insert(DataBase_ALL.TABLE_NAME, null, values)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })
    }
}
*/