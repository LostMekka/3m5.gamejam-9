package com.mygdx.game

import kotlin.math.*

enum class FightMode {
    NEUTRAL,
    OFFENCE,
    DEFENCE
}

class BossFight(private val state: ResettableGameState) {
    var fightMode = FightMode.NEUTRAL
    var baseDamage = 4

    private val fightModeAttackMultiplier get() =
        when (fightMode) {
            FightMode.NEUTRAL -> 1f
            FightMode.DEFENCE -> 0.5f
            FightMode.OFFENCE -> 2f
        }

    private val fightModeDefenseMultiplier get() =
        when (fightMode) {
            FightMode.NEUTRAL -> 1f
            FightMode.DEFENCE -> 0.5f
            FightMode.OFFENCE -> 2f
        }

    fun minionAttack(): Boolean {
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
        state.bossHp = Hp(10 * state.bossLevel)
        state.boss = state.bosses
            .filter { it.level == state.bossLevel }
            .randomOrNull()
            ?: state.bosses.random()
    }

    val nextBossLoot get() =
        ResourcePackage(
            circles = state.bossLevel,
            squares = state.bossLevel / 10,
        )

    private fun giveLoot() {
        state.resourceInventory += nextBossLoot
    }

    private fun baseBossDamage(bossLevel: Int) = bossLevel * baseDamage

    private fun bossAttack() {
        val newAttack = state.boss.nextAttack()
        var damage: Float = (baseBossDamage(state.boss.level) * newAttack.damage)

        if (state.archerMinionData.minionCountOutside>0)
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
        if (state.factoryHp.damage(damage.toInt())) lost()
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
