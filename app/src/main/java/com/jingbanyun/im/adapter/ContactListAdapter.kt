package com.jingbanyun.im.adapter

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hyphenate.chat.EMClient
import com.jingbanyun.im.R
import com.jingbanyun.im.data.ContactListItem
import com.jingbanyun.im.ui.activity.ChatActivity
import com.jingbanyun.im.widget.ContactListItemView
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ContactListAdapter(private val context: Context,private val  contactListItems: MutableList<ContactListItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactListItemViewHolder(ContactListItemView(context))
    }

    override fun getItemCount(): Int = contactListItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contactListItemView = holder.itemView as ContactListItemView
        contactListItemView.bindView(contactListItems[position])
        val userName = contactListItems[position].userName
        contactListItemView.setOnClickListener { context.startActivity<ChatActivity>("username" to userName) }
        contactListItemView.setOnLongClickListener {
            val message = String.format(context.getString(R.string.delete_friend_message), userName)
            AlertDialog.Builder(context)
                    .setTitle(R.string.delete_friend_title)
                    .setMessage(message)
                    .setNegativeButton(R.string.cancel,null)
                    .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialogInterface, i ->
                        deleteFriend(userName)
                    })
                    .show()
            true
        }
    }

    private fun deleteFriend(userName: String) {
        EMClient.getInstance().contactManager().aysncDeleteContact(userName,object:EMCallbackAdapter(){
            override  fun onSuccess() {
                context.runOnUiThread { toast(R.string.delete_friend_success) }
            }

            override  fun onError(p0: Int, p1: String?) {
                context.runOnUiThread { toast(R.string.delete_friend_failed) }
            }
        })
    }

    class ContactListItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }
}