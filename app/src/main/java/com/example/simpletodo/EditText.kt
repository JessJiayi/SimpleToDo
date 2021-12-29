package com.example.simpletodo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class EditText : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        loadItems()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)
        setTitle("Edit Task")


        findViewById<EditText>(R.id.editTaskField).setText(getIntent().getStringExtra("task"))
        findViewById<Button>(R.id.buttonSave).setOnClickListener{
            val task = findViewById<EditText>(R.id.editTaskField).text.toString()
            val index = getIntent().getIntExtra("index",0)
            val data = Intent()
            data.putExtra("task",task)
            data.putExtra("index",index)
            setResult(RESULT_OK,data)
            finish()
        }
    }

    fun getDataFile() : File {
        return File(filesDir,"data.txt")
    }
    fun loadItems(){
        try{
            listOfTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
    fun saveItems(){
        try{
            org.apache.commons.io.FileUtils.writeLines(getDataFile(),listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}