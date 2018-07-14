package com.jingbanyun.im.contract

interface ContactContract{
    interface Presenter:BasePresenter{
        fun loadContacts()
    }

    interface View{
        fun onLoadContactSuccess()
        fun onLoadContactFailed()
    }
}