package com.mobilewaitchatter.recycleview.item

import android.content.Context
import com.mobilewaitchatter.model.User
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import com.mobilewaitchatter.R
import com.mobilewaitchatter.glide.GlideApp
import com.mobilewaitchatter.util.StorageUtil
import kotlinx.android.synthetic.main.item_person.*

/**
 * Created by mariana on 02/05/2018.
 */
class PersonItem (val person: User, val userId: String, private val context: Context) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio
        if(person.profilePicturePath != null){
            GlideApp.with(context)
                    .load(StorageUtil.pathToReference(person.profilePicturePath))
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(viewHolder.imageView_profile_picture)
        }
    }

    override fun getLayout() = R.layout.item_person //To change body of created functions use File | Settings | File Templates.

}