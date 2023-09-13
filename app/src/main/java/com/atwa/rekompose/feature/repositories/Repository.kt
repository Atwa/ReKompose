package com.atwa.rekompose.feature.repositories

import com.atwa.rekompose.ui.theme.*
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class Repository(
    val id: Int,
    val ownerName: String,
    val ownerImageUrl: String?,
    val name: String,
    val description: String?,
    val language: String?,
    val stars: Int?,
) {
    val languageColor
        get() = when (language) {
            "Python" -> BlueNavy
            "C++" -> Pink
            "Java" -> BrownOrange
            "Kotlin" -> Purple100
            "TypeScript" -> Blue
            "Go" -> Cyan
            "JavaScript" -> Yellow
            "Julia" -> Purple400
            else -> Grey200
        }
}

