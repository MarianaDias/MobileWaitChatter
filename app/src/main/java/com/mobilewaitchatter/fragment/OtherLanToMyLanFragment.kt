package com.mobilewaitchatter.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mobilewaitchatter.CoolFragmentListener
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Vocabulary
import kotlinx.android.synthetic.main.fragment_new_vocabulary.*
import kotlinx.android.synthetic.main.fragment_otherlan_to_mylan.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by maria on 07/05/2018.
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
        imageView_nextLesson_mylan.setOnClickListener{
            if(get_asware("casa", editText_myLan.text.toString())){
                listener?.changeFragment(FeedbackFragment())
                thread.start()
            }
            else{
                //TODO: Change Feedback Text
                listener?.changeFragment(FeedbackFragment())
                thread.start()
            }
        }
    }



    fun get_asware(myvocabulary: String, word: String): Boolean{
        return word.toLowerCase() == myvocabulary.toLowerCase()
    }

    var thread: Thread = object : Thread() {
        override fun run() {
            try {
                Thread.sleep(1000)
                listener?.changeFragment(NewVocabularyFragment())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}