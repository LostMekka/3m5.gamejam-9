package com.mygdx.game

class MinionProduction(
    val minionType: MinionType,
    var attackStrength: Float = 1f,
    var defence: Float = 1f,
    var factoryLevel: Int = 1,
    var factoryProgress: Float = 0f,
    var minionCountInside: Float = 0f,
    var minionCountOutside: Float = 0f,
    var attackMultiplier: Float = 1f,
    var defenceMultiplier: Float = 1f
)
