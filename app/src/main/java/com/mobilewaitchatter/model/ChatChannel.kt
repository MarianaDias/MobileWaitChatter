package com.mobilewaitchatter.model

/**
 * Created by mariana on 07/05/2018.
 */
data class ChatChannel(val userIds: MutableList<String>) {
    constructor(): this(mutableListOf())
}