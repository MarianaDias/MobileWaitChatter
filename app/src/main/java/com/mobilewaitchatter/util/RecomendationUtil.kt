package com.mobilewaitchatter.util

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.kotlinandroidextensions.Item
import com.google.firebase.firestore.ListenerRegistration
import com.mobilewaitchatter.recycleview.item.PersonItem
import android.util.Log
import android.widget.Toast
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.ChatActivity
import com.mobilewaitchatter.model.*
import com.mobilewaitchatter.recycleview.item.ImageMessageItem
import com.mobilewaitchatter.recycleview.item.TextMessageItem
import java.util.*

/**
 * Created by mariana on 18/08/2018.
 */
object RecomendationUtil {

    fun getNextLevelForGroup(flashcardsCorrectCount: Int, group: String, onComplete:(nextLevel: Int) -> Unit){
        var nextLevel = 1
        FireStoreUtil.getUserLevelByGroup(group){level ->
            if (flashcardsCorrectCount == 5)
                nextLevel= level+1
            else if (flashcardsCorrectCount == 4)
                nextLevel = level //todo: confirmar o que fazer nesse caso
            else if ((flashcardsCorrectCount == 3) or (flashcardsCorrectCount == 2))
                nextLevel = level + 5
            else
                if( level > 1)
                    nextLevel = level -1
            onComplete(nextLevel)
        }
    }
}