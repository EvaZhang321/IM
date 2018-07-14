package com.jingbanyun.im.contract

import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.SaveListener
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.jingbanyun.im.extentions.isValidPassword
import com.jingbanyun.im.extentions.isValidUserName
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class RegisterPresenter(val view:RegisterContract.View) :RegisterContract.Presenter{
    override fun register(userName: String, password: String, confirmPassword: String) {
        if (userName.isValidUserName()){
            //检查密码
            if (password.isValidPassword()){
                //检查确认密码
                if (password.equals(confirmPassword)) {
                    //密码和确认密码一致
                    view.onStartRegister()
                    //开始注册
                    registerBmob(userName,password)
                }else view.onConfirmPasswordError()
            }else view.onPasswordError()
        }else view.onUserNameError()
    }

    private fun registerBmob(userName: String, password: String) {
        val bu = BmobUser()
        bu.username = userName
        bu.setPassword(password)
        //注意：不能用save方法进行注册
        bu.signUp<BmobUser>(object : SaveListener<BmobUser>() {
            override fun done(s: BmobUser?, e: BmobException?) {
                if (e == null) {
                    //注册成功
                    //注册到环信
                    registerEaseMob(userName,password)
                } else {
                    //注册失败
                    if (e.errorCode == 202) view.onUserExist()
                    else view.onRegisterFailed()
                }
            }
        })
    }

    private fun registerEaseMob(userName: String, password: String) {
        doAsync {
            try {
                //注册失败会抛出HyphenateException
                EMClient.getInstance().createAccount(userName, password)//同步方法
                //注册成功
                uiThread { view.onRegisterSuccess() }
            }catch (e:HyphenateException){
                //注册失败
                uiThread { view.onRegisterFailed() }
            }
        }
    }
}