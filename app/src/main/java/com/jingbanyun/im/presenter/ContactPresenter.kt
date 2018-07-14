package com.jingbanyun.im.presenter

import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.jingbanyun.im.contract.ContactContract
import com.jingbanyun.im.data.ContactListItem
import com.jingbanyun.im.data.db.Contact
import com.jingbanyun.im.data.db.IMDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class ContactPresenter(val view:ContactContract.View):ContactContract.Presenter {
    val contactListItems = mutableListOf<ContactListItem>()
    override fun loadContacts() {
        doAsync {
            //再次加载数据，先清空集合
            contactListItems.clear()
            //清空数据库
            IMDatabase.instance.deleteAllContact()
            try {
                val usernames = EMClient.getInstance().contactManager().allContactsFromServer
                //根据首字符排序
                usernames.sortBy { it[0] }
                usernames.forEachIndexed { index, s ->
                    //判断是否显示首字符
                    //第一个条目显示首字母，从第二个开始，如果和上一个条目首字母不一样，就显示
                    val showFirstLetter = index==0 ||s[0]!=usernames[index-1][0]
                    val contactListItem = ContactListItem(s,s[0].toUpperCase(),showFirstLetter)
                    contactListItems.add(contactListItem)

                    val contact = Contact(mutableMapOf("name" to s))
                    IMDatabase.instance.saveContact(contact)
                }
                uiThread { view.onLoadContactSuccess() }
            } catch (e: HyphenateException) {
                uiThread { view.onLoadContactFailed() }
            }
        }
    }
}