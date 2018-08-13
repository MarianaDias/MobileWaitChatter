package com.mobilewaitchatter.model

/**
 * Created by maria on 13/08/2018.
 */
data class Vocabulary_Flashcards(var flahshcards : MutableList<Vocabulary>, var count_correct: Int,var current:Int, var max_count:Int) {
    constructor(): this(mutableListOf<Vocabulary>(), 0,0,5)
}
