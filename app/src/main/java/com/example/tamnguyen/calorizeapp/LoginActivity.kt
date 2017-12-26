package com.example.tamnguyen.calorizeapp

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*
import com.google.firebase.auth.AuthResult
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.google.firebase.auth.FacebookAuthProvider
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    private val readPermissions = Arrays.asList(
            "email", "public_profile","user_birthday")
    val APP_NAME_FONT = "DancingScript-Regular.ttf"
    val LOGIN_BUTTON_FONT = "HelveticaNeue-Light.otf"
    var appNameText: TextView? = null
    var loginBtn: Button? = null
    private var mCallbackManager: CallbackManager? = null
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use this code to see token in debugging mode
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

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
        loginBtn?.setOnClickListener {
            v ->
            LoginManager.getInstance().logInWithReadPermissions(this,readPermissions);
        }
        //Login Setup
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                Log.d("D",error.message)
            }
        })
    }
    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                //User signs in successfully with Authentication
                //Get user data from Facebook
                val request = GraphRequest.newMeRequest(token, { json, response ->
                    var intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("name", json["name"] as String)
                        putExtra("birthday", json["birthday"] as String)
                        putExtra("picture", json.getJSONObject("picture").getJSONObject("data")["url"] as String)
                    }
                    startActivity(intent)
                    finish()
                })
                val params = Bundle()
                params.putString("fields", "id,name,birthday,email,picture")
                request.parameters = params
                request.executeAsync()
            } else {
                Toast.makeText(this, "Something wrong, Try again later!!!", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager?.onActivityResult(requestCode,resultCode,data)
    }

}
