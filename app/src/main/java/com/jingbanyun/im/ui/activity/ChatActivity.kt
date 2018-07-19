package com.jingbanyun.im.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.jingbanyun.im.R
import com.jingbanyun.im.adapter.EMMessageListenerAdapter
import com.jingbanyun.im.adapter.MessageListAdapter
import com.jingbanyun.im.contract.ChatContract
import com.jingbanyun.im.presenter.ChatPresenter
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.toast

class ChatActivity : BaseActivity(), ChatContract.View {

    lateinit var username: String
    val presenter = ChatPresenter(this)

    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            //收到消息后，添加都消息集合
            presenter.addMessage(username, p0)
            //刷新消息列表
            runOnUiThread {
                scrollToBottom()
                recyclerView.adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onStartSendMessage() {
        //通知RecyclerView刷新列表
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onSendMessageSuccess() {
        recyclerView.adapter.notifyDataSetChanged()
        toast(R.string.send_message_success)
        //清空编辑框
        edit.text.clear()
        scrollToBottom()
    }

    private fun scrollToBottom() {
        recyclerView.scrollToPosition(presenter.messages.size - 1)
    }

    override fun onSendMessafeFailed() {
        recyclerView.adapter.notifyDataSetChanged()
        toast(R.string.send_message_failed)
        //清空编辑框
        edit.text.clear()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_chat
    }

    override fun init() {
        super.init()
        initHeader()
        initEditText()
        initRecyclerView()
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
        send.setOnClickListener { send() }
        presenter.loadMessage(username)
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MessageListAdapter(context, presenter.messages)
            addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    //当RecyclerView是一个空闲的状态，
                    //检查是否滑到了顶部，要加载更多数据
                    if (newState == RecyclerView.SCROLL_STATE_IDLE){
                        //如果第一个可见条目的位置是0，为滑到顶部
                        val linearLayoutManager = layoutManager as LinearLayoutManager
                        if (linearLayoutManager.findFirstVisibleItemPosition() == 0){
                            //加载更多数据
                            presenter.loadMoreMessage(username)
                        }
                    }
                }
            })
        }
    }

    //发送消息
    fun send() {
        hideSoftKeyborad()
        val message = edit.text.trim().toString()
        if(TextUtils.isEmpty(message)){
            toast("发送消息不能为空")
            return
        }
        presenter.sendMessage(username, message)
    }

    private fun initEditText() {
        edit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //如果用户输入的文本长度大于0，发送按钮enable
                send.isEnabled = p0.isNullOrEmpty().not()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        edit.setOnEditorActionListener { textView, i, keyEvent ->
            send()
            true
        }
    }

    private fun initHeader() {
        back.visibility = View.VISIBLE
        back.setOnClickListener { finish() }

        //获取聊天的用户名
        username = intent.getStringExtra("username")
        val titleString = String.format(getString(R.string.chat_title), username)
        headerTitle.text = titleString
    }

    override fun onDestroy() {
        super.onDestroy()
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }

    override fun onMessageLoaded() {
        recyclerView.adapter.notifyDataSetChanged()
        scrollToBottom()
    }

    override fun onMoreMessageLoaded(size: Int) {
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(size)
    }
}