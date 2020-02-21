package com.example.todolisdt

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
        var title_Text : String = ""
        var desc_Text : String = ""
        val toDoItemList = ArrayList<ToDoItem>()


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

        createRecyclerView()

        initializeBuilders(builderTitle,builderDesc,inputTitle,inputDesc)

        fab.setOnClickListener {
            builderTitle.show()
        }


//        val toDoItem1 = ToDoItem("Hiking","0")
//        val toDoItem2 = ToDoItem("Hiking","1")
//        val toDoItem3 = ToDoItem("Hiking","2")
//        val toDoItem4 = ToDoItem("Hiking","3")

//        toDoItemList.add(toDoItem1)
//        toDoItemList.add(toDoItem2)
//        toDoItemList.add(toDoItem3)
//        toDoItemList.add(toDoItem4)




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
            dialog.cancel()}
    }
}
