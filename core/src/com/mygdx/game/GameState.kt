package com.mygdx.game

class GameState(
    var factoryMaxHp: Int = 1000,
    var factoryHp: Int = factoryMaxHp,
    var doorIsOpen: Boolean = true,

    var minionTankFactoryLevel: Int = 1,
    var minionTankCountInside: Int = 0,
    var minionTankCountOutside: Int = 0,

    var minionArcherFactoryLevel: Int = 1,
    var minionArcherCountInside: Int = 0,
    var minionArcherCountOutside: Int = 0,

    var minionMinerFactoryLevel: Int = 1,
    var minionMinerCountInside: Int = 0,
    var minionMinerCountOutside: Int = 0,

    var resourceTriangle: Int = 100,
    var resourceCircle: Int = 0,
    var resourceSquare: Int = 0,

    var bossLevel: Int = 1,
    var bossHp: Int = 1000,
)
