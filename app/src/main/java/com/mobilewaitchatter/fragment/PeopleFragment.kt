package com.mobilewaitchatter.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ListenerRegistration
import com.mobilewaitchatter.AppConstants
import com.mobilewaitchatter.ChatActivity

import com.mobilewaitchatter.R
import com.mobilewaitchatter.recycleview.item.PersonItem
import com.mobilewaitchatter.util.FireStoreUtil
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_people.*
import org.jetbrains.anko.support.v4.startActivity

class PeopleFragment : Fragment() {

    private lateinit var userListenerResistration :ListenerRegistration

    private var shouldInitRecycleView = true

    private lateinit var peopleSection: Section

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        userListenerResistration = FireStoreUtil.addUsersListener(this.activity!!, this::updateRecycleView)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_people, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        FireStoreUtil.removeListener(userListenerResistration)
        shouldInitRecycleView = true
    }

    private fun updateRecycleView(items: List<Item>){

        fun init(){
            recycler_view_people.apply {
                layoutManager = LinearLayoutManager(this@PeopleFragment.context)
                adapter = GroupAdapter<com.xwray.groupie.kotlinandroidextensions.ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecycleView = false
        }

        fun updateItems() = peopleSection.update(items)

        if(shouldInitRecycleView)
            init()
        else
            updateItems()
    }

    private val onItemClick = OnItemClickListener{ item, view ->
            if (item is PersonItem) {
                startActivity<ChatActivity>(
                        AppConstants.USER_NAME to item.person.name,
                        AppConstants.USER_ID to item.userId
                )
            }
    }

}// Required empty public constructor
