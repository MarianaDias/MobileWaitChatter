package com.mobilewaitchatter.model

/**
 * Created by mariana on 08/05/2018.
 */
data class Vocabulary(val word_mylan: String, val word_otherlan: String, val group: String, val level: Int) {
    constructor(): this("","","",1)

    val exampleWords = listOf<Vocabulary>(
            Vocabulary("Igreja","Church","Places",1),
            Vocabulary("Gato","Cat","Animals",1),
            Vocabulary("Festa","Party","Events",1),
            Vocabulary("Bom","Good","Adv",1),
            Vocabulary("Sorte","Luck","hello",2),
            Vocabulary("Pao","Bread","Food",1))
}




