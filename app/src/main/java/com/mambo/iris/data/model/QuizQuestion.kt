package com.mambo.iris.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val answerIndex: Int
)