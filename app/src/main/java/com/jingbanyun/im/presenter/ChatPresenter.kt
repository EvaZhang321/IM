package com.jingbanyun.im.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.jingbanyun.im.adapter.EMCallbackAdapter
import com.jingbanyun.im.contract.ChatContract
import org.jetbrains.anko.doAsync

class ChatPresenter(val view: ChatContract.View) : ChatContract.Presenter {

    companion object {
        val PAGE_SIZE = 10 //默认每次加载10条消息数据
    }

    val messages = mutableListOf<EMMessage>()

    override fun sendMessage(contact: String, message: String) {
        //创建一条消息
        val enMessage = EMMessage.createTxtSendMessage(message, contact)
        enMessage.setMessageStatusCallback(object : EMCallbackAdapter() {
            override fun onSuccess() {
                uiThread { view.onSendMessageSuccess() }
            }

            override fun onError(p0: Int, p1: String?) {
                uiThread { view.onSendMessafeFailed() }
            }
        })
        messages.add(enMessage)
        view.onStartSendMessage()
        EMClient.getInstance().chatManager().sendMessage(enMessage)
    }

    override fun addMessage(username: String, p0: MutableList<EMMessage>?) {
        //加入到当前的消息列表
        p0?.let { messages.addAll(it) }
        //更新消息为已读消息
        //获取跟联系人的会话，然后标记会话里面的消息为全部已读
        val conversation = EMClient.getInstance().chatManager().getConversation(username)
        conversation.markAllMessagesAsRead()

    }

    override fun loadMessage(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            //将加载的消息标记为已读
            conversation.markAllMessagesAsRead()
            messages.addAll(conversation.allMessages)
            uiThread { view.onMessageLoaded() }
        }
    }

    override fun loadMoreMessage(username: String) {
        doAsync {
            val conversation = EMClient.getInstance().chatManager().getConversation(username)
            val startMsgId = messages[0].msgId
            val loadMoreMsgFromDB = conversation.loadMoreMsgFromDB(startMsgId, PAGE_SIZE)
            messages.addAll(0,loadMoreMsgFromDB)
            uiThread { view.onMoreMessageLoaded(loadMoreMsgFromDB.size) }
        }
    }
}