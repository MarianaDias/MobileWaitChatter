package com.mobilewaitchatter.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.metalab.asyncawait.async
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import com.mobilewaitchatter.util.FireStoreUtil
import java.util.*
import android.util.Log
import org.jetbrains.anko.AnkoLogger

class LoadingFragment : Fragment(), AnkoLogger {

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
            val loaded = await { getVocabularyFlashcards()}
            listener?.changeFragment(NewVocabularyFragment.newInstance(loaded))
        }
    }

    fun getVocabularyFlashcards() : Boolean {
        val index = Random().nextInt(AppConstants.groups.count())
        var completed = false
        val group = AppConstants.groups[index]


        FireStoreUtil.getUserLevelByGroup(group){ userLevel ->
            FireStoreUtil.getWordGroup(group) { words ->
                var wordsFromLevel = mutableListOf<Vocabulary>()
                AppConstants.vocabularyFlashcards.flahshcards.clear()
                var n = 0
                Log.d("Count",words.count().toString())
                while (n < 5) {
                    for (word in words){
                        Log.d(word.word_mylan,word.level.toString())
                        if (word.level == userLevel) {
                            Log.d("add","add")
                            wordsFromLevel.add(word)
                            n = n +1
                        }
                    }
                }
                AppConstants.vocabularyFlashcards.flahshcards = wordsFromLevel
                AppConstants.vocabularyFlashcards.count_correct = 0
                AppConstants.vocabularyFlashcards.max_count =  AppConstants.vocabularyFlashcards.flahshcards.count()
                AppConstants.vocabularyFlashcards.current = 0
                AppConstants.vocabularyFlashcards.current_group = group
                completed = true
            }
        }
        while (AppConstants.vocabularyFlashcards.current != 0){
            continue
        }
        return false
    }
}