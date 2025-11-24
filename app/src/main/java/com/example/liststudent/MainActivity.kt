package com.example.liststudent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtMssv: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var listView: ListView

    private val students = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    private var selectedIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtMssv = findViewById(R.id.edtMssv)
        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        listView = findViewById(R.id.listView)

        adapter = StudentAdapter(this, students, object : StudentAdapter.OnStudentClick {

            override fun onItemClick(position: Int) {
                selectedIndex = position
                val s = students[position]

                edtName.setText(s.name)
                edtMssv.setText(s.mssv)
            }

            override fun onDelete(position: Int) {
                students.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        })

        listView.adapter = adapter

        btnAdd.setOnClickListener {
            val name = edtName.text.toString()
            val mssv = edtMssv.text.toString()

            if (name.isNotEmpty() && mssv.isNotEmpty()) {
                students.add(Student(name, mssv))
                adapter.notifyDataSetChanged()

                edtName.setText("")
                edtMssv.setText("")
            }
        }

        btnUpdate.setOnClickListener {
            if (selectedIndex != -1) {
                students[selectedIndex].name = edtName.text.toString()
                students[selectedIndex].mssv = edtMssv.text.toString()

                adapter.notifyDataSetChanged()
            }
        }
    }
}