package com.example.lab1.utils

fun nullableExamples() {
    val text: String? = null
    println(text?.length)
    val len = text?.length ?: 0
}