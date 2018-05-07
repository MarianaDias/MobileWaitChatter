package com.mobilewaitchatter.recycleview.item

import android.content.Context
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

/**
 * Created by mariana on 07/05/2018.
 */
class TextMessageItem(val message: TextMessage,
                      val context: Context): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //TODO: Placeholder bind
    }

    override fun getLayout() = R.layout.item_text_message

}