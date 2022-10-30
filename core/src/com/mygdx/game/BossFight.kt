package com.mygdx.game

import com.mygdx.game.common.soundController
import kotlin.math.*

enum class FightMode {
    NEUTRAL,
    OFFENCE,
    DEFENCE,
}

class BossFight(private val state: ResettableGameState) {
    var fightMode = FightMode.NEUTRAL

    private val fightModeAttackMultiplier
        get() = when (fightMode) {
            FightMode.NEUTRAL -> 1f
            FightMode.DEFENCE -> 0.5f
            FightMode.OFFENCE -> 2f
        }

    private val fightModeDefenseMultiplier
        get() = when (fightMode) {
            FightMode.NEUTRAL -> 1f
            FightMode.DEFENCE -> 0.5f
            FightMode.OFFENCE -> 2f
        }

    fun minionAttack(): Boolean {
        soundController.playRandomHitSound()
        val archerDamage = state.archerMinionData.minionCountOutside * state.archerMinionData.attackStrength
        val tankDamage = state.tankMinionData.minionCountOutside * state.tankMinionData.attackStrength
        val totalDamage = archerDamage + tankDamage
        if (totalDamage > 0) {
            val bossIsDead = state.bossHp.damage((totalDamage * fightModeAttackMultiplier).roundToInt())
            if (bossIsDead) {
                giveLoot()
                respawnBoss()
                return true
            }
        }
        return false
    }

    fun respawnBoss() {
        state.bossLevel++
        state.bossHp = Hp(bossHealth(state.bossLevel))
        state.boss = state.bosses
            .filter { it.level == state.bossLevel }
            .randomOrNull()
            ?: state.bosses.random()
    }

    private fun giveLoot() {
        state.resourceInventory += ResourcePackage(
            circles = bossLootCircles(state.bossLevel),
            pentas = bossLootPentas(state.bossLevel),
        )
    }

    private fun bossAttack() {
        soundController.playRandomHitSound()
        val newAttack = state.boss.nextAttack()
        var damage: Float = (bossBaseDamage(state.bossLevel) * newAttack.damage)

        for (minionType in MinionType.values()) {
            val factor = when (minionType) {
                MinionType.Miner -> 1f / state[minionType].defence
                else -> fightModeDefenseMultiplier / state[minionType].defence
            }
            val damageDealt = min(damage * factor, state[minionType].minionCountOutside)
            state[minionType].minionCountOutside -= damageDealt
            damage -= damageDealt / factor
            if (damage <= 0f) return
        }

        factoryNotAttackedSince = 0f
        var door =1f
        if (!state.doorIsOpen)door=door/1.5f
        if (state.factoryHp.damage(max(1,(damage*door).toInt()))) lost()
    }

    fun lost() {
        // TODO
    }

    var factoryNotAttackedSince = 0f
    var fightRoundLength = 1f
    private var lastFightUpdate = 0f
    fun update(delta: Float) {
        factoryNotAttackedSince += delta
        lastFightUpdate += delta
        if (lastFightUpdate < fightRoundLength) return
        lastFightUpdate = 0f

        if (factoryNotAttackedSince > 10f) state.factoryHp.heal(5)
        if (!minionAttack()) bossAttack()

        val mincount=state.minerMinionData.minionCountInside;
        if (mincount>0&&!state.factoryHp.isFull){
            state.factoryHp.heal((mincount/50*state.minerMinionData.attackMultiplier).roundToInt())
            if (state.tankMinionData.minionCountOutside<=0) state.minerMinionData.minionCountInside-=1
        }
    }
}

class Attack(val damage: Float, var picture: String?)

class Boss(val level: Int, val image: String, val name: String, val attacks: List<Attack>) {
    var currentAttackIndex = 0

    fun nextAttack(): Attack {
        val attack = attacks.getOrNull(currentAttackIndex) ?: Attack(1f, null)
        currentAttackIndex = (currentAttackIndex + 1) % attacks.size
        if (attack.picture == null) attack.picture = image
        return attack
    }
}

