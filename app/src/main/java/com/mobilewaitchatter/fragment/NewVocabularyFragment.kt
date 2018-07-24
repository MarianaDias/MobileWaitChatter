package com.mobilewaitchatter.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.view.*
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*
import org.jetbrains.anko.support.v4.toast
import java.lang.Math.random
import java.util.*

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
            listener?.changeFragment(MyLanToOtherLanFragment())
        }
        val currentVoc = get_word()
        text_mylan_newVoc.text = currentVoc.word_mylan
        text_otherlan_newVoc.text = currentVoc.word_otherlan
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CoolFragmentListener){
            listener = context
        }
    }

    private fun get_word() : Vocabulary{
        val index = Random().nextInt(3 )
        //todo: Pegar vocabulario do banco
        if (AppConstants.USER_LEVEL ==1){
            return AppConstants.exampleWords_level1[index]
        }
        return AppConstants.exampleWords_level02[index]
    }



}