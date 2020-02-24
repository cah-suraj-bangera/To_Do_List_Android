package com.example.todolisdt

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
        var title_Text : String = ""
        var desc_Text : String = ""
        var toDoItemList = ArrayList<ToDoItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builderTitle: AlertDialog.Builder = AlertDialog.Builder(this)
        val builderDesc: AlertDialog.Builder = AlertDialog.Builder(this)

        val inputTitle = EditText(this)
        val inputDesc = EditText(this)

        inputTitle.inputType = InputType.TYPE_CLASS_TEXT
        builderTitle.setView(inputTitle)

        inputDesc.inputType = InputType.TYPE_CLASS_TEXT
        builderDesc.setView(inputDesc)

        loadData()

        createRecyclerView()

        initializeBuilders(builderTitle,builderDesc,inputTitle,inputDesc)

        fab.setOnClickListener {
            builderTitle.show()
        }

//        addSamples()
    }

    private fun addSamples() {

        val toDoItem1 = ToDoItem("Hiking","0")
        val toDoItem2 = ToDoItem("Hiking","1")
        val toDoItem3 = ToDoItem("Hiking","2")
        val toDoItem4 = ToDoItem("Hiking","3")

        toDoItemList.add(toDoItem1)
        toDoItemList.add(toDoItem2)
        toDoItemList.add(toDoItem3)
        toDoItemList.add(toDoItem4)
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        saveData()
    }


    private fun createRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = ToDoItemAdapter(toDoItemList) {
            Toast.makeText(this,"${it.title} Clicked !!!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun initializeBuilders(builderTitle : AlertDialog.Builder,builderDesc : AlertDialog.Builder,inputTitle : EditText,inputDesc : EditText) {

        builderTitle.setTitle("Add new Task")
        builderDesc.setTitle("Add Description of your new Task")

        builderTitle.setPositiveButton("OK") { dialog, which ->
            title_Text = inputTitle.text.toString()
            builderDesc.show()
            inputDesc.requestFocus()
        }

        builderTitle.setNegativeButton("Cancel") { dialog, which ->
            inputTitle.text.clear()
            dialog.cancel()}

        builderDesc.setPositiveButton("OK") { dialog, which ->
            inputDesc.requestFocus()
            desc_Text = inputDesc.text.toString()
            if (desc_Text.length != 0)
            {
                desc_Text = "Description: $desc_Text"
            }
            toDoItemList.add(ToDoItem(title_Text,desc_Text))
            (recycler_view.adapter as ToDoItemAdapter).notifyDataSetChanged()
            inputTitle.text.clear()
            inputDesc.text.clear()
        }

        builderDesc.setNegativeButton("Cancel") { dialog, which ->
            inputTitle.text.clear()
            inputDesc.text.clear()
            dialog.cancel() }
    }

    private fun saveData()
    {
        var sharedPreferences : SharedPreferences = getSharedPreferences("Task Data", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        var gson = Gson()
        var json : String = gson.toJson(toDoItemList)
        editor.putString("task list",json)
        editor.apply()

    }

    private fun loadData()
    {
        var sharedPreferences : SharedPreferences = getSharedPreferences("Task Data", Context.MODE_PRIVATE)
        var gson = Gson()
        var json : String? = sharedPreferences.getString("task list", null)
//        var type : Type = TypeToken<ArrayList<ToDoItem>>().type
        if (!json.isNullOrEmpty())
        {
        val type = object : TypeToken<ArrayList<ToDoItem?>?>() {}.type
        toDoItemList = gson.fromJson(json , type)
        }
    }



}
