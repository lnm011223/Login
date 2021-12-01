# 登录注册界面的设计与实现说明文档

### 

------

## 一、软件名称

 简易登录注册界面



## 二、软件内容简介

一个简约的登录，注册，用户界面的软件，

实现了基础的记住密码，注册账号，登录账号的功能

## 三、界面设计



<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211128163856426.png" alt="image-20211128163856426" style="zoom:33%;" />



<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116211125505.png" alt="image-20211116211125505" style="zoom:33%;" />

<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211128163958478.png" alt="image-20211128163958478" style="zoom:33%;" />

<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116211224620.png" alt="image-20211116211224620" style="zoom:33%;" />

<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116211249672.png" alt="image-20211116211249672" style="zoom:33%;" />

<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116211314882.png" alt="image-20211116211314882" style="zoom:33%;" />

<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116211709931.png" alt="image-20211116211709931" style="zoom:33%;" />

<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116211732097.png" alt="image-20211116211732097" style="zoom:33%;" />

<img src="https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211116211758226.png" alt="image-20211116211758226" style="zoom:33%;" />

## 四、关键代码

### 1.逻辑文件

MainActivity.kt

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



```

signup_activity.kt

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
```

User_activity.kt

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

class userActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val insetsController = WindowCompat.getInsetsController(
            window, window.decorView
        )

        insetsController?.hide(WindowInsetsCompat.Type.navigationBars())
        insetsController?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.statusBars())
        window.statusBarColor = Color.TRANSPARENT
        val account = intent.getStringExtra("account")
        user_account.setText(account)


    }

}
```

### 2.布局文件	

activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#2196F3"
    tools:context=".MainActivity">


    <TextView
        android:id="@+id/signin_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="216dp"
        android:text="@string/welcome_back"
        android:textColor="#ffffff"
        android:textSize="24sp"

        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <LinearLayout
        android:id="@+id/linearLayout"
        android:layout_width="345dp"
        android:layout_height="165dp"
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
                android:textColor="#ffffff"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signin_accountEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:layout_marginTop="5dp"
                android:background="@drawable/button_drawable"
                android:backgroundTint="#90CAF9"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:paddingStart="15dp" />

        </LinearLayout>

        <LinearLayout
            android:layout_marginTop="10dp"
            android:layout_width="match_parent"
            android:layout_height="95dp"
            android:orientation="vertical">

            <TextView
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:text="@string/password"
                android:textColor="#ffffff"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signin_passwordEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:background="@drawable/button_drawable"
                android:padding="5dp"
                android:layout_marginTop="5dp"
                android:backgroundTint="#90CAF9"
                android:inputType="textPassword"
                android:paddingStart="15dp"/>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal">

                <TextView

                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"

                    android:text="@string/remember"

                    android:textColor="#ffffff"
                    android:textSize="14sp" />
                <androidx.appcompat.widget.AppCompatCheckBox
                    android:id="@+id/remember_check"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:buttonTint="#ffffff" />


            </LinearLayout>


        </LinearLayout>


    </LinearLayout>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/signin_Button"
        style="@style/Widget.MaterialComponents.Button.UnelevatedButton"
        android:layout_width="120dp"
        android:layout_height="wrap_content"

        android:layout_marginTop="40dp"
        android:backgroundTint="#ffffff"
        android:paddingVertical="10dp"
        android:text="@string/sign_in"
        android:textAllCaps="false"
        android:textColor="#2196F3"
        app:backgroundTint="#2196F3"
        app:cornerRadius="100dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.113"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/linearLayout" />

    <com.google.android.material.button.MaterialButton
        android:id="@+id/signin_up_Button"
        style="@style/Widget.MaterialComponents.Button.UnelevatedButton"
        android:layout_width="120dp"
        android:layout_height="wrap_content"

        android:layout_marginTop="40dp"
        android:backgroundTint="#ffffff"
        android:paddingVertical="10dp"
        android:text="@string/sign_up"
        android:textAllCaps="false"
        android:textColor="#2196F3"
        app:backgroundTint="#2196F3"
        app:cornerRadius="100dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.652"
        app:layout_constraintStart_toEndOf="@+id/signin_Button"
        app:layout_constraintTop_toBottomOf="@+id/linearLayout" />


</androidx.constraintlayout.widget.ConstraintLayout>
```

Activity_user.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#607D8B"
    tools:context=".userActivity">


    <TextView
        android:id="@+id/user_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="244dp"
        android:text="@string/user_information"
        android:textColor="#ffffff"
        android:textSize="24sp"

        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />


    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="100dp"
        app:layout_constraintTop_toBottomOf="@+id/user_text"
        android:layout_marginTop="10dp"
        android:layout_marginHorizontal="10dp"
        app:cardCornerRadius="10dp">

        <LinearLayout
            android:padding="10dp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@string/account"

                android:textStyle="bold" />

            <TextView
                android:id="@+id/user_account"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="5dp" />
        </LinearLayout>

    </androidx.cardview.widget.CardView>






</androidx.constraintlayout.widget.ConstraintLayout>
```

Activity_signup.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#9CCC65"
    tools:context=".signup_Activity">

    <TextView
        android:id="@+id/signup_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="232dp"
        android:text="@string/create_account"
        android:textColor="#ffffff"
        android:textSize="24sp"

        android:textStyle="bold"
        app:layout_constraintEnd_toEndOf="parent"
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
                android:textColor="#ffffff"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signup_emailEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:backgroundTint="#C5E1A5"
                android:layout_marginTop="5dp"
                android:background="@drawable/button_drawable"
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
                android:textColor="#ffffff"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signup_accountEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:inputType="textPersonName"
                android:padding="5dp"
                android:backgroundTint="#C5E1A5"
                android:layout_marginTop="5dp"
                android:background="@drawable/button_drawable"
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
                android:textColor="#ffffff"
                android:textSize="14sp" />

            <EditText
                android:id="@+id/signup_passwordEdit"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="center_vertical"
                android:background="@drawable/button_drawable"
                android:padding="5dp"
                android:layout_marginTop="5dp"
                android:backgroundTint="#C5E1A5"
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
        android:backgroundTint="#ffffff"
        android:text="@string/sign_up"
        android:textAllCaps="false"
        android:textColor="#9CCC65"
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



## 五、软件操作流程

![image-20211128170200559](https://gitee.com/lnm011223/lnm011223-picture/raw/master/uPic/image-20211128170200559.png)

## 六、难点和解决方案

1.记住密码

解决方案：可以用SharedPreferences存储

```kotlin
val prefs = getPreferences(Context.MODE_PRIVATE)
val isremember = prefs.getBoolean("remember_password",false)
```

2.传递账号信息

解决方案：用

```kotlin
i.putExtra("account",username)
i.putExtra("password",userpassword)
```

然后和edittext里获取的字符串匹配一下就好了

3.错误三次以后无法登录

解决方案：弹出一个只能点ok的alertdialog就好了，然后把button改为不可点击，为了让效果更明显，把按钮的颜色也改成深色

```
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
```

## 七、不足之处

## 八、今后设想