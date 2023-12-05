package com.example.mobile_programming_teamproject.home

data class ItemModel(
    val sellerID: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val price: String,
    val imageURL: String,
    val status: Boolean,
    var itemKey: String? = null,
){
    constructor(): this("","", "",0,"","", false, "")
}
