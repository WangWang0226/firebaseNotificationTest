package com.example.firebasenotification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcom)

        getFirebaseToken()
        checkIntentDestination()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        //當intent產生一個新的MainActivity時，即使已經存在一個相同且正在執行的activity，系統還是會建立一個新的實例。
        //為了避免此狀況，需要在manifest中的mainActivity加入singleTask，讓系統使用相同的實例。但這時系統intent過去後不會進入onCreate，而是進入onNewIntent
        checkIntentDestination()
    }


    private fun checkIntentDestination() {
        val destination = intent.extras?.getString("destination")

        // app在背景時收到推播 點擊後預設會進入此Activity(也就是launcher Activity) 並把data放進extra
        //拿到extra後 我們依據extra的值導去對應頁面
        if (destination != null) {
            if (destination == "MainActivity") startActivity(Intent(this,MainActivity::class.java))
            else if (destination == "Main2Activity") startActivity(Intent(this,Main2Activity::class.java))
        }
        //用一般方式開啟app，
        else {
            //do something
        }
    }

    private fun getFirebaseToken() {
        //向firebase取得當前設備的推播token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
//                Log.e(
//                    "firebaseToken",
//                    "****************Fetching FCM registration token failed",
//                    task.exception
//                )
                return@OnCompleteListener
            }
            // Get new FCM registration token
            println("****************getInstanceToken: ${task.result!!}")
        })
    }
}
