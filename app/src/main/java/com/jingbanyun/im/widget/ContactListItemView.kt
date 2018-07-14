package com.jingbanyun.im.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.jingbanyun.im.R
import com.jingbanyun.im.data.ContactListItem
import kotlinx.android.synthetic.main.view_contact_item.view.*

class ContactListItemView(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {
    fun bindView(contactListItem: ContactListItem) {
        if (contactListItem.showFirstLetter) {
            firstLetter.visibility = View.VISIBLE
            firstLetter.text = contactListItem.firstLetter.toString()
        } else firstLetter.visibility = View.GONE
        userName.text = contactListItem.userName
    }

    init {
        View.inflate(context, R.layout.view_contact_item, this)
    }
}