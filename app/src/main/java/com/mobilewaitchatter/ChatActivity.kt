package com.mobilewaitchatter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.mobilewaitchatter.model.MessageType
import com.mobilewaitchatter.model.TextMessage
import com.mobilewaitchatter.util.FireStoreUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecycleView = true
    private lateinit var messageSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)

        FireStoreUtil.getOrCreateChatChannel(otherUserId){ channelId ->
            messagesListenerRegistration = FireStoreUtil.addChatMessagesListener(channelId, this,this::updateRecycleView)
            imageView_send.setOnClickListener{
                val messageToSend = TextMessage(edit_textmessage.text.toString(),
                                                Calendar.getInstance().time,
                                                FirebaseAuth.getInstance().currentUser!!.uid,
                                                MessageType.TEXT)
                edit_textmessage.setText("")
                FireStoreUtil.sendMessage(messageToSend,channelId)
            }

            fab_send_image.setOnClickListener{
                //todo send image message
            }

        }
    }

    private fun updateRecycleView(messages: List<Item>){
        fun init(){
            recycler_view_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messageSection = Section(messages)
                    this.add(messageSection)
                }
            }
            shouldInitRecycleView = false
        }

        fun updateItems() = messageSection.update(messages)

        if(shouldInitRecycleView)
            init()
        else
            updateItems()
        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter.itemCount -1)
    }
}
