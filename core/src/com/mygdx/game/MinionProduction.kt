package com.mygdx.game

class MinionProduction(
    val minionType: MinionType,
    var offence:Int=1,
    var defence:Int=1,
    var factoryLevel: Int = 1,
    var factoryProgress: Float = 0f,
    var minionCountInside: Float = 0f,
    var minionCountOutside: Float = 0f
)
