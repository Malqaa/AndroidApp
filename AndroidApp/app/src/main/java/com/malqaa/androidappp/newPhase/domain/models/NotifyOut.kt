package com.malqaa.androidappp.newPhase.domain.models

data class NotifyOut(
    val body: String,
    val createdAt: String,
    val fromBusinessAccountId: Any,
    val fromUserId: String,
    val id: Int,
    val isRead: Boolean,
    val itemId: Int,
    val notificationModuleType: Int,
    val notificationType: Int,
    val readAt: Any,
    val title: String,
    val toBusinessAccountId: Any,
    val toUserId: String
)