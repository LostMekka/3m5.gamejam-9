package com.mygdx.game

import com.mygdx.game.common.soundController
import kotlin.math.pow
import kotlin.math.roundToInt

class GameState(
    var factoryHp: Hp = Hp(total = 1000),
    var doorIsOpen: Boolean = true,

    var tankMinionData: MinionProduction = MinionProduction(MinionType.Tank,2,4),
    var archerMinionData: MinionProduction = MinionProduction(MinionType.Archer,4,2),
    var minerMinionData: MinionProduction = MinionProduction(MinionType.Miner,0,1),

    var resourceInventory: ResourcePackage = ResourcePackage(triangles = 100),

    var bossLevel: Int = 1,
    var bossHp: Hp = Hp(total = 10),
) {

    var lastFightUpdate=0f
    var lastMiningUpdate=0f
    var fight:Boss_Fight= Boss_Fight(this)
    val fight_round_length=1f
    val mining_round_length=0.5f
    val rate=0.5f

    var basicAttack:Attack= Attack(1f,null)
    var bosses= mutableListOf<Boss>()

    init {
        bosses.add(Boss(1,"Hier könnte ihre Werbung stehen","Bööööses Monster", listOf(basicAttack)))

    }
    var boss:Boss=bosses.get(0)


    private val factoryUpgradeCostCache = mutableMapOf<MinionType, ResourcePackage>()

    operator fun get(minionType: MinionType) = when (minionType) {
        MinionType.Tank -> tankMinionData
        MinionType.Archer -> archerMinionData
        MinionType.Miner -> minerMinionData
    }


    fun calculateFrame(delta: Float) {
        calculateFactoryFrame(delta)
        calculateMiningFrame(delta)
        calculateCombatFrame(delta)
    }

    private fun calculateFactoryFrame(delta: Float) {
        for (minionType in MinionType.values()) {
            this[minionType].apply {
                factoryProgress += factorySpeedForLevel(factoryLevel) * delta
                val fullProgress = factoryProgress.toInt()
                factoryProgress -= fullProgress
                minionCountInside += fullProgress
                if (doorIsOpen) {
                    minionCountOutside += minionCountInside
                    minionCountInside = 0f
                }
            }
        }
    }

    private fun calculateCombatFrame(delta: Float) {
        lastFightUpdate+=delta;
        if (lastFightUpdate>=fight_round_length){
            lastFightUpdate=0f
            fight.round()

        }
    }

    private fun calculateMiningFrame(delta: Float) {
        lastMiningUpdate+=delta;
        if (lastMiningUpdate>=mining_round_length&&doorIsOpen){
            lastMiningUpdate=0f
            if (minerMinionData.minionCountOutside>0)
            this.resourceInventory.triangles+=(rate*minerMinionData.minionCountOutside).toInt()

        }
    }

    fun onGGClicked() {
        // TODO
    }

    fun onRepairClicked() {
        // TODO
        soundController.playRepairSound()
    }

    fun canUpgradeFactory(minionType: MinionType): Boolean {
        return getUpgradeCost(minionType) in resourceInventory
    }

    fun onUpgradeFactoryClicked(minionType: MinionType) {
        if (!canUpgradeFactory(minionType)) return
        resourceInventory -= getUpgradeCost(minionType)
        this[minionType].factoryLevel++
    }

    fun onToggleDoorClicked() {
        doorIsOpen = !doorIsOpen
    }

    fun getUpgradeCost(minionType: MinionType): ResourcePackage {
        return factoryUpgradeCostCache.getOrPut(minionType) {
            val level = this[minionType].factoryLevel
            factoryUpgradeCost(minionType, level)
        }
    }

    fun getRepairCost(): ResourcePackage {
        return ResourcePackage(
            triangles = 5 * factoryHp.missing,
            circles = 0,
            squares = 0,
        )
    }
}

fun factorySpeedForLevel(level: Int): Float {
    return 0.1f * level
}

fun factoryUpgradeCost(minionType: MinionType, level: Int): ResourcePackage {
    return ResourcePackage(
        triangles = (10 * 1.3.pow(level)).roundToInt(),
        circles = 0,
        squares = 0,
    )
}
