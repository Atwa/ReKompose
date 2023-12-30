package com.atwa.rekompose.feature.filter

data class LanguageFilter(
    val id:Int,
    val language:String,
    val isSelected:Boolean = false
)