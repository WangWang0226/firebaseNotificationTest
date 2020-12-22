package com.example.firebasenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)

        sendNotification(msg)
    }

    private fun sendNotification(msg: RemoteMessage) {

        //app在前景時收到推播 點擊後會進入這裡(onMessageReceive)
        var intent: Intent

        // 我們讓頁面intent到WelcomeActivity 再依據extra中destination的值導去對應頁面
        intent = Intent(this, WelcomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        //清除"包含Activity的task"中位于该Activity实例之上的其他Activity实例。
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (msg.data.get("destination") == "MainActivity") intent.putExtra("destination", "MainActivity")
        else if (msg.data.get("destination") == "Main2Activity") intent.putExtra("destination", "Main2Activity")


        val code = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            this, code/* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        //自定義channel id
        val channelId = "my_notification_id"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //創建推播物件
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            //設定推播icon、icon顏色、標題及內文
            .setSmallIcon(R.mipmap.ketawan)
            .setColor(ContextCompat.getColor(baseContext, R.color.color64C1BE))
            .setContentTitle(msg.notification?.title)
            .setContentText(msg.notification?.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)

            .setContentIntent(pendingIntent)

        //創建channel
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "test 通知", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            notificationBuilder.setChannelId(channelId)
        }
        notificationManager.notify(code /* ID of notification */, notificationBuilder.build())
    }
}