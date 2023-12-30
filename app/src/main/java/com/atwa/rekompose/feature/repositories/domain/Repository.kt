package com.atwa.rekompose.feature.repositories.domain

import com.atwa.rekompose.designsystem.theme.Blue
import com.atwa.rekompose.designsystem.theme.BlueNavy
import com.atwa.rekompose.designsystem.theme.BrownOrange
import com.atwa.rekompose.designsystem.theme.Cyan
import com.atwa.rekompose.designsystem.theme.Grey200
import com.atwa.rekompose.designsystem.theme.Pink
import com.atwa.rekompose.designsystem.theme.Purple100
import com.atwa.rekompose.designsystem.theme.Purple400
import com.atwa.rekompose.designsystem.theme.Yellow

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

