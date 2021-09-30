package com.sunnyweather.android.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sunnyweather.android.R
import android.content.Intent
import android.content.SharedPreferences
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.google.android.material.switchmaterial.SwitchMaterial
import com.sunnyweather.android.logic.model.User
import com.sunnyweather.android.ui.forum.ForumActivity

class LoginActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    private lateinit var phoneNumLoginEdit: EditText
    private lateinit var passwordLoginEdit: EditText
    private lateinit var userPhotoDisplayImg: ImageView
    private lateinit var toRegisterBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var rememberSwitch: SwitchMaterial

    private var userToLogin: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        phoneNumLoginEdit = findViewById(R.id.phoneNumLoginExit)
        passwordLoginEdit = findViewById(R.id.passwordLoginEdit)
        userPhotoDisplayImg = findViewById(R.id.userPhotoDisplayImg)
        toRegisterBtn = findViewById(R.id.toRegisterBtn)
        loginBtn = findViewById(R.id.loginBtn)
        rememberSwitch = findViewById(R.id.rememberSwitch)

        readRemember()
        loadUser()
        toRegisterBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        phoneNumLoginEdit.addTextChangedListener {
            loadUser()
        }

        loginBtn.setOnClickListener {
            if (userToLogin != null && passwordLoginEdit.text.toString() == userToLogin!!.password) {
                saveRememberOrNot()
                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ForumActivity::class.java)
                intent.putExtra("userPhoneNumber", userToLogin!!.phoneNumber)
                startActivity(intent)
            } else {
                Toast.makeText(this, "手机号或密码输入错误，请检查！", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loadUser() {
        val pn = phoneNumLoginEdit.text.toString()
        if (pn != "") {
            val user = userViewModel.searchUserByPhoneNumber(pn)
            userToLogin = user
            if (userToLogin != null) {
                userPhotoDisplayImg.setImageBitmap(userToLogin!!.image)
            }
        }
    }

    private fun saveRememberOrNot() {
        val remember = rememberSwitch.isChecked
        val sp = getSharedPreferences("remember_account", MODE_PRIVATE)
        sp.edit {
            putBoolean("key", remember)
            if (remember) {
                putString("pn", phoneNumLoginEdit.text.toString())
                putString("pw", passwordLoginEdit.text.toString())
            }
        }
    }

    private fun readRemember() {
        val sp = getSharedPreferences("remember_account", MODE_PRIVATE)
        val remember = sp.getBoolean("key", false)
        rememberSwitch.isChecked = remember
        if (remember) {
            phoneNumLoginEdit.setText(sp.getString("pn", ""))
            passwordLoginEdit.setText(sp.getString("pw", ""))
        }

    }

}