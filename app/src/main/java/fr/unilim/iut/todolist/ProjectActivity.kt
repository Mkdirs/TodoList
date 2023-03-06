package fr.unilim.iut.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ProjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        actionBar?.title = intent.getStringExtra(PROJECT_NAME)
    }
}