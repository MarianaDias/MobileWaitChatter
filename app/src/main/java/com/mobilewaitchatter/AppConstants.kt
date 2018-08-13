package com.mobilewaitchatter

import com.mobilewaitchatter.model.Vocabulary
import com.mobilewaitchatter.model.Vocabulary_Flashcards

/**
 * Created by mariana on 07/05/2018.
 */
object AppConstants {
    const val USER_NAME = "USER_NAME"
    const val USER_ID = "USER_ID"

    val exampleWords_level1 = listOf<Vocabulary>(
            Vocabulary("Igreja","Church","Places",1),
            Vocabulary("Escola","School","Places",1),
            Vocabulary("Gato","Cat","Animals",1),
            Vocabulary("Cachorro","Dog","Animals",1),
            Vocabulary("Arroz","Rice","Foods",1),
            Vocabulary("Pao","Bread","Foods",1),
            Vocabulary("Professor","Teacher","Professions",1),
            Vocabulary("Dançarino","Dancer","Professions",1))


    val exampleWords_level02 = listOf<Vocabulary>(
            Vocabulary("Industria","Industry","Places",1),
            Vocabulary("Hotel","Hotel","Places",1),
            Vocabulary("Macaco","Monkey","Animals",1),
            Vocabulary("Leão","Lion","Animals",1),
            Vocabulary("Feijão","Beans","Foods",1),
            Vocabulary("Frango","Chicken","Foods",1),
            Vocabulary("Engenheiro","Engineer","Professions",1),
            Vocabulary("Bombeiro","Firefighter","Professions",1))


    val groups = listOf<String>(
            "Places", "Animals", "Foods","Professions"
    )

    var vocabularyFlashcards = Vocabulary_Flashcards()
}