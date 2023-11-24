package com.example.mobile_programming_teamproject.chatdata

data class ChatItem (
    val senderId: String,
    val senderName: String,
    val message: String,
) {
    constructor(): this("", "", "")
}