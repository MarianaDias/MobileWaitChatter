package com.mobilewaitchatter

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.mobilewaitchatter.fragment.MyAccountFragment
import com.mobilewaitchatter.fragment.MyLanToOtherLanFragment
import com.mobilewaitchatter.fragment.NewVocabularyFragment
import com.mobilewaitchatter.fragment.OtherLanToMyLanFragment
import com.mobilewaitchatter.model.*
import com.mobilewaitchatter.util.FireStoreUtil
import com.mobilewaitchatter.util.StorageUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.*
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.view.*
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*
import kotlinx.android.synthetic.main.fragment_otherlan_to_mylan.*
import org.jetbrains.anko.db.NULL
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2

interface CoolFragmentListener {
    fun changeFragment(fragment: android.support.v4.app.Fragment)
    fun getVocabularyFlashcards(user_i: Int)
}

class ChatActivity : AppCompatActivity(), CoolFragmentListener  {

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

        changeFragment(NewVocabularyFragment())

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


    override fun changeFragment(fragment: android.support.v4.app.Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.application_fragment, fragment)
                .commit()
    }

    override fun getVocabularyFlashcards(user_i: Int)  {

        FireStoreUtil.getWordGroup { words ->
            var flashcards = Vocabulary_Flashcards()
            val user_level = 1
            var wordsFromLevel = mutableListOf<Vocabulary>()
            words.forEach {
                if (it.level == user_level)
                    wordsFromLevel.add(it)
            }
            wordsFromLevel.forEach {
                flashcards.flahshcards.add(it)
            }

            flashcards.count_correct = 0
            flashcards.current = 0
            flashcards.max_count = flashcards.flahshcards.count()
            AppConstants.vocabularyFlashcards = flashcards
        }
    }
}
