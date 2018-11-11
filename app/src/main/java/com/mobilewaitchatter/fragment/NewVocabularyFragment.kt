package com.mobilewaitchatter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*

/**
 * Created by mariana on 07/05/2018.
 */
class NewVocabularyFragment : Fragment() {

    private var listener: CoolFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_vocabulary, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView_nextLesson_newVoc.setOnClickListener{
            if (AppConstants.vocabularyFlashcards.current == AppConstants.vocabularyFlashcards.max_count){
                AppConstants.vocabularyFlashcards.current = 0
                listener?.changeFragment(MyLanToOtherLanFragment())
            }
            else{
                text_otherlan_newVoc.text = AppConstants.vocabularyFlashcards.current.toString()
                listener?.changeFragment(NewVocabularyFragment.newInstance(false))
            }
        }
        val newFlashcards = arguments?.getBoolean("newFlashcards")
        if(newFlashcards == false){
            val currentVoc = get_word()
            text_mylan_newVoc.text = currentVoc.word_mylan
            text_otherlan_newVoc.text = currentVoc.word_otherlan
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CoolFragmentListener){
            listener = context
        }
    }

    private fun get_word() : Vocabulary{
        val index = AppConstants.vocabularyFlashcards.current
        AppConstants.vocabularyFlashcards.current = AppConstants.vocabularyFlashcards.current + 1
        return AppConstants.vocabularyFlashcards.flahshcards[index]
    }

    companion object {
        fun newInstance(newFlashcards: Boolean): NewVocabularyFragment {
            val fragment = NewVocabularyFragment()
            val args = Bundle()
            args.putBoolean("newFlashcards", newFlashcards)
            fragment.setArguments(args)
            return fragment
        }
    }

    var thread: Thread = object : Thread() {
        override fun run() {
            try {
                Thread.sleep(2000)
                listener?.changeFragment(NewVocabularyFragment.newInstance(false))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}