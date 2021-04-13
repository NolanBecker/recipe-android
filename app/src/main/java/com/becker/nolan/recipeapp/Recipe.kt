package com.becker.nolan.recipeapp

data class Recipe(
    val id: Int,
    val name: String,
    val ingredients: List<Int> = ArrayList(),
    val tags: List<Int> = ArrayList(),
    val directions: String,
    val link: String
)