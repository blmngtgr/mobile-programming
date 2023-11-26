package com.example.mobile_programming_teamproject.home

data class ArticleModel(
    val sellerID: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageURL: String,
    var articleKey: String? = null
){
    constructor(): this("","",0,"","", "")
}