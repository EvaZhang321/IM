package com.jingbanyun.im.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jingbanyun.im.data.AddFriendItem
import com.jingbanyun.im.widget.AddFriendListItemView

class AddFriendListAdapter(val context: Context,val addFriendItems: MutableList<AddFriendItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddFriendListItemViewHolder(AddFriendListItemView(context))
    }

    override fun getItemCount(): Int = addFriendItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val addFriendListItemView = holder.itemView as AddFriendListItemView
        addFriendListItemView.bindView(addFriendItems[position])
    }

    class AddFriendListItemViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView)
}