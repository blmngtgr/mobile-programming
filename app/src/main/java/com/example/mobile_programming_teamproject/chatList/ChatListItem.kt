package com.example.mobile_programming_teamproject.chatList

data class ChatListItem(
    val buyerId: String,
    val sellerId :String,
    val buyerName: String,
    val sellerName: String,
    val itemTitle: String,
    val key: Long
) {

    constructor(): this("", "", "", "","",0)
}