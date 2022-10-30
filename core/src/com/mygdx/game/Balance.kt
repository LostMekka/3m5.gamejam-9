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
fun bossLootCircles(bossLevel: Int) = (0.1f * bossLevel + 1).roundToInt()
fun bossLootPentas(bossLevel: Int) = (0.2f * bossLevel).roundToInt()



////// FACTORY ////////////////////////////////
fun factorySpeedForLevel(level: Int): Float = 0.1f * level
val factoryRepairCostPerHpPoint = ResourcePackage(triangles = 1)

@Suppress("UNUSED_PARAMETER")
fun factoryUpgradeCost(minionType: MinionType, level: Int) =
    ResourcePackage(
        triangles = (8 * 1.2.pow(level - 1)).roundToInt(),
        circles = (2 * 1.3.pow(level - 8)).roundToInt(),
    )

fun strengthUpgradeCost(minionType: MinionType,level: Float) =
    ResourcePackage(
        triangles =0,
        squares = (1 * 1.5.pow(level.toDouble())).roundToInt(),
        circles = (5 * 1.5.pow(level.toDouble())).roundToInt(),
    )

fun roundtripUpgradeCost(level: Float) =
    ResourcePackage(
        triangles =(1 * 1.2.pow(1/level.toDouble())).roundToInt(),
        squares = (2 * 1.2.pow(1/level.toDouble())).roundToInt(),
        circles = (10 * 1.2.pow(1/level.toDouble())).roundToInt(),
    )
