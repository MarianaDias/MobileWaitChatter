package com.mobilewaitchatter.util

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mobilewaitchatter.model.User
import com.xwray.groupie.kotlinandroidextensions.Item
import com.google.firebase.firestore.ListenerRegistration
import com.mobilewaitchatter.recycleview.item.PersonItem
import android.util.Log
import com.mobilewaitchatter.model.ChatChannel
import com.mobilewaitchatter.model.MessageType
import com.mobilewaitchatter.model.TextMessage
import com.mobilewaitchatter.recycleview.item.TextMessageItem

/**
 * Created by mariana on 15/04/2018.
 */
object FireStoreUtil {
    private val filestoreInstance : FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}

    private val currentUserDocRef : DocumentReference
        get() = filestoreInstance.document("/users/${ FirebaseAuth.getInstance().currentUser?.uid?:
                                                        throw NullPointerException("UID is null")}")

    private val chatChannelCollectionRef = filestoreInstance.collection("chatChannels")


    fun initCurrentUserIfFirstTime(onComplete: () -> Unit){
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()){
                val newUser = User(FirebaseAuth.getInstance().currentUser?.displayName ?: "","",null)
                currentUserDocRef.set(newUser).addOnSuccessListener { onComplete() }
            }
            else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(name: String= "", bio: String ="", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePicturePath != null) userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser (onComplete: (User) -> Unit){
        currentUserDocRef.get().addOnSuccessListener {
            onComplete(it.toObject(User::class.java))
        }
    }

    fun addUsersListener(context: Context, onListen:(List<Item>) -> Unit): ListenerRegistration {
        return filestoreInstance.collection("users")
                .addSnapshotListener{ querySnapshot, firebaseFirestoreException ->
                    if(firebaseFirestoreException != null){
                        Log.e("FIRESTORE","User Listener Error", firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val items = mutableListOf<Item>()
                    querySnapshot.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid ){
                            items.add(PersonItem(it.toObject(User::class.java), it.id, context))
                        }
                    }
                    onListen(items)
                }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(otherUserId: String,
                                onComplete: (channelId: String) -> Unit){
        currentUserDocRef.collection("engagedChatChannels")
                .document(otherUserId).get().addOnSuccessListener {
                    if(it.exists()){
                        onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }
                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
                    val newChannel = chatChannelCollectionRef.document()
                    newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                    currentUserDocRef.collection("engagedChatChannels").document(otherUserId)
                            .set(mapOf("channelId" to newChannel.id))
                    filestoreInstance.collection("users").document(otherUserId)
                            .collection("engagedChatChannels")
                            .document(currentUserId)
                            .set(mapOf("channelId" to newChannel.id))

                    onComplete(newChannel.id)
                }
    }

    fun addChatMessagesListener(channelId: String,context: Context,
                                onListen: (List<Item>) -> Unit) : ListenerRegistration{
        return chatChannelCollectionRef.document(channelId).collection("messages")
                .orderBy("time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if(firebaseFirestoreException != null){
                        Log.e("Firestore","chatMessagesListenerError",firebaseFirestoreException)
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<Item>()
                    querySnapshot.documents.forEach {
                        if(it["type"] == MessageType.TEXT){
                            items.add(TextMessageItem(it.toObject(TextMessage::class.java),context))
                        }
                        else{
                            //todo: add image message
                        }
                    }
                    onListen(items)
                }
    }

}