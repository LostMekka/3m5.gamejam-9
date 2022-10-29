package com.mygdx.game

data class Hp(var total: Int, var current: Int = total) {
    val missing get() = total - current
    val isDead get() = current <= 0
}
