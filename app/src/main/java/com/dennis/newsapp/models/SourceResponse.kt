package com.dennis.newsapp.models

data class SourceResponse(
    val sources: List<Source>,
    val status: String
)