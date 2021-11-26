package com.lnm011223.login


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {


    @SuppressLint("Range")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val account_exist = intent.getStringExtra("account")
        val password_exist = intent.getStringExtra("password")

        var counter = 3

        var flag1 = true
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )

        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        insetsController?.hide(WindowInsetsCompat.Type.statusBars())
        window.statusBarColor = Color.TRANSPARENT
        var flag2 = false
        val prefs = getPreferences(Context.MODE_PRIVATE)
        val isremember = prefs.getBoolean("remember_password",false)
        if (isremember) {
            flag2 = true
            val account_remember = prefs.getString("account","")
            val password_remember = prefs.getString("password","")
            signin_accountEdit.setText(account_remember)
            signin_passwordEdit.setText(password_remember)
            remember_check.isChecked = true
        }
        signin_Button.setOnClickListener {
            val account = signin_accountEdit.text.toString()
            val password = signin_passwordEdit.text.toString()
            val editor = prefs.edit()
            counter--
            if (account != "" && password != "" && account == account_exist && password == password_exist || flag2){
                flag1 = false
                if (remember_check.isChecked) {
                    editor.putBoolean("remember_password",true)
                    editor.putString("account",account)
                    editor.putString("password",password)
                }else{
                    editor.clear()
                }
                editor.apply()
                val intent = Intent(this,userActivity::class.java)
                intent.putExtra("account",account)
                startActivity(intent)
            }
            if (counter==0 && flag1) {
                AlertDialog.Builder(this).apply {
                    setTitle("错误：")
                    setMessage("账号或密码错误，请检查或者重新注册")
                    setCancelable(false)
                    setPositiveButton("OK") { _, _ ->
                        signin_passwordEdit.setText("")
                        signin_Button.setBackgroundColor(Color.parseColor("#1565C0"))
                        signin_Button.isClickable = false
                    }

                    show()
                }
            }
        }
        signin_up_Button.setOnClickListener {
            val intent = Intent(this,signup_Activity::class.java)
            startActivityForResult(intent,1)
        }
    }



}


