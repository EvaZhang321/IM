package com.jingbanyun.im.ui.activity

import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.jingbanyun.im.R
import com.jingbanyun.im.R.id.bottomBar
import com.jingbanyun.im.adapter.EMMessageListenerAdapter
import com.jingbanyun.im.factory.FragmentFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_main

    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            runOnUiThread {
                updateBottomBarUnreadCount()
            }
        }
    }

    override fun init() {
        super.init()
        bottomBar.setBadgesHideWhenActive(false)
        bottomBar.setOnTabSelectListener { tabId ->
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_frame, FragmentFactory.instance.getFragment(tabId))
            transaction.commit()
        }

        EMClient.getInstance().chatManager().addMessageListener(messageListener)
        EMClient.getInstance().addConnectionListener(object:EMConnectionListener{
            override fun onConnected() {
            }

            override fun onDisconnected(p0: Int) {
                if (p0 == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    //发送多设备登录，跳转到登录界面
                    startActivity<LoginActivity>()
                    finish()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        updateBottomBarUnreadCount()
    }

    private fun updateBottomBarUnreadCount() {
        //初始化bottombar未读消息的个数
        val tab = bottomBar.getTabWithId(R.id.tab_conversation)
        tab.setBadgeCount(EMClient.getInstance().chatManager().unreadMessageCount)
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }
}
