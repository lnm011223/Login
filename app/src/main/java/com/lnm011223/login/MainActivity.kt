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
    @RequiresApi(Build.VERSION_CODES.R)
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

        signin_Button.setOnClickListener {
            val account = signin_accountEdit.text.toString()
            val password = signin_passwordEdit.text.toString()

            counter--
            if (account != "" && password != "" && account == account_exist && password == password_exist){
                flag1 = false
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


