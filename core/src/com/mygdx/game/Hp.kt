package com.mygdx.game

data class Hp(var total: Int, var current: Int = total) {
    val missing get() = total - current
    val isDead get() = current <= 0

    fun damage(amount: Int): Boolean {
        current = maxOf(current - amount, 0)
        return current == 0
    }

    fun heal(amount: Int): Boolean {
        current = minOf(current + amount, total)
        return current == total
    }
}
