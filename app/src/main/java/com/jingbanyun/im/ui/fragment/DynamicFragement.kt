package com.jingbanyun.im.ui.fragment

import com.hyphenate.chat.EMClient
import com.jingbanyun.im.R
import com.jingbanyun.im.adapter.EMCallbackAdapter
import com.jingbanyun.im.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.fragment_dynamic.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class DynamicFragement : BaseFragment() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_dynamic
    }

    override fun init() {
        headerTitle.text = getString(R.string.dynamic)
        val logoutString = String.format(getString(R.string.logout), EMClient.getInstance().currentUser)
        logout.text = logoutString
        logout.setOnClickListener { logout() }
    }

    fun logout() {
        EMClient.getInstance().logout(true, object : EMCallbackAdapter() {

            override fun onSuccess() {
                context?.runOnUiThread {
                    toast(R.string.logout_success)
                    //跳转到登录界面
                    context?.startActivity<LoginActivity>()
                    activity?.finish()
                }
            }

            override fun onError(p0: Int, p1: String?) {
                //切换到主线程
                context?.runOnUiThread { toast(R.string.logout_failed) }
            }
        })
    }
}