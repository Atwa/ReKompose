package com.atwa.rekompose.feature.filter.domain

data class LanguageFilter(
    val id:Int,
    val language:String,
    val isSelected:Boolean = false
)