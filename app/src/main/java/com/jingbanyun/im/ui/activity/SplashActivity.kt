package com.jingbanyun.im.ui.activity

import android.os.Handler
import com.jingbanyun.im.R
import com.jingbanyun.im.contract.SplashContract
import com.jingbanyun.im.presenter.SplashPresenter
import org.jetbrains.anko.startActivity

class SplashActivity: BaseActivity(),SplashContract.View {

    val presenter = SplashPresenter(this)

    companion object {
     const val DELAY = 2000L
    }

    private val handler by lazy {
        Handler()
    }

    override fun init() {
        super.init()
        presenter.checkLoginStatus()
    }

    //延时2秒，跳转到登录界面
    override fun onNotLoggedIn() {
        handler.postDelayed({
            startActivity<LoginActivity>()
            finish()
        }, DELAY)
    }

    //跳转到主界面
    override fun onLoggedIn() {
        startActivity<MainActivity>()
        finish()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }
}