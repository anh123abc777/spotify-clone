package com.example.tonezone.network

data class Owner (
    val display_name: String ?= "",
    val id: String ?= "",
    val type: String ?= "user",
    val uri: String ?= ""
    )