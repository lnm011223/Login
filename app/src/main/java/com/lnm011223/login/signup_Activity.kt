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

class signup_Activity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        if (!isDarkTheme(this)) {

            insetsController?.isAppearanceLightStatusBars = true

        }
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        insetsController?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.statusBarColor = Color.TRANSPARENT

        val dbhelper = MyDatabaseHelper(this, "accountdata.db", 1)
        already_text.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        signup_Button.setOnClickListener {
            val db = dbhelper.writableDatabase
            val email = signup_emailEdit.text.toString()
            val account = signup_accountEdit.text.toString()
            val password = signup_passwordEdit.text.toString()
            val values = ContentValues().apply {
                //组装数据
                put("account", account)
                put("email", email)
                put("password", password)
            }
            db.insert("accountdata", null, values)
            AlertDialog.Builder(this).apply {
                setTitle("成功：")
                setMessage("已成功创建账户，请回到登录界面")

                setPositiveButton("OK") { _, _ ->
                    ActivityCollector.finishAll()
                    val i = Intent(context, MainActivity::class.java)
                    context.startActivity(i)

                }

                show()
            }
        }
    }
    private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
}