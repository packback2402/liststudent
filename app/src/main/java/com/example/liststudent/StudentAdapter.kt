package com.example.liststudent

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class StudentAdapter(
    private val activity: Activity,
    private val list: MutableList<Student>,
    private val listener: OnStudentClick
) : ArrayAdapter<Student>(activity, R.layout.item_student, list) {

    interface OnStudentClick {
        fun onItemClick(position: Int)
        fun onDelete(position: Int)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = activity.layoutInflater.inflate(R.layout.item_student, parent, false)

        val txtName = row.findViewById<TextView>(R.id.txtName)
        val txtMssv = row.findViewById<TextView>(R.id.txtMssv)
        val btnDelete = row.findViewById<ImageView>(R.id.btnDelete)

        val s = list[position]

        txtName.text = s.name
        txtMssv.text = s.mssv

        row.setOnClickListener { listener.onItemClick(position) }
        btnDelete.setOnClickListener { listener.onDelete(position) }

        return row
    }
}