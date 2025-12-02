package com.example.liststudent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val students = mutableListOf<Student>()
    private lateinit var adapter: StudentAdapter

    private val studentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val student = data?.getSerializableExtra("result_student") as? Student
            val isEdit = data?.getBooleanExtra("is_edit", false) ?: false

            if (student != null) {
                if (isEdit) {
                    val index = students.indexOfFirst { it.mssv == student.mssv }
                    if (index != -1) {
                        students[index] = student
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        // Trường hợp đặc biệt nếu MSSV bị đổi (dù đã khóa ở UI nhưng code vẫn nên xử lý)
                        val originalMssv = data?.getStringExtra("original_mssv")
                        val oldIndex = students.indexOfFirst { it.mssv == originalMssv }
                        if (oldIndex != -1) {
                            students[oldIndex] = student
                            adapter.notifyDataSetChanged()
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    if (students.any { it.mssv == student.mssv }) {
                        Toast.makeText(this, "MSSV đã tồn tại", Toast.LENGTH_SHORT).show()
                    } else {
                        students.add(student)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this, "Thêm mới thành công", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)

        // Dữ liệu mẫu
        students.add(Student("20200001", "Nguyễn Văn A", "0901234567", "Hà Nội"))

        adapter = StudentAdapter(this, students, object : StudentAdapter.OnStudentClick {
            override fun onItemClick(position: Int) {
                val student = students[position]
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("student_data", student)
                studentLauncher.launch(intent)
            }

            override fun onDelete(position: Int) {
                students.removeAt(position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Đã xóa", Toast.LENGTH_SHORT).show()
            }
        })

        listView.adapter = adapter
    }

    // Tạo Option Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Xử lý sự kiện chọn Menu (Đã sửa lỗi cú pháp tại đây)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, DetailActivity::class.java)
                studentLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}