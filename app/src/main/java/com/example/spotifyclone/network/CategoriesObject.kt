package com.example.spotifyclone.network

data class CategoriesObject (
    val categories: Categories
        )

data class Categories(
    val items: List<Category>
)

data class Category(
    val id: String,
    val name: String,
    val icons: List<Image>?= listOf()
)