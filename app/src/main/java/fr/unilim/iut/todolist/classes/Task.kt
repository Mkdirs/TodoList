package fr.unilim.iut.todolist.classes;

import android.icu.util.Calendar
import fr.unilim.iut.todolist.R
import java.text.SimpleDateFormat

data class Task (
    var id: Int,
    val desc: String,
    val state: Int,
    val date: String,
    val project:String
){
    fun isOverdue():Boolean{
        if(date.isEmpty())
            return false

        val dateFormat = SimpleDateFormat("dd/MM/yyyy");
        val cal = Calendar.getInstance();
        val currentDate = cal.time

        val dueDate = dateFormat.parse(date)!!

        return (currentDate >= dueDate && state == R.string.task_status_awaiting) || (state == R.string.task_status_overdue)
    }
}
