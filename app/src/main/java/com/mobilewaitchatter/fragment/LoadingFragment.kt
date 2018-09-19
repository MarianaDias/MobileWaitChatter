package com.mobilewaitchatter.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.metalab.asyncawait.async
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.ChatActivity
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import com.mobilewaitchatter.model.Vocabulary_Flashcards
import com.mobilewaitchatter.util.FireStoreUtil
import kotlinx.android.synthetic.main.fragment_loading.*
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class LoadingFragment : Fragment() {

    private var listener: CoolFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_loading, container, false)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CoolFragmentListener){
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        async{
            val loaded = await { getVocabularyFlashcards() }
            listener?.changeFragment(NewVocabularyFragment.newInstance(loaded))
        }
    }

    fun getVocabularyFlashcards() : Boolean {
        val index = Random().nextInt(AppConstants.groups.count())
        var completed = false
        val group = AppConstants.groups[index]
        AppConstants.vocabularyFlashcards.current_group = group

        FireStoreUtil.getUserLevelByGroup(group){ userLevel ->
            FireStoreUtil.getWordGroup(group) { words ->
                var wordsFromLevel = mutableListOf<Vocabulary>()
                AppConstants.vocabularyFlashcards.flahshcards.clear()
                for (i in 0..4){
                    wordsFromLevel.add(words[i])
                }
                AppConstants.vocabularyFlashcards.flahshcards = wordsFromLevel
                AppConstants.vocabularyFlashcards.count_correct = 0
                AppConstants.vocabularyFlashcards.max_count =  AppConstants.vocabularyFlashcards.flahshcards.count()
                AppConstants.vocabularyFlashcards.current = 0
                completed = true
            }
        }
        while (AppConstants.vocabularyFlashcards.current != 0){
            continue
        }
        return false
    }
}