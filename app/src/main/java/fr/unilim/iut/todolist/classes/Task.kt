package fr.unilim.iut.todolist.classes;

data class Task (
    var id: Int,
    val desc: String,
    val state: Int,
    val date: String,
)
