package com.dennis.newsapp.models

import java.io.Serializable

data class Source (
    val category: String,
    val country: String,
    val description: String,
    val id: String,
    val language: String,
    val name: String,
    val url: String,
    var isSelected: Boolean
): Serializable {
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(name.isNullOrEmpty()){
            result = 31 * result + name.hashCode()
        }
        return result
    }

    override fun equals(other: Any?): Boolean {
        return (other is Source) && (other.id == this.id)
    }
}