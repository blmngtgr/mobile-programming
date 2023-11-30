package com.example.mobile_programming_teamproject.chatdata

data class ChatItem (
    val senderId: String,
    val message: String,
) {
    constructor(): this("", "")
}