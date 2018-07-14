package com.jingbanyun.im.contract

import com.hyphenate.chat.EMClient
import com.jingbanyun.im.adapter.EMCallbackAdapter
import com.jingbanyun.im.extentions.isValidPassword
import com.jingbanyun.im.extentions.isValidUserName

class LoginPresenter(val view: LoginContract.View) : LoginContract.Presener {
    override fun login(userName: String, password: String) {
        if (userName.isValidUserName()) {
            //用户名合法，继续
            if (password.isValidPassword()) {
                //密码合法，开始登陆
                view.onStartLogin()
                //登陆环信
                loginEaseMob(userName, password)
            } else view.onPasswordError()
        } else view.onUserNameError()

    }

    private fun loginEaseMob(userName: String, password: String) {
        EMClient.getInstance().login(userName, password, object : EMCallbackAdapter() {
            //在子线程
            override fun onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()
                //在主线程通知view
               uiThread { view.onLoggedInSuccess() }
            }

            override fun onError(p0: Int, p1: String?) {
                uiThread { view.onLoggedInFailed() }
            }
        })
    }
}