package com.mobilewaitchatter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import com.mobilewaitchatter.util.FireStoreUtil
import com.mobilewaitchatter.util.RecomendationUtil
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*
import kotlinx.android.synthetic.main.fragment_otherlan_to_mylan.*
import org.jetbrains.anko.support.v4.toast
import java.util.*

/**
 * Created by mariana on 07/05/2018.
 */
class OtherLanToMyLanFragment: Fragment() {

    private var listener: CoolFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_otherlan_to_mylan, container, false)
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
        text_otherlan.text = currentVoc.word_otherlan
        imageView_nextLesson_mylan.setOnClickListener{
            if(getAsware(currentVoc.word_mylan, editText_myLan.text.toString())){
                listener?.changeFragment(FeedbackFragment.newInstance(true,"Muito Bom"))
                AppConstants.vocabularyFlashcards.count_correct += 1
                thread.start()
            }
            else{
                listener?.changeFragment(FeedbackFragment.newInstance(false, "Desculpe, o correto é "+currentVoc.word_mylan))
                thread.start()
            }
        }
    }

    private fun getVocabulary(): Vocabulary{
        val index = AppConstants.vocabularyFlashcards.current
        AppConstants.vocabularyFlashcards.current = AppConstants.vocabularyFlashcards.current + 1
        return AppConstants.vocabularyFlashcards.flahshcards[index]
    }

    private fun getAsware(myvocabulary: String, word: String): Boolean{
        return word.toLowerCase() == myvocabulary.toLowerCase()
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
                    listener?.changeFragment(NewVocabularyFragment.newInstance(true))
                }
                else {
                    listener?.changeFragment(MyLanToOtherLanFragment())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}