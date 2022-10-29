package com.mygdx.game

class GameState(
    var factoryMaxHp: Int = 1000,
    var factoryHp: Int = factoryMaxHp,
    var doorIsOpen: Boolean = true,

    var minionTankFactoryLevel: Int = 1,
    var minionTankCountInside: Float = 0f,
    var minionTankCountOutside: Float = 0f,

    var minionArcherFactoryLevel: Int = 1,
    var minionArcherCountInside: Float = 0f,
    var minionArcherCountOutside: Float = 0f,

    var minionMinerFactoryLevel: Int = 1,
    var minionMinerCountInside: Float = 0f,
    var minionMinerCountOutside: Float = 0f,

    var resourceTriangle: Int = 100,
    var resourceCircle: Int = 0,
    var resourceSquare: Int = 0,

    var bossLevel: Int = 1,
    var bossHp: Int = 1000,
)
