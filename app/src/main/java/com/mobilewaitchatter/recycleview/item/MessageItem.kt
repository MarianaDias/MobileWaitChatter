package com.mobilewaitchatter.recycleview.item

import android.view.Gravity
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.mobilewaitchatter.R
import com.mobilewaitchatter.model.Message
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

/**
 * Created by mariana on 07/05/2018.
 */
abstract class MessageItem(private val message: Message): Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: ViewHolder){
        val dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT)
        viewHolder.textView_message_time.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(viewHolder: ViewHolder){
        if(message.senderId == FirebaseAuth.getInstance().currentUser?.uid){
            viewHolder.message_root.apply {
                backgroundResource = R.drawable.react_round_color_white
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }
        }
        else {
            viewHolder.message_root.apply {
                backgroundResource = R.drawable.rect_round_primary_color
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }

    }
}