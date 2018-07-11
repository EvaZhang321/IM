package com.jingbanyun.im.contract

interface SplashContract {
    interface Presenter :BasePresenter{
        fun checkLoginStatus()//检查登录状态
    }
    interface View{
        fun onNotLoggedIn()//没有登录的UI处理
        fun onLoggedIn()//已经登录的UI处理
    }
}