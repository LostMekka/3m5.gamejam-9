package com.mygdx.game

import kotlin.math.pow
import kotlin.math.roundToInt

////// MINIONS ////////////////////////////////
const val tankBaseAttack = 0f
const val tankBaseDefense = 10f

const val archerBaseAttack = 1f
const val archerBaseDefense = 2f

const val minerBaseDefense = 1f
const val baseMinerRoundTripTime = 10f

////// BOSS ////////////////////////////////
fun bossBaseDamage(bossLevel: Int): Float = 0.5f * 1.15f.pow(bossLevel - 1)
fun bossHealth(bossLevel: Int): Int = 20 * 1.15f.pow(bossLevel - 1).toInt()



////// FACTORY ////////////////////////////////
fun factorySpeedForLevel(level: Int): Float = 0.1f * level
val factoryRepairCostPerHpPoint = ResourcePackage(triangles = 1)

fun factoryUpgradeCost(minionType: MinionType, level: Int) =
    ResourcePackage(
        triangles = (10 * 1.2.pow(level)).roundToInt(),
        circles = 0,
        squares = 0,
    )
