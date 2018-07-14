package com.jingbanyun.im.contract

interface LoginContract {

    interface Presener:BasePresenter{
        fun login(userName:String,password:String)
    }

    interface View{
      fun  onUserNameError()
      fun  onPasswordError()
      fun  onStartLogin()
      fun  onLoggedInSuccess()
      fun  onLoggedInFailed()
    }
}