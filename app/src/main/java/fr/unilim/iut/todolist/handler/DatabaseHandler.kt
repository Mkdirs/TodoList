package fr.unilim.iut.todolist.handler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import fr.unilim.iut.todolist.classes.Task

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_TASK = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_DESC = "desc"
        private val KEY_STATE = "state"
        private val KEY_DATE = "date"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_TASK + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DESC + " TEXT,"
                + KEY_STATE + " TEXT," + KEY_DATE + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK)
        onCreate(db)
    }
    //method to insert data
    fun addEmployee(emp: Task):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.getID())
        contentValues.put(KEY_DESC, emp.getDesc())
        contentValues.put(KEY_STATE,emp.getState())
        contentValues.put(KEY_DATE, emp.getDate())
        // Inserting Row
        val success = db.insert(TABLE_TASK, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewEmployee():List<Task>{
        val empList:ArrayList<Task> = ArrayList<Task>()
        val selectQuery = "SELECT  * FROM $TABLE_TASK"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var desc: String
        var state: String
        var date: Long
        if (cursor.moveToFirst()) {
            do {
                desc = cursor.getString(cursor.getColumnIndex("desc"))
                state = cursor.getString(cursor.getColumnIndex("state"))
                date = cursor.getLong(cursor.getColumnIndex("date"))
                val emp= Task(
                    description = desc,
                    state = state,
                    date = date
                )
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }
    //method to update data
    fun updateEmployee(emp: Task):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.getID())
        contentValues.put(KEY_DESC, emp.getDesc())
        contentValues.put(KEY_STATE,emp.getState())
        contentValues.put(KEY_DATE, emp.getDate())

        // Updating Row
        val success = db.update(TABLE_TASK, contentValues,"id="+emp.getID(),null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteEmployee(emp: Task):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.getID()) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_TASK,"id="+emp.getID(),null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}