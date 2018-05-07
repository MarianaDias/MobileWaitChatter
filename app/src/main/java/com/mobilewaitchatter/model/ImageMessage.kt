package com.mobilewaitchatter.model

import java.util.*

/**
 * Created by maria on 07/05/2018.
 */
data class ImageMessage(val imagePath: String,
                       override val time: Date,
                       override val senderId: String,
                       override val type: String = MessageType.IMAGE): Message {
    constructor(): this("", Date(0),"")

}