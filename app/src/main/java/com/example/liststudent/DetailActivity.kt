package com.example.liststudent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    // Khai báo biến View
    private lateinit var tvTitle: TextView
    private lateinit var edtMssvDetail: EditText
    private lateinit var edtNameDetail: EditText
    private lateinit var edtPhoneDetail: EditText
    private lateinit var edtAddressDetail: EditText
    private lateinit var btnSave: Button

    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Phải có file layout này

        // 1. Ánh xạ View (findViewById)
        tvTitle = findViewById(R.id.tvTitle)
        edtMssvDetail = findViewById(R.id.edtMssvDetail)
        edtNameDetail = findViewById(R.id.edtNameDetail)
        edtPhoneDetail = findViewById(R.id.edtPhoneDetail)
        edtAddressDetail = findViewById(R.id.edtAddressDetail)
        btnSave = findViewById(R.id.btnSave)

        // 2. Nhận dữ liệu từ MainActivity
        val receivedStudent = intent.getSerializableExtra("student_data") as? Student

        if (receivedStudent != null) {
            isEditMode = true

            // Hiển thị dữ liệu
            edtMssvDetail.setText(receivedStudent.mssv)
            edtNameDetail.setText(receivedStudent.name)
            edtPhoneDetail.setText(receivedStudent.phone)
            edtAddressDetail.setText(receivedStudent.address)

            // Khóa MSSV khi sửa
            edtMssvDetail.isEnabled = false

            tvTitle.text = "Cập Nhật Thông Tin"
            btnSave.text = "Cập Nhật"
        } else {
            isEditMode = false
            tvTitle.text = "Thêm Sinh Viên Mới"
            btnSave.text = "Thêm"
        }

        // 3. Xử lý nút Lưu
        btnSave.setOnClickListener {
            saveStudent()
        }
    }

    private fun saveStudent() {
        val mssv = edtMssvDetail.text.toString().trim()
        val name = edtNameDetail.text.toString().trim()
        val phone = edtPhoneDetail.text.toString().trim()
        val address = edtAddressDetail.text.toString().trim()

        if (mssv.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "MSSV và Họ tên không được để trống.", Toast.LENGTH_SHORT).show()
            return
        }

        val resultStudent = Student(mssv, name, phone, address)

        val resultIntent = Intent()
        resultIntent.putExtra("result_student", resultStudent)
        resultIntent.putExtra("is_edit", isEditMode)
        // Gửi kèm MSSV gốc phòng khi cần
        if (isEditMode) {
            resultIntent.putExtra("original_mssv", mssv)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}