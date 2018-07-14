package com.jingbanyun.im.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.hyphenate.chat.EMClient
import com.jingbanyun.im.R
import com.jingbanyun.im.adapter.EMCallbackAdapter
import com.jingbanyun.im.data.AddFriendItem
import kotlinx.android.synthetic.main.view_add_friend_item.view.*
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast

class AddFriendListItemView(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {
    fun bindView(addFriendItem: AddFriendItem) {
        if (addFriendItem.isAdded){
            add.isEnabled = false
            add.text = context.getText(R.string.already_added)
        }else{
            add.isEnabled = true
            add.text = context.getText(R.string.add)
        }
        userName.text = addFriendItem.userName
        timestamp.text = addFriendItem.timeStamp

        add.setOnClickListener { addFriend(addFriendItem.userName) }
    }

    private fun addFriend(userName: String) {
        EMClient.getInstance().contactManager().aysncAddContact(userName,null,object:EMCallbackAdapter(){
            override fun onSuccess() {
                context.runOnUiThread { toast(R.string.send_add_friend_success) }
            }

            override fun onError(p0: Int, p1: String?) {
                context.runOnUiThread { toast(R.string.send_add_friend_failed) }
            }
        })
    }

    init {
        View.inflate(context, R.layout.view_add_friend_item,this)
    }
}