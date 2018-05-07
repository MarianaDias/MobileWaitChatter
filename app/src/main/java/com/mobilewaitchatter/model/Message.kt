package com.mobilewaitchatter.model

import java.util.*

/**
 * Created by mariana on 07/05/2018.
 */
object MessageType{
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}


interface Message {
    val time: Date
    val senderId: String
    val type: String
}