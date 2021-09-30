package com.sunnyweather.android.ui.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.dao.UserDao
import com.sunnyweather.android.logic.model.User
import com.sunnyweather.android.ui.diary.DiaryActivity

class RegisterActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        userPhotoSetImg.setImageURI(it)
    }

    private lateinit var userNameEdit: EditText
    private lateinit var phoneNumberEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var userPhotoSetImg: ImageView
    private lateinit var registerBtn: Button

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userNameEdit = findViewById(R.id.userNameEdit)
        phoneNumberEdit = findViewById(R.id.phoneNumberEdit)
        passwordEdit = findViewById(R.id.passwordEdit)
        userPhotoSetImg = findViewById(R.id.userPhotoSetImg)
        registerBtn = findViewById(R.id.registerBtn)

        userPhotoSetImg.setOnClickListener {
            getContent.launch("image/*")
        }

        registerBtn.setOnClickListener {
            val username = userNameEdit.text.toString()
            val phoneNumber = phoneNumberEdit.text.toString()
            val password = passwordEdit.text.toString()
            val image = userPhotoSetImg.drawToBitmap()
            if (username != "" && phoneNumber != "" && password != "") {
                if (userViewModel.searchUserByPhoneNumber(phoneNumber) == null) {
                    val user = User(username, password, phoneNumber, image, "", "", "")
                    userViewModel.saveUser(user)
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                    finish()
                }else {
                    Toast.makeText(this, "该手机号已被注册！", Toast.LENGTH_SHORT).show()
                    phoneNumberEdit.setText("")
                }

            }
        }
    }

}