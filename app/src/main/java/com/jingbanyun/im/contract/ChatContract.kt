package com.jingbanyun.im.contract

import com.hyphenate.chat.EMMessage

interface ChatContract {
    interface Presenter:BasePresenter{
        fun sendMessage(contact:String,message:String)
        fun addMessage(username: String, p0: MutableList<EMMessage>?)
        fun loadMessage(username: String)
        fun loadMoreMessage(username: String)
    }

    interface View{
        fun onStartSendMessage()
        fun onSendMessageSuccess()
        fun onSendMessafeFailed()
        fun onMessageLoaded()
        fun onMoreMessageLoaded(size: Int)
    }
}