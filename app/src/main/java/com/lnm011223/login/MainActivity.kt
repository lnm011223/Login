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



class MainActivity : BaseActivity() {


    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //非常重要，没有这句话监听无法生效
        window.setDecorFitsSystemWindows(false)
        setContentView(R.layout.activity_main)

        //icon color -> black
        //activity.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        //icon color -> white
        //activity.getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS);

        var counter = 3


        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        if (!isDarkTheme(this)){

            insetsController?.isAppearanceLightStatusBars = true

        }
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        window.statusBarColor = Color.TRANSPARENT
        val dbhelper = MyDatabaseHelper(this,"accountdata.db",1)
        create_text.setOnClickListener {
            val intent = Intent(this,signup_Activity::class.java)
            startActivity(intent)
        }
        signin_Button.setOnClickListener {
            counter--
            var flag1 = true
            val account = signin_accountEdit.text.toString()
            val password = signin_passwordEdit.text.toString()
            val db = dbhelper.writableDatabase
            val cursor = db.query("accountdata",null,null,null,null,null,null)
            if (cursor.moveToFirst()) {
                do {
                    val account_exist = cursor.getString(cursor.getColumnIndex("account"))
                    val password_exist = cursor.getString(cursor.getColumnIndex("password"))
                    val email_exist = cursor.getString(cursor.getColumnIndex("email"))
                    if (account_exist==account && password_exist==password && account!= "" && password != ""){
                        flag1 = false
                        val intent = Intent(this,userActivity::class.java)

                        intent.putExtra("user_name",account)
                        intent.putExtra("user_email",email_exist)
                        startActivity(intent)
                        finish()
                    }

                }while (cursor.moveToNext())
            }
            cursor.close()
            if (flag1) {
                Toast.makeText(this, "剩余可尝试次数为 $counter 次", Toast.LENGTH_SHORT).show()
            }
            if (counter==0) {
                AlertDialog.Builder(this).apply {
                    setTitle("错误：")
                    setMessage("账号或密码错误，请检查或者重新注册")
                    setCancelable(false)
                    setPositiveButton("OK") { _, _ ->
                        ActivityCollector.finishAll()
                        val i = Intent(context, MainActivity::class.java)
                        context.startActivity(i)

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