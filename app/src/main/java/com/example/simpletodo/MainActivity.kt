package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {

    var listofTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongCLickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. move the item from the list
                listofTasks.removeAt(position)
                //2. notify the adapter that something has changed
                adapter.notifyDataSetChanged()

                saveItems()

            }
        }

        //1. let's detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            Log.i("Himaja", "User clicked on button")
//        }

        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listofTasks, onLongCLickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        findViewById<Button>(R.id.button).setOnClickListener {
            //1. Grab the text that the user has inputted into addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //2. Add the string to our listOfTasks
            listofTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listofTasks.size - 1)

            //3.  Reset text field
            inputTextField.setText("")

            saveItems()
        }

    }

    // Save the data that the user has inputted
    // Save data by reading and writing from a file

    // Get the file we need
    fun getDataFile(): File {

        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    // Load the items by reading every line in the data file
    fun loadItems() {
        try {
            listofTasks = org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }


    // Save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listofTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}