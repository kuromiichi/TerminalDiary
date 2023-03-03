package models

import java.io.Serializable
import java.time.LocalDate

data class Entry(
    val date: LocalDate,
    val title: String,
    val content: String,
    val emotion: Emotion
) : Serializable {
    enum class Emotion {
        DEPRESSED, SAD, NEUTRAL, HAPPY, VERY_HAPPY
    }
}