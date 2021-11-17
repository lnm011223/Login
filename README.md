# 			登录注册界面的设计与实现说明文档 

### 







## 一、软件名称

​	简易登录注册界面

> Github项目地址：https://github.com/lnm011223/Login

## 二、软件内容简介

一个有较为美观（个人认为）的登录，注册，重置密码，用户界面的软件，用了Sqlite增删查改用户信息，比较完整的模拟了登录，注册，忘记密码，下线，注销等功能，适配了深色模式（2021年怎么会还有app不支持深色模式啊）。

实现了一些人性化的小细节，比如点击忘记密码会自动填充在登录界面输入的账号在重置密码界面，重置密码时如果账号和邮箱不匹配，会自动清除邮箱的已输入内容，显示密码，还有各种错误提醒，等等。

## 三、界面设计

#### 1.登录界面

> ![Sign in](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/Sign%20in.png)



#### 2.重置密码界面

> ![Reset](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/Reset.png)
>



#### 3.注册界面

> ![Sign up](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/Sign%20up.png)
>



#### 4.用户信息界面

> ![User](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/User.png)
>



#### 5.深色模式及英文适配展示

> ![dark and english](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/dark%20and%20english.png)
>







## 四、关键代码

### 1.Activity逻辑

> 1.MainActivity.kt

```kotlin
package com.lnm011223.login


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //非常重要，没有这句话监听无法生效
      
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
        forgot.setOnClickListener {
            val account = signin_accountEdit.text.toString()
            val intent = Intent(this,forgotActivity::class.java)
            intent.putExtra("forgot_account",account)
            startActivity(intent)
            finish()
        }
        show_check.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                signin_passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }else{
                signin_passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
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
                        finish()

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
```

> 2.signupActivity.kt

```kotlin
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
```

> 3.userActivity.kt

```kotlin
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
```

> 4.forgotActivity.kt

```kotlin
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
                        flag1 = false
                        if (password==password1 && password != ""){

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
```

### 2.布局文件

> 1.activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
    <TextView
        android:id="@+id/signin_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="300dp"
        android:text="@string/welcome_back"
        android:textColor="#2196F3"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="@+id/signin_image"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/signin_image"
        android:layout_width="290dp"
        android:layout_height="428dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.495"
        app:layout_constraintStart_toStartOf="parent"
        app:srcCompat="@drawable/ic_undraw_secure_login_pdn4"
        tools:ignore="MissingConstraints"
        tools:layout_editor_absoluteY="-105dp" />


    <LinearLayout
        android:id="@+id/linearLayout"
        android:layout_width="345dp"
        android:layout_height="190dp"
        android:layout_marginHorizontal="5dp"
        android:orientation="vertical"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/signin_text">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/account"
                android:textColor="#2196F3"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signin_accountEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:backgroundTint="#2196F3"
                android:layout_marginTop="5dp"
                android:background="@drawable/bg_edittext_normal"
                android:paddingStart="15dp"
                />

        </LinearLayout>

        <LinearLayout
            android:layout_marginTop="10dp"
            android:layout_width="match_parent"
            android:layout_height="115dp"
            android:orientation="vertical">
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                >
                <TextView
                    android:layout_width="0dp"
                    android:layout_weight="1"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_vertical"
                    android:text="@string/password"
                    android:textColor="#2196F3"
                    android:textSize="14sp" />

                <TextView
                    android:id="@+id/switch_button"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="@string/show"
                    android:gravity="end"
                    android:textColor="#2196F3"
                    android:textSize="14sp" />

                <androidx.appcompat.widget.AppCompatCheckBox
                    android:id="@+id/show_check"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:buttonTint="#2196F3" />
            </LinearLayout>

            <EditText
                android:id="@+id/signin_passwordEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:background="@drawable/bg_edittext_normal"
                android:padding="5dp"
                android:layout_marginTop="5dp"
                android:backgroundTint="#2196F3"
                android:inputType="textPassword"
                android:paddingStart="15dp"/>
            <TextView
                android:id="@+id/forgot"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/forgot_password"
                android:textColor="#2196F3"
                android:layout_marginTop="5dp"
                android:layout_gravity="end"
                />

        </LinearLayout>


    </LinearLayout>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/signin_Button"
        android:layout_width="120dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="28dp"

        android:backgroundTint="#2196F3"
        android:text="@string/sign_in"
        android:textAllCaps="false"
        app:backgroundTint="#2196F3"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/linearLayout"
        app:cornerRadius="100dp"
        android:paddingVertical="10dp"
        style="@style/Widget.MaterialComponents.Button.UnelevatedButton"
        />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="52dp"
        android:orientation="horizontal"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/signin_Button">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/new_user"
            android:textColor="#2196F3" />

        <TextView
            android:id="@+id/create_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="5dp"
            android:text="@string/create_an_account"
            android:textColor="#2196F3"
            android:textStyle="bold" />
    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```



> 2.activity_forgot.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".forgotActivity">
    <ImageView
        android:id="@+id/forgot_image"
        android:layout_width="290dp"
        android:layout_height="428dp"
        app:srcCompat="@drawable/ic_undraw_forgot_password_re_hxwm"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        />
    <TextView
        android:id="@+id/forgot_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="300dp"
        android:text="@string/update_password"
        android:textColor="#536DFE"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="@+id/forgot_image"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
    <LinearLayout
        android:id="@+id/linearLayout"
        android:layout_width="345dp"
        android:layout_height="275dp"
        android:layout_marginHorizontal="5dp"
        android:orientation="vertical"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/forgot_text">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/email"
                android:textColor="#536DFE"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/forgot_emailEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:backgroundTint="#536DFE"
                android:layout_marginTop="5dp"
                android:background="@drawable/bg_edittext_normal"
                android:paddingStart="15dp"/>

        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical"
            android:layout_marginTop="10dp">

            <TextView
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/account"
                android:textColor="#536DFE"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/forgot_accountEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:backgroundTint="#536DFE"
                android:layout_marginTop="5dp"
                android:background="@drawable/bg_edittext_normal"
                android:paddingStart="15dp"/>

        </LinearLayout>

        <LinearLayout
            android:layout_marginTop="10dp"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/new_password"
                android:textColor="#536DFE"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/forgot_passwordEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:background="@drawable/bg_edittext_normal"
                android:padding="5dp"
                android:layout_marginTop="5dp"
                android:backgroundTint="#536DFE"
                android:inputType="textPassword"
                android:paddingStart="15dp"/>
        </LinearLayout>

        <LinearLayout
            android:layout_marginTop="10dp"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/verify_new_password"
                android:textColor="#536DFE"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/forgot_passwordEdit1"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:background="@drawable/bg_edittext_normal"
                android:padding="5dp"
                android:layout_marginTop="5dp"
                android:backgroundTint="#536DFE"
                android:inputType="textPassword"
                android:paddingStart="15dp"/>
        </LinearLayout>

    </LinearLayout>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/forgot_Button"
        android:layout_width="120dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="28dp"
        app:cornerRadius="100dp"
        android:backgroundTint="#536DFE"
        android:text="@string/update"
        android:textAllCaps="false"
        app:backgroundTint="#8bc34a"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/linearLayout"
        android:paddingVertical="10dp"
        style="@style/Widget.MaterialComponents.Button.UnelevatedButton"
        />


</androidx.constraintlayout.widget.ConstraintLayout>
```



> 3.activity_user.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".userActivity">

    <ImageView

        android:id="@+id/user_image"
        android:layout_width="290dp"
        android:layout_height="428dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.495"
        app:layout_constraintStart_toStartOf="parent"
        app:srcCompat="@drawable/ic_undraw_access_account_re_8spm"
        tools:ignore="MissingConstraints"
        tools:layout_editor_absoluteY="-58dp" />

    <TextView
        android:id="@+id/user_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/user_information"
        android:textColor="#F9A826"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="@+id/user_image"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/user_image" />


    <LinearLayout
        android:id="@+id/linearLayout2"
        android:layout_width="289dp"
        android:layout_height="120dp"
        android:layout_marginTop="10dp"
        android:background="@drawable/bg_usertext_normal"
        android:backgroundTint="#F9A826"
        android:orientation="vertical"
        android:padding="10dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/user_text">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/account"
                android:textColor="#F9A826"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/user_account"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp" />
        </LinearLayout>

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/email"
                android:textColor="#F9A826"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/user_email"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp" />
        </LinearLayout>
    </LinearLayout>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/signout_Button"
        style="@style/Widget.MaterialComponents.Button.UnelevatedButton"
        android:layout_width="120dp"
        android:layout_height="wrap_content"
        android:backgroundTint="#F9A826"
        android:paddingVertical="10dp"
        android:text="@string/sign_out"
        android:textAllCaps="false"
        app:backgroundTint="#F9A826"
        app:cornerRadius="100dp"
        app:layout_constraintEnd_toStartOf="@+id/delete_Button"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/linearLayout2"
        android:layout_marginTop="10dp"/>

    <com.google.android.material.button.MaterialButton
        app:strokeWidth="2dp"
        app:strokeColor="#F9A826"
        android:layout_marginTop="10dp"
        android:textColor="#F9A826"
        android:id="@+id/delete_Button"
        style="@style/Widget.MaterialComponents.Button.UnelevatedButton"
        android:layout_width="120dp"
        android:layout_height="wrap_content"
        android:backgroundTint="#00000000"
        android:paddingVertical="10dp"
        android:text="@string/delete_account"
        android:textAllCaps="false"
        app:backgroundTint="#F9A826"
        app:cornerRadius="100dp"
        app:layout_constraintStart_toEndOf="@id/signout_Button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/linearLayout2" />

</androidx.constraintlayout.widget.ConstraintLayout>
```



> 4.activity_signup.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".signup_Activity">
    <ImageView
        android:id="@+id/signup_image"
        android:layout_width="290dp"
        android:layout_height="428dp"
        app:srcCompat="@drawable/ic_undraw_my_password_d_6_kg__1_"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        />
    <TextView
        android:id="@+id/signup_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="300dp"
        android:text="@string/create_account"
        android:textColor="#8bc34a"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="@+id/signup_image"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
    <LinearLayout
        android:id="@+id/linearLayout"
        android:layout_width="345dp"
        android:layout_height="200dp"
        android:layout_marginHorizontal="5dp"
        android:orientation="vertical"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/signup_text">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/email"
                android:textColor="#8bc34a"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signup_emailEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:backgroundTint="#8bc34a"
                android:layout_marginTop="5dp"
                android:background="@drawable/bg_edittext_normal"
                android:paddingStart="15dp"/>

        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical"
            android:layout_marginTop="10dp">

            <TextView
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/account"
                android:textColor="#8bc34a"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signup_accountEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:backgroundTint="#8bc34a"
                android:layout_marginTop="5dp"
                android:background="@drawable/bg_edittext_normal"
                android:paddingStart="15dp"/>

        </LinearLayout>

        <LinearLayout
            android:layout_marginTop="10dp"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/password"
                android:textColor="#8bc34a"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signup_passwordEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:background="@drawable/bg_edittext_normal"
                android:padding="5dp"
                android:layout_marginTop="5dp"
                android:backgroundTint="#8bc34a"
                android:inputType="textPassword"
                android:paddingStart="15dp"/>
        </LinearLayout>


    </LinearLayout>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/signup_Button"
        android:layout_width="120dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="28dp"
        app:cornerRadius="100dp"
        android:backgroundTint="#8bc34a"
        android:text="@string/sign_up"
        android:textAllCaps="false"
        app:backgroundTint="#8bc34a"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/linearLayout"
        android:paddingVertical="10dp"
        style="@style/Widget.MaterialComponents.Button.UnelevatedButton"
        />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="52dp"
        android:orientation="horizontal"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/signup_Button">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/already_have_an_account"
            android:textColor="#8bc34a" />

        <TextView
            android:id="@+id/already_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="5dp"
            android:text="@string/sign_in"
            android:textColor="#8bc34a"
            android:textStyle="bold" />
    </LinearLayout>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 3.Sqlite逻辑

> MyDatabaseHelper.kt



```kotlin
package com.lnm011223.login

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context: Context,name: String,version: Int): SQLiteOpenHelper(context,name,null,version) {

    private val account_creat = "create table accountdata (" +
            "id integer primary key autoincrement," +
            "account text," +
            "email text," +
            "password text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(account_creat)
        Toast.makeText(context,"Create succeeded",Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}
```



## 五、软件操作流程

![image-20211116193524392](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116193524392.png)

## 六、难点和解决方案

#### 1.状态栏图标颜色在白色背景以及深色模式下的适配

解决方案：

可以用API31才有的`insetsController`功能，再写个`isDarkTheme`函数判断是否切换到深色模式

```kotlin
val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )
        if (!isDarkTheme(this)){

            insetsController?.isAppearanceLightStatusBars = true

        }
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        window.statusBarColor = Color.TRANSPARENT


private fun isDarkTheme(context: Context): Boolean {
        val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return flag == Configuration.UI_MODE_NIGHT_YES
    }
```

#### 2.用户信息的处理

解决方案：

可以利用Sqlite处理用户信息，增删查改分别对应注册，注销，登录，重置密码

#### 3.用户下线等操作

解决方案：

可以使所有activity重新继承一个`baseactivity`父类，重写相关函数

```kotlin
package com.lnm011223.login

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
       
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
    
}
```



## 七、不足之处

- 1.用户信息以明文方式存在本地的数据里，不安全，可以加混淆的功能
- 2.一个真正的登录功能应该是从服务器匹配数据，可惜我暂时没有买服务器
- 3.还是有很多小细节没有顾及到，比如一些文字的配色懒得去处理了

## 八、今后设想

这次锻炼了 一下自定义布局文件，sqlite操作的能力，本来想用Room进行增删查改的，不过有点麻烦

可以继续完善小细节，并接入服务器存储用户信息

