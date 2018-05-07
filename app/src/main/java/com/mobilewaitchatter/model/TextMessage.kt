package com.mobilewaitchatter.model
/**
 * Created by mariana on 07/05/2018.
 */

import java.util.Date

data class TextMessage(val text: String,
                        override val time: Date,
                        override val senderId: String,
                        override val type: String = MessageType.TEXT): Message {
    constructor(): this("",Date(0),"")

}