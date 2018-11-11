package com.mobilewaitchatter.fragment

import android.content.Context

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import com.mobilewaitchatter.util.FireStoreUtil
import com.mobilewaitchatter.util.RecomendationUtil
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.*


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
        if (AppConstants.vocabularyFlashcards.flahshcards.count() > 0) {
            val currentVoc = getVocabulary()
            text_mylan.text = currentVoc.word_mylan

            imageView_nextLesson_other_lan.setOnClickListener {
                if (getAsware(currentVoc.word_otherlan, editText_otherlan.text.toString())) {
                    listener?.changeFragment(FeedbackFragment.newInstance(true, "Muito Bom!!!"))
                    AppConstants.vocabularyFlashcards.count_correct += 1
                    thread.start()
                } else {
                    listener?.changeFragment(FeedbackFragment.newInstance(false, currentVoc.word_mylan+" -> " + currentVoc.word_otherlan))
                    thread.start()
                }
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
                    AppConstants.vocabularyFlashcards.current = -1
                    listener?.changeFragment(LoadingFragment())
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