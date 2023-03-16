package fr.unilim.iut.todolist

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import fr.unilim.iut.todolist.adapter.MyListAdapter
import fr.unilim.iut.todolist.classes.Task
import fr.unilim.iut.todolist.handler.DatabaseHandler


class DBActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db)
    }
    //method for saving records in database
    fun saveRecord(view: View){
        val desc = findViewById<EditText>(R.id.t_desc).text.toString()
        val state = findViewById<EditText>(R.id.t_state).text.toString()
        val date = findViewById<EditText>(R.id.t_date).text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(date.trim()!="" && desc.trim()!="" && state.trim()!=""){
            val status = databaseHandler.addEmployee(
                Task(
                    0,
                    desc.trim(),
                    0,
                    date.trim(),
                )
            )
            if(status > -1){
                Toast.makeText(applicationContext,"record save",Toast.LENGTH_LONG).show()
                findViewById<EditText>(R.id.t_desc).text.clear()
                findViewById<EditText>(R.id.t_state).text.clear()
                findViewById<EditText>(R.id.t_date).text.clear()
            }
        }else{
            Toast.makeText(applicationContext,"one entry is blank !",Toast.LENGTH_LONG).show()
        }
    }
    //method for read records from database in ListView
    fun viewRecord(view: View){
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler= DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val emp: List<Task> = databaseHandler.viewEmployee()
        val empArrayId = Array<String>(emp.size){"0"}
        val empArrayDesc = Array<String>(emp.size){"null"}
        val empArrayState = Array<String>(emp.size){"null"}
        val empArrayDate = Array<String>(emp.size){"null"}
        var index = 0
        for(e in emp){
            empArrayId[index] = e.id.toString()
            empArrayDesc[index] = e.desc
            empArrayState[index] = e.state.toString()
            empArrayDate[index] = e.date
            index++
        }
        //creating custom ArrayAdapter
        val myListAdapter = MyListAdapter(
            this,
            empArrayId,
            empArrayDesc,
            empArrayState,
            empArrayDate
        )
        findViewById<ListView>(R.id.listView).adapter = myListAdapter
    }
    //method for updating records based on user id
    fun updateRecord(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val edtId = dialogView.findViewById(R.id.updateId) as EditText
        val edtDesc = dialogView.findViewById(R.id.updateDesc) as EditText
        val edtState = dialogView.findViewById(R.id.updateState) as EditText
        val edtDate = dialogView.findViewById(R.id.updateDate) as EditText

        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = edtId.text.toString()
            val updateDesc = edtDesc.text.toString()
            val updateState = edtState.text.toString()
            val updateDate = edtDate.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(updateId.trim()!="" && updateDesc.trim()!="" && updateState.trim()!="" && updateDate.trim()!=""){
                //calling the updateEmployee method of DatabaseHandler class to update record
                val status = databaseHandler.updateEmployee(
                    Task(
                        updateId.toInt(),
                        updateDesc,
                        updateState.toInt(),
                        updateDate,
                    )
                )
                if(status > -1){
                    Toast.makeText(applicationContext,"record update",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"id or desc or state or date cannot be blank",Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
    //method for deleting records based on id
    fun deleteRecord(view: View){
        //creating AlertDialog for taking user id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
        dialogBuilder.setTitle("Delete Record")
        dialogBuilder.setMessage("Enter id below")
        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->

            val deleteId = dltId.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(deleteId.trim()!=""){
                //calling the deleteEmployee method of DatabaseHandler class to delete record
                val status = databaseHandler.deleteEmployee(
                    Task(
                        Integer.parseInt(deleteId),
                        "",
                        0,
                        "",
                    )
                )
                if(status > -1){
                    Toast.makeText(applicationContext,"record deleted",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,"id cannot be blank",Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.  OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }

    fun showDatePickerDialog(v: View) {

        val dateSetListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                findViewById<EditText>(R.id.t_date).setText(
                    year.toString().padStart(4, '0') +
                            "-" + (monthOfYear + 1).toString().padStart(2, '0') +
                            "-" + dayOfMonth.toString().padStart(2, '0')
                )
            }
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            year,
            month,
            day
        )
        datePickerDialog.show();
    }
}