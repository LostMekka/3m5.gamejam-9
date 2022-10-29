package com.mygdx.game

class MinionProduction(
    val minionType: MinionType,
    var factoryLevel: Int = 1,
    var factoryProgress: Float = 0f,
    var minionCountInside: Float = 0f,
    var minionCountOutside: Float = 0f,
)
