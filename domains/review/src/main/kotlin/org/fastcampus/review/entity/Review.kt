package org.fastcampus.review.entity

import java.time.LocalDateTime

class Review(
    val id: Long? = null,
    val orderId: String,
    val userId: Long,
    val storeId: String,
    val clientReviewContent: String,
    val totalScore: Int,
    val tasteScore: Int,
    val amountScore: Int,
    val representativeImageUri: String? = null,
    val deliveryQuality: DeliveryQuality? = null,
    val adminUserId: Long? = null,
    val adminReviewContent: String? = null,
    val adminReviewedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    enum class DeliveryQuality(
        val code: String,
        val desc: String,
    ) {
        GOOD("d1", "Good"),
        BAD("d2", "Bad"),
    }

    enum class Sort(
        val code: String,
        val desc: String,
    ) {
        LATEST("s1", "latest"),
        SCORE("s2", "score"),
    }

    enum class AnswerType(
        val code: String,
        val desc: String,
    ) {
        ALL("a1", "all"),
        OWNER_ANSWERED("a2", "owner_answered"),
        OWNER_NOT_ANSWERED("a3", "owner_not_answered"),
    }
}
