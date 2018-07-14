package com.jingbanyun.im.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hyphenate.chat.EMMessage
import com.hyphenate.util.DateUtils
import com.jingbanyun.im.widget.ReceiveMessageItemView
import com.jingbanyun.im.widget.SendMessageItemView

class MessageListAdapter(val context: Context, val messages: List<EMMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val ITEM_TYPE_SEND_MESSAGE = 0
        val ITEM_TYPE_RECEIVE_MESSAGE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].direct() == EMMessage.Direct.SEND) {
            ITEM_TYPE_SEND_MESSAGE
        } else {
            ITEM_TYPE_RECEIVE_MESSAGE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_SEND_MESSAGE) {
            SendMessageViewHolder(SendMessageItemView(context))
        } else {
            ReceiveMessageViewHolder(ReceiveMessageItemView(context))
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val showTimestamp:Boolean = isShowTimeStamp(position)
        if (getItemViewType(position) == ITEM_TYPE_SEND_MESSAGE) {
            val sendMessageItemView = holder.itemView as SendMessageItemView
            sendMessageItemView.bindView(messages[position],showTimestamp)
        } else {
            val receiveMessageItemView = holder.itemView as ReceiveMessageItemView
            receiveMessageItemView.bindView(messages[position],showTimestamp)
        }
    }

    private fun isShowTimeStamp(position: Int): Boolean {
        //如果是第一条消息或者和前一条间隔时间比较长
        var showTimestamp = true
        if (position>0){
            showTimestamp = DateUtils.isCloseEnough(messages[position].msgTime,messages[position-1].msgTime).not()
        }
        return showTimestamp
    }

    class SendMessageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    class ReceiveMessageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}