package com.mobilewaitchatter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ListenerRegistration
import com.mobilewaitchatter.util.FireStoreUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import org.jetbrains.anko.toast

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME)

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)

        FireStoreUtil.getOrCreateChatChannel(otherUserId){ channelId ->
            messagesListenerRegistration = FireStoreUtil.addChatMessagesListener(channelId, this,this::onMessagesChanged)

        }
    }

    private fun onMessagesChanged(messages: List<Item>){
        toast("onmessagesChanged Running")
    }
}
