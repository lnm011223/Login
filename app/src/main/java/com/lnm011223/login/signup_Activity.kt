package com.lnm011223.login

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*

class signup_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )

        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.statusBars())

        window.statusBarColor = Color.TRANSPARENT



        signup_Button.setOnClickListener {
            val username = signup_accountEdit.text.toString()
            val userpassword = signup_passwordEdit.text.toString()
            if (username != "" && userpassword != "") {
                AlertDialog.Builder(this).apply {
                    setTitle("成功：")
                    setMessage("已成功创建账户，请回到登录界面")

                    setPositiveButton("OK") { _, _ ->

                        val i = Intent(context, MainActivity::class.java)
                        i.putExtra("account",username)
                        i.putExtra("password",userpassword)
                        context.startActivity(i)

                        finish()

                    }

                    show()
                }
            }
        }
    }

}