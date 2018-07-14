package com.jingbanyun.im.factory

import android.support.v4.app.Fragment
import com.jingbanyun.im.R
import com.jingbanyun.im.ui.fragment.ContactFragment
import com.jingbanyun.im.ui.fragment.ConversationFragement
import com.jingbanyun.im.ui.fragment.DynamicFragement

class FragmentFactory private constructor(){

    val conversation by lazy { ConversationFragement() }
    val contact by lazy { ContactFragment() }
    val dynamic by lazy { DynamicFragement() }

    companion object {
        val instance = FragmentFactory()
    }

    fun getFragment(tabId: Int):Fragment? {
        when (tabId) {
            R.id.tab_conversation-> return conversation
            R.id.tab_contacts-> return contact
            R.id.tab_dynamic -> return dynamic
        }
        return null
    }
}