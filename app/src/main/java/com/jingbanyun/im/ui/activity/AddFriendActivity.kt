package com.jingbanyun.im.ui.activity

import android.support.v7.widget.LinearLayoutManager
import com.jingbanyun.im.R
import com.jingbanyun.im.adapter.AddFriendListAdapter
import com.jingbanyun.im.contract.AddFriendContract
import com.jingbanyun.im.presenter.AddFriendPresenter
import kotlinx.android.synthetic.main.activity_add_friend.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

class AddFriendActivity : BaseActivity(), AddFriendContract.View {

    val presenter =AddFriendPresenter(this)

    override fun onSearchSuccess() {
        dismissDialogProgress()
        toast(R.string.search_success)
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onSearchFailed() {
        dismissDialogProgress()
        toast(R.string.search_failed)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_add_friend
    }

    override fun init() {
        super.init()
        headerTitle.text = getString(R.string.add_friend)

        search.setOnClickListener { search() }
        userName.setOnEditorActionListener { textView, i, keyEvent ->
            search()
            true
        }

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(context)
            adapter = AddFriendListAdapter(context,presenter.addFriendItems)
        }

        search()
    }

    fun search(){
        hideSoftKeyborad()
        showProgress(getString(R.string.searching))
        val key = userName.text.trim().toString()
        presenter.search(key)
    }
}