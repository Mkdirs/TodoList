package fr.unilim.iut.todolist.handler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import fr.unilim.iut.todolist.classes.Project
import fr.unilim.iut.todolist.classes.Task

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "TodoDatabase"
        private val TABLE_TASK = "TodoTable"
        private val TABLE_PROJECT = "Project"
        private val KEY_ID = "id"
        private val KEY_DESC = "desc"
        private val KEY_STATE = "state"
        private val KEY_DATE = "date"
        private val KEY_PROJECT = "project"
        private val KEY_PROJECT_NAME = "name"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_PROJECTS_TABLE = ("CREATE TABLE " + TABLE_PROJECT + "("
                + KEY_PROJECT_NAME + " TEXT PRIMARY KEY)"
                )

        val CREATE_TASKS_TABLE = ("CREATE TABLE " + TABLE_TASK + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DESC + " TEXT,"
                + KEY_STATE + " INTEGER," + KEY_DATE + " DATE, "
                + KEY_PROJECT + " TEXT )"
                )
        db?.execSQL(CREATE_PROJECTS_TABLE)
        db?.execSQL(CREATE_TASKS_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK)
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PROJECT)
        onCreate(db)
    }
    //method to insert data
    fun addTask(emp: Task):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_DESC, emp.desc)
        contentValues.put(KEY_STATE,emp.state)
        contentValues.put(KEY_DATE, emp.date)
        contentValues.put(KEY_PROJECT, emp.project)
        // Inserting Row
        val success = db.insert(TABLE_TASK, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    fun addProject(project: Project) : Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_PROJECT_NAME, project.name)
        // Inserting Row
        val success = db.insert(TABLE_PROJECT, null, contentValues)
        db.close()
        return success
    }

    fun getTask(id:Int):Task?{
        val query = "SELECT * FROM $TABLE_TASK WHERE id=$id"
        val c = readableDatabase.rawQuery(query, null)

        if(!c.moveToFirst())
            return null

        return Task(
            c.getInt(c.getColumnIndex(KEY_ID)),
            c.getString(c.getColumnIndex(KEY_DESC)),
            c.getInt(c.getColumnIndex(KEY_STATE)),
            c.getString(c.getColumnIndex(KEY_DATE)),
            c.getString(c.getColumnIndex(KEY_PROJECT))

        )
    }

    fun getProject(name:String):Project?{
        val query = "SELECT * FROM $TABLE_PROJECT WHERE $KEY_PROJECT_NAME=$name"
        val c = readableDatabase.rawQuery(query, null)

        if(!c.moveToFirst())
            return null

        return Project(
            c.getString(c.getColumnIndex(KEY_PROJECT_NAME))

        )
    }

    //method to read data
    fun viewTasks(project:String):List<Task>{
        val empList:ArrayList<Task> = ArrayList<Task>()
        val selectQuery = "SELECT  * FROM $TABLE_TASK WHERE $KEY_PROJECT='$project'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var desc: String
        var state: Int
        var date: String
        var project: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                desc = cursor.getString(cursor.getColumnIndex(KEY_DESC))
                state = cursor.getInt(cursor.getColumnIndex(KEY_STATE))
                date = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                project = cursor.getString(cursor.getColumnIndex(KEY_PROJECT))
                val emp= Task(
                    id = id,
                    desc = desc,
                    state = state,
                    date = date,
                    project = project
                )
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    fun viewProjects():List<Project>{
        val projects = ArrayList<Project>()
        val selectQuery = "SELECT  * FROM $TABLE_PROJECT"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var name: String
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_NAME))

                projects.add(Project(name))
            } while (cursor.moveToNext())
        }
        return projects
    }

    //method to update data
    fun updateTask(emp: Task):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)
        contentValues.put(KEY_DESC, emp.desc)
        contentValues.put(KEY_STATE,emp.state)
        contentValues.put(KEY_DATE, emp.date)

        // Updating Row
        val success = db.update(TABLE_TASK, contentValues,"id="+emp.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }


    fun clearTasks() : Int{
        val rows = writableDatabase.delete(TABLE_TASK, null, null)
        writableDatabase.close()
        return rows
    }

    //method to delete data
    fun deleteTask(emp: Task):Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_TASK,"id="+emp.id,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    fun deleteProject(project:Project):Int{
        val db = this.writableDatabase
        val s1 = db.delete(TABLE_PROJECT,"$KEY_PROJECT_NAME=${project.name}",null)
        val s2 = db.delete(TABLE_TASK, "$KEY_PROJECT=${project.name}", null)
        db.close()
        return s1+s2
    }
}