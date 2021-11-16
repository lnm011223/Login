package com.lnm011223.login

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.android.synthetic.main.activity_user.*
import kotlin.math.log

class userActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        if (!isDarkTheme(this)){

            insetsController?.isAppearanceLightStatusBars = true

        }
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        window.statusBarColor = Color.TRANSPARENT
        val dbhelper = MyDatabaseHelper(this, "accountdata.db", 1)
        val user_nametext = intent.getStringExtra("user_name")
        val user_emailtext = intent.getStringExtra("user_email")
        user_account.text = user_nametext
        user_email.text = user_emailtext
        signout_Button.setOnClickListener {
            ActivityCollector.finishAll()
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
        delete_Button.setOnClickListener {
            val db = dbhelper.writableDatabase
            db.delete("accountdata","account = ?", arrayOf(user_nametext))
            AlertDialog.Builder(this).apply {
                setTitle("成功：")
                setMessage("已删除账号！")
                setCancelable(false)
                setPositiveButton("OK") { _, _ ->
                    ActivityCollector.finishAll()
                    val i = Intent(context, MainActivity::class.java)
                    context.startActivity(i)
                    finish()

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