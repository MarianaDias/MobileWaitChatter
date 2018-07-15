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
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.*
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*
import kotlinx.android.synthetic.main.fragment_otherlan_to_mylan.*
import org.jetbrains.anko.support.v4.toast
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.view.*
import kotlinx.android.synthetic.main.fragment_otherlan_to_mylan.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by mariana on 07/05/2018.
 */
class MyLanToOtherLanFragment : Fragment() {

  /*  val exampleWords = createExampleWords()
    private fun createExampleWords(): List<Vocabulary> {
        val word1 = Vocabulary("Casa", "House")
        val word2 = Vocabulary("Cachorro", "Dog")
        val word3 = Vocabulary("Mulher", "Woman")
        val word4 = Vocabulary("Agua", "Water")
        val word5 = Vocabulary("Suco", "Juice")
        val exampleWords = listOf<Vocabulary>(word1, word2, word3, word4, word5)
        return exampleWords
    }*/
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
        imageView_nextLesson_other_lan.setOnClickListener {
            listener?.changeFragment(OtherLanToMyLanFragment())
        }

    }
}