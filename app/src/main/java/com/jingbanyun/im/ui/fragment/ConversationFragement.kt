package com.jingbanyun.im.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.jingbanyun.im.R
import com.jingbanyun.im.adapter.ConversationListAdapter
import com.jingbanyun.im.adapter.EMMessageListenerAdapter
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ConversationFragement: BaseFragment() {

    val conversations = mutableListOf<EMConversation>()

    val messageListener = object :EMMessageListenerAdapter(){
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            loadConversations()
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_conversation
    }

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.message)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager =LinearLayoutManager(context)
            adapter = ConversationListAdapter(context,conversations)
        }

        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    private fun loadConversations() {
        conversations.clear()
        doAsync {
            val allConversations = EMClient.getInstance().chatManager().allConversations
            conversations.addAll(allConversations.values)
            uiThread { recyclerView.adapter.notifyDataSetChanged() }
        }
    }

    override fun onResume() {
        super.onResume()
        loadConversations()
    }
    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }
}