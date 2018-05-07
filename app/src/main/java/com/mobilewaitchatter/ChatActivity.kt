package com.mobilewaitchatter

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.mobilewaitchatter.model.ImageMessage
import com.mobilewaitchatter.model.MessageType
import com.mobilewaitchatter.model.TextMessage
import com.mobilewaitchatter.util.FireStoreUtil
import com.mobilewaitchatter.util.StorageUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2

class ChatActivity : AppCompatActivity() {

    private lateinit var currentChannelId : String
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
            currentChannelId = channelId
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
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("images/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent,"Selectione a Imagem: "), RC_SELECT_IMAGE)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver,selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG,90,outputStream)

            val selectedImageBytes = outputStream.toByteArray()

            StorageUtil.uploadMessageImage(selectedImageBytes){ imagePath ->
                val messageToSend = ImageMessage(imagePath, Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid)
                FireStoreUtil.sendMessage(messageToSend,currentChannelId)
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
