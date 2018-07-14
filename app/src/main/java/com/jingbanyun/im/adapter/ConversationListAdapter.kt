package com.jingbanyun.im.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hyphenate.chat.EMConversation
import com.jingbanyun.im.ui.activity.ChatActivity
import com.jingbanyun.im.widget.ConversationListItemView
import org.jetbrains.anko.startActivity

class ConversationListAdapter(val context: Context,val conversations: MutableList<EMConversation>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ConversationListItemViewHolder(ConversationListItemView(context))
    }

    override fun getItemCount(): Int = conversations.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val conversationListItemView = holder.itemView as ConversationListItemView
        conversationListItemView.bindView(conversations[position])
        conversationListItemView.setOnClickListener { context.startActivity<ChatActivity>(
                "username" to conversations[position].conversationId()
        ) }
    }

    class ConversationListItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}