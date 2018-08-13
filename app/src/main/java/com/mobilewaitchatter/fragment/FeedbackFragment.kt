package com.mobilewaitchatter.fragment

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobilewaitchatter.CoolFragmentListener

import com.mobilewaitchatter.R
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.fragment_mylan_to_otherlan.*
import org.jetbrains.anko.textColor
import android.support.v4.content.ContextCompat
import android.content.res.ColorStateList



class FeedbackFragment : Fragment() {
    private var listener: CoolFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootview = inflater.inflate(R.layout.fragment_feedback, container, false)
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
        val answer = arguments?.getBoolean("answer")
        text_feedback.text = arguments?.getString("msg")
        if (answer == false){
            text_feedback.textColor = Color.RED
        }

    }

    companion object {
        fun newInstance(answer: Boolean, msg: String): FeedbackFragment {
            val fragment = FeedbackFragment()
            val args = Bundle()
            args.putBoolean("answer", answer)
            args.putString("msg",msg)
            fragment.setArguments(args)
            return fragment
        }
    }

}
