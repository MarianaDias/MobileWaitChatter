package com.mobilewaitchatter

import com.mobilewaitchatter.model.Vocabulary

/**
 * Created by mariana on 07/05/2018.
 */
object AppConstants {
    const val USER_NAME = "USER_NAME"
    const val USER_ID = "USER_ID"

   // var CURRENT_VOC = Vocabulary("","","",1)
    var USER_LEVEL = 1

    val exampleWords_level1 = listOf<Vocabulary>(
            Vocabulary("Igreja","Church","Places",1),
            Vocabulary("Gato","Cat","Animals",1),
            Vocabulary("Festa","Party","Events",1),
            Vocabulary("Bom","Good","Adv",1),
            Vocabulary("Pao","Bread","Food",1))

    val exampleWords_level02 = listOf<Vocabulary>(
            Vocabulary("Sorte","Luck","hello",2),
            Vocabulary("Engenheiro","Engineer","Professions",2),
            Vocabulary("Cavalo Marinho","Seahorse","Animals",2)
    )
}