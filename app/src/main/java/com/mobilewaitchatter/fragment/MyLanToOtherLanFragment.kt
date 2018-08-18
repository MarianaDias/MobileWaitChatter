package com.mobilewaitchatter.fragment

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.ChatActivity
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import com.mobilewaitchatter.util.FireStoreUtil
import com.mobilewaitchatter.util.RecomendationUtil
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.*
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*
import kotlinx.android.synthetic.main.fragment_otherlan_to_mylan.*
import org.jetbrains.anko.support.v4.toast
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.view.*
import kotlinx.android.synthetic.main.fragment_otherlan_to_mylan.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


/**
 * Created by mariana on 07/05/2018.
 */
class MyLanToOtherLanFragment : Fragment() {

    private var listener: CoolFragmentListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.fragment_mylan_to_otherlan, container, false)
        return rootview
    }

    override fun onAttach(context: Context?) {
         super.onAttach(context)
         if (context is CoolFragmentListener){
             listener = context
         }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentVoc = getVocabulary()
        text_mylan.text = currentVoc.word_mylan
        imageView_nextLesson_other_lan.setOnClickListener {
            if (getAsware(currentVoc.word_otherlan, editText_otherlan.text.toString())) {
                listener?.changeFragment(FeedbackFragment.newInstance(true,"Muito Bom!!!"))
                AppConstants.vocabularyFlashcards.count_correct += 1
                thread.start()
            }
            else{
                listener?.changeFragment(FeedbackFragment.newInstance(false, "Desculpe, o correto é "+currentVoc.word_otherlan))
                thread.start()
            }
        }

    }

    private fun getAsware(my_vocabulary: String, word: String) : Boolean{
        return word.toLowerCase() == my_vocabulary.toLowerCase()
    }

    private fun getVocabulary(): Vocabulary{
        val index = AppConstants.vocabularyFlashcards.current
        AppConstants.vocabularyFlashcards.current = AppConstants.vocabularyFlashcards.current + 1
        return AppConstants.vocabularyFlashcards.flahshcards[index]
    }

    var thread: Thread = object : Thread() {
        override fun run() {
            try {
                Thread.sleep(1000)
                if (AppConstants.vocabularyFlashcards.current == AppConstants.vocabularyFlashcards.max_count){
                    AppConstants.vocabularyFlashcards.current = 0
                    RecomendationUtil.getNextLevelForGroup(AppConstants.vocabularyFlashcards.count_correct,
                            AppConstants.vocabularyFlashcards.current_group){ level ->
                        FireStoreUtil.updateUserLevelByGroup(AppConstants.vocabularyFlashcards.current_group, level)
                    }
                    listener?.getVocabularyFlashcards()
                }
                else {
                    listener?.changeFragment(OtherLanToMyLanFragment())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}