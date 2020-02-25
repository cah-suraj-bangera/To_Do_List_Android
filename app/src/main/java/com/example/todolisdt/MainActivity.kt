package com.example.todolisdt


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_new_item.*


class MainActivity : AppCompatActivity() {
//        var title_Text : String = ""
//        var desc_Text : String = ""
        var toDoItemList = ArrayList<ToDoItem>()
    private var mAuth: FirebaseAuth? = null
    var currentUser : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth!!.currentUser!!.uid
        Log.d("Current_User", currentUser)

        loadData()




        createRecyclerView()

        fab.setOnClickListener {
            var customDialog = BottomSheetDialog(this)
            customDialog.setContentView(R.layout.input_new_item)
            customDialog.show()

            customDialog.doneButton?.setOnClickListener {
                if (customDialog.newTitle.text.isNotEmpty() || customDialog.newDescription.text.isNotEmpty()) {
                    toDoItemList.add(ToDoItem(customDialog.newTitle.text.toString(),customDialog.newDescription.text.toString()))
                    recycler_view.adapter!!.notifyDataSetChanged()
                    saveData()
                }
                customDialog.dismiss()
            }
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

    private fun saveData()
    {
//        Log.d("SaveData","Entered")
        var sharedPreferences : SharedPreferences = getSharedPreferences("To Do Data", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPreferences.edit()
        var gson = Gson()
        var json : String = gson.toJson(toDoItemList)
//        editor.putString("uid",currentUser)
        editor.putString(currentUser,json)
        editor.apply()

    }

    private fun loadData()
    {
        var sharedPreferences : SharedPreferences = this.getSharedPreferences("To Do Data", Context.MODE_PRIVATE)
        var gson = Gson()
//        if(sharedPreferences.getString("uid","").equals(currentUser))
//        {
            var json : String? = sharedPreferences.getString(currentUser, null)
            if (!json.isNullOrEmpty())
            {
                val type = object : TypeToken<ArrayList<ToDoItem>>() {}.type
                toDoItemList = gson.fromJson(json , type)
            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_rght_menu_min_activity,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.title.equals("Sign Out"))
        {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this,LoginScreen::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)

    }


}
