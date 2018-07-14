package com.jingbanyun.im.presenter

import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.hyphenate.chat.EMClient
import com.jingbanyun.im.contract.AddFriendContract
import com.jingbanyun.im.data.AddFriendItem
import com.jingbanyun.im.data.db.IMDatabase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AddFriendPresenter(val view: AddFriendContract.View) : AddFriendContract.Presenter {
    val addFriendItems = mutableListOf<AddFriendItem>()

    override fun search(key: String) {
        val query = BmobQuery<BmobUser>()
        query.addWhereContains("username", key)
                .addWhereNotEqualTo("username", EMClient.getInstance().currentUser)
        query.findObjects(object : FindListener<BmobUser>() {
            override fun done(p0: MutableList<BmobUser>?, p1: BmobException?) {
                if (p1 == null) {
                    addFriendItems.clear()
                    //处理数据
                    val contacts = IMDatabase.instance.getAllContacts()
                    doAsync {
                        p0?.forEach {
                            //对比是否已经添加过好友
                            var isAdded = false
                            for (contact in contacts){
                                if (contact.name == it.username){
                                    isAdded =true
                                }
                            }

                            val addFriendItem = AddFriendItem(it.username, it.createdAt,isAdded)
                            addFriendItems.add(addFriendItem)
                        }
                        uiThread { view.onSearchSuccess() }
                    }

                } else view.onSearchFailed()
            }
        })
    }
}