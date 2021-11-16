package com.lnm011223.login

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_forgot.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*

class forgotActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        if (!isDarkTheme(this)){

            insetsController?.isAppearanceLightStatusBars = true

        }
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.statusBarColor = Color.TRANSPARENT
        val dbhelper = MyDatabaseHelper(this,"accountdata.db",1)
        val forgot_user= intent.getStringExtra("forgot_account")
        forgot_accountEdit.setText(forgot_user)
        forgot_Button.setOnClickListener {
            var flag1 = true
            val email = forgot_emailEdit.text.toString()
            val account = forgot_accountEdit.text.toString()
            val password = forgot_passwordEdit.text.toString()
            val password1 = forgot_passwordEdit1.text.toString()
            val db = dbhelper.writableDatabase
            val cursor = db.query("accountdata",null,null,null,null,null,null)
            if (cursor.moveToFirst()) {
                do {
                    val account_exist = cursor.getString(cursor.getColumnIndex("account"))
                    val password_exist = cursor.getString(cursor.getColumnIndex("password"))
                    val email_exist = cursor.getString(cursor.getColumnIndex("email"))
                    if (account_exist==account && email_exist==email && account!= "" && email != ""){
                        if (password==password1 && password != ""){
                            flag1 = false
                            val values = ContentValues()
                            values.put("password",password)
                            db.update("accountdata",values,"account = ?", arrayOf(account))
                            AlertDialog.Builder(this).apply {
                                setTitle("成功：")
                                setMessage("已成功重置密码，请回到登录界面")

                                setPositiveButton("OK") { _, _ ->
                                    ActivityCollector.finishAll()
                                    val i = Intent(context, MainActivity::class.java)
                                    context.startActivity(i)

                                }

                                show()
                            }
                        }else{
                            AlertDialog.Builder(this).apply {
                                setTitle("错误：")
                                setMessage("两次密码不一样！")

                                setPositiveButton("OK") { _, _ ->
                                    forgot_passwordEdit1.setText("")

                                }

                                show()
                            }
                        }

                    }

                }while (cursor.moveToNext())
            }
            cursor.close()
            if (flag1) {
                AlertDialog.Builder(this).apply {
                    setTitle("错误：")
                    setMessage("邮箱与账号不匹配，请检查信息！")

                    setPositiveButton("OK") { _, _ ->
                        forgot_emailEdit.setText("")

                    }

                    show()
                }
            }
        }


    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}