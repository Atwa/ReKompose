package com.atwa.rekompose.feature.filter

data class RepositoryLanguageFilter(
    val id:Int,
    val language:String,
    val isSelected:Boolean = false
)