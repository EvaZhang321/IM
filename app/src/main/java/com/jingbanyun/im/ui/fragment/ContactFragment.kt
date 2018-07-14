package com.jingbanyun.im.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hyphenate.chat.EMClient
import com.jingbanyun.im.R
import com.jingbanyun.im.adapter.ContactListAdapter
import com.jingbanyun.im.adapter.EMContactListenerAdapter
import com.jingbanyun.im.contract.ContactContract
import com.jingbanyun.im.presenter.ContactPresenter
import com.jingbanyun.im.ui.activity.AddFriendActivity
import com.jingbanyun.im.widget.SlideBar
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ContactFragment : BaseFragment(), ContactContract.View {

    val presenter by lazy { ContactPresenter(this) }

    private val contactListener = object : EMContactListenerAdapter() {
        override fun onContactDeleted(p0: String?) {
            //重新获取联系人数据
            presenter.loadContacts()
        }

        override fun onContactAdded(p0: String?) {
            //重新获取联系人数据
            presenter.loadContacts()
        }
    }

    override fun onLoadContactSuccess() {
        swipeRefreshLayout.isRefreshing = false
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onLoadContactFailed() {
        swipeRefreshLayout.isRefreshing = false
        context?.toast(R.string.load_contacts_failed)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_contacts
    }

    override fun init() {
        super.init()
        initHeader()
        initSwipeRefreshLayout()
        initRecycleview()
        EMClient.getInstance().contactManager().setContactListener(contactListener)
        initSlideBar()

        presenter.loadContacts()
    }

    private fun initSlideBar() {
        slideBar.onSectionListener = object : SlideBar.OnSectionChangeListener {
            override fun onSectionChange(firstLetter: String) {
                section.visibility = View.VISIBLE
                section.text = firstLetter
                val position = getPosition(firstLetter)
                if (position != -1)
                    recyclerView.smoothScrollToPosition(position)
            }

            override fun onSlideFinish() {
                section.visibility = View.GONE
            }
        }
    }

    private fun initRecycleview() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ContactListAdapter(context, presenter.contactListItems)
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.qq_blue)
            isRefreshing = true
            setOnRefreshListener { presenter.loadContacts() }
        }
    }

    private fun initHeader() {
        headerTitle.text = getString(R.string.contact)
        add.visibility = View.VISIBLE
        add.setOnClickListener { context?.startActivity<AddFriendActivity>() }
    }

    private fun getPosition(firstLetter: String): Int =
            presenter.contactListItems.binarySearch { contactListItem ->
                contactListItem.firstLetter.minus(firstLetter[0])
            }

    override fun onDestroy() {
        super.onDestroy()
        //移除监听器
        EMClient.getInstance().contactManager().removeContactListener(contactListener)
    }
}