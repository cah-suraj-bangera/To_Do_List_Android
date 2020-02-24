package com.example.todolisdt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.input_new_item.*
import kotlinx.android.synthetic.main.view_item.view.*

class  ToDoItemAdapter (val toDoItemList: ArrayList<ToDoItem> , val listner : (ToDoItem) -> Unit) :
    RecyclerView.Adapter<ToDoItemAdapter.ToDoItemViewHolder>() {


    override fun getItemCount() = toDoItemList.size


    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        holder.bind(toDoItemList.get(position),listner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent ,false)
        return ToDoItemViewHolder (rootView)

    }
    inner class ToDoItemViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind (toDoItem: ToDoItem, listner : (ToDoItem) -> Unit) = with(itemView) {
            txvTitle.text = toDoItem.title
            txvDesc.text = toDoItem.desc
            setOnClickListener { listner(toDoItem)}
            itemView.imgShare.setOnClickListener(){
                toDoItemList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
            itemView.imgEdit.setOnClickListener {
                var customDialog = BottomSheetDialog(context)
                customDialog.setContentView(R.layout.input_new_item);
                customDialog.newTitle.setText(toDoItem.title)
                customDialog.newDescription.setText(toDoItem.desc)
                customDialog.show()

                customDialog.doneButton?.setOnClickListener {
                    toDoItem.title = customDialog.newTitle.text.toString()
                    toDoItem.desc = customDialog.newDescription.text.toString()
                    notifyItemChanged(adapterPosition)
                    customDialog.dismiss()
                }
            }

        }

    }
}