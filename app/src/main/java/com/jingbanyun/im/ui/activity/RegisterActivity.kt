package com.jingbanyun.im.ui.activity

import com.jingbanyun.im.R
import com.jingbanyun.im.R.id.*
import com.jingbanyun.im.contract.RegisterContract
import com.jingbanyun.im.contract.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity(), RegisterContract.View {
    val presenter by lazy { RegisterPresenter(this) }

    override fun init() {
        super.init()
        register.setOnClickListener{ register()}
        confirmPassword.setOnEditorActionListener { textView, i, keyEvent ->
            register()
            true
        }
    }

    fun register(){
        //隐藏软键盘
        hideSoftKeyborad()
        val userNameString = userName.text.trim().toString()
        val passwordString = password.text.trim().toString()
        val confirmPasswordString = confirmPassword.text.trim().toString()
        presenter.register(userNameString,passwordString,confirmPasswordString)
    }

    override fun onUserNameError() {
        userName.error = getString(R.string.user_name_error)
    }

    override fun onPasswordError() {
        password.error = getString(R.string.password_error)
    }

    override fun onConfirmPasswordError() {
        confirmPassword.error = getString(R.string.confirm_password_error)
    }

    override fun onStartRegister() {
        showProgress(getString(R.string.registering))
    }

    override fun onRegisterSuccess() {
        dismissDialogProgress()
        toast(R.string.register_success)
        finish()
    }

    override fun onRegisterFailed() {
        dismissDialogProgress()
        toast(R.string.register_failed)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_register
    }

    override fun onUserExist() {
        dismissDialogProgress()
        toast(R.string.user_already_exist)
    }
}