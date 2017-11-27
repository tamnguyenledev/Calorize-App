package com.example.tamnguyen.calorizeapp

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

class LoginActivity : AppCompatActivity() {

    val APP_NAME_FONT = "DancingScript-Regular.ttf"
    val LOGIN_BUTTON_FONT = "HelveticaNeue-Light.otf"
    var appNameText: TextView? = null
    var loginBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        appNameText = findViewById(R.id.image_app_name)
        loginBtn = findViewById(R.id.login_button)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //Setup UI for App Name
        appNameText?.typeface = Typeface.createFromAsset(assets,APP_NAME_FONT)
        var appNameParams = appNameText?.layoutParams
        (appNameParams as RelativeLayout.LayoutParams?)?.topMargin = MyApplication.getStatusBarHeight(this) + resources.getDimension(R.dimen.app_name_text_margin_top).toInt()

        //Setup UI for login button
        loginBtn?.typeface = Typeface.createFromAsset(assets,LOGIN_BUTTON_FONT)
        var btnParams = loginBtn?.layoutParams
        (btnParams as RelativeLayout.LayoutParams?)?.bottomMargin = resources.getDimension(R.dimen.login_btn_margin_bottom).toInt() +
              if (MyApplication.hasSoftNavBar(this)) MyApplication.getNavigationBarHeight(this,resources.configuration.orientation)
              else 0

    }
}
