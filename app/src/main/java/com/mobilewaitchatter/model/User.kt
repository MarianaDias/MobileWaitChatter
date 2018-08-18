package com.mobilewaitchatter.model

/**
 * Created by mariana on 15/04/2018.
 */
data class User(val name: String, val bio: String, val profilePicturePath: String?) {
    //Firestore needs a "constructor"
    constructor(): this("", "", null)
}