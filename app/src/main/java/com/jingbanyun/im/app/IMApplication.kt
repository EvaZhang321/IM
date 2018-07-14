package com.jingbanyun.im.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.SoundPool
import cn.bmob.v3.Bmob
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMOptions
import com.hyphenate.chat.EMTextMessageBody
import com.jingbanyun.im.BuildConfig
import com.jingbanyun.im.R
import com.jingbanyun.im.adapter.EMMessageListenerAdapter
import com.jingbanyun.im.ui.activity.ChatActivity

class IMApplication : Application() {

    companion object {
        lateinit var instance: IMApplication
        //lateinit延迟初始化，本类在onCreate中实例化
    }

    val soundPool = SoundPool(2,AudioManager.STREAM_MUSIC,0)

    val short by lazy{
        soundPool.load(instance, R.raw.duan,0)
    }

    val long by lazy{
        soundPool.load(instance,R.raw.yulu,0)
    }

    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            //如果是在前台，则播放短的声音
            //如果是在后台，则播放长的声音
            if (isForeground()){
                soundPool.play(short,1f,1f,0,0,1f)
            }else{
                soundPool.play(long,1f,1f,0,0,1f)
                showNotification(p0)
            }
        }
    }

    private fun showNotification(p0: MutableList<EMMessage>?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        p0?.forEach {
            var contextText = getString(R.string.no_text_message)
            if (it.type ==EMMessage.Type.TXT){
                contextText = (it.body as EMTextMessageBody).message
            }
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("username",it.conversationId())
//            val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

            val taskStackBuilder = TaskStackBuilder.create(this).addParentStack(ChatActivity::class.java).addNextIntent(intent)
            val pendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

           val notification = Notification.Builder(this)
                   .setContentTitle(getString(R.string.receive_new_message))
                   .setContentText(contextText)
                   .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.avatar1))
                   .setSmallIcon(R.mipmap.ic_contact)
                   .setContentIntent(pendingIntent)
                   .setAutoCancel(true)
                   .notification
            notificationManager.notify(1,notification)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        //        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        //        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        //        options.setAutoDownloadThumbnail(true);

        //初始化
        EMClient.getInstance().init(applicationContext, EMOptions())
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG)

        Bmob.initialize(applicationContext, "e1de53bf4ac1b910100474c2553d26bf")

        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    private fun isForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (runningAppProcess in activityManager.runningAppProcesses) {
            if (runningAppProcess.processName == packageName){
                //找到了app的进程
              return  runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}