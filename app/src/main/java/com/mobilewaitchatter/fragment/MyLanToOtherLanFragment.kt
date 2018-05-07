package com.mobilewaitchatter.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobilewaitchatter.R

/**
 * Created by mariana on 07/05/2018.
 */
class MyLanToOtherLanFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_mylan_to_otherlan, container, false)
    }
}