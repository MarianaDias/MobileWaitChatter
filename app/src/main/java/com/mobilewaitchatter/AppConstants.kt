package com.mobilewaitchatter

import com.mobilewaitchatter.model.Vocabulary
import com.mobilewaitchatter.model.Vocabulary_Flashcards

/**
 * Created by mariana on 07/05/2018.
 */
object AppConstants {
    const val USER_NAME = "USER_NAME"
    const val USER_ID = "USER_ID"

    val groups = listOf<String>(
            "places", "animals","hello"
    )

    var vocabularyFlashcards = Vocabulary_Flashcards()
}