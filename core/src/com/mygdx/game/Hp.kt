package com.mygdx.game

data class Hp(var total: Int, var current: Int = total) {
    val missing get() = total - current
    val isDead get() = current <= 0
    val isFull get() = current >= total

    fun damage(amount: Int): Boolean {
        current = maxOf(current - amount, 0)
        return isDead
    }

    fun heal(amount: Int): Boolean {
        current = minOf(current + amount, total)
        return isFull
    }
}
