package com.example.simpletodo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        val onClickListener = object : TaskItemAdapter.OnClickListener{
            override fun onClicked(position: Int) {
                var i = Intent(this@MainActivity, com.example.simpletodo.EditText :: class.java)
                i.putExtra("index",position)
                i.putExtra("task",listOfTasks.get(position))
                startActivityForResult(i,123)
            }
        }
        

        loadItems()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks,onLongClickListener,onClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.button).setOnClickListener{
            var userInputtedTask = findViewById<EditText>(R.id.addTaskField).text.toString()
            listOfTasks.add(userInputtedTask)
            adapter.notifyItemInserted(listOfTasks.size-1)
            findViewById<EditText>(R.id.addTaskField).setText("")
            saveItems()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 123){
            if (resultCode == Activity.RESULT_OK){
                var task = data?.getExtras()?.getString("task")
                var index = data?.getExtras()?.getInt("index")
                if (index != null ) {
                    if (task != null) {
                        listOfTasks.set(index,task)
                        saveItems()
                        recreate()
                    }
                }
            }
        }


    }
}
