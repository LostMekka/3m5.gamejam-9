package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.common.soundController
import java.util.LinkedList
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random
import ktx.math.minus

class MinionController(
    tankTexture: Texture,
    archerTexture: Texture,
    minerTexture: Texture,
    private val projectileTexture: Texture,
) {
    private val projectileSpeed = 900f
    private val maxProjectiles = 50
    private val maxAttackCoolDown = 0.025f
    private var attackCoolDown = 0f

    private val gateX = Gdx.graphics.width * 0.5f
    private val walkX1 = Gdx.graphics.width * 0.6f
    private val walkX2 = Gdx.graphics.width * 0.9f
    private val bossPos = Vector2(1135f, 810f)

    private val walkSpeed = 120f
    private val walkStateSpeed = 3.5f
    private val walkBobHeight = 10f

    private val maxMinionCount = 200

    private val yByType = mapOf(
        MinionType.Tank to Gdx.graphics.height * 0.65f,
        MinionType.Archer to Gdx.graphics.height * 0.45f,
        MinionType.Miner to Gdx.graphics.height * 0.20f,
    )
    private val textureByType = mapOf(
        MinionType.Tank to tankTexture,
        MinionType.Archer to archerTexture,
        MinionType.Miner to minerTexture,
    )

    private val minionsByType = MinionType.values().associateWith { LinkedList<MinionSprite>() }
    private val projectiles = LinkedList<Projectile>()

    fun update(delta: Float, gameState: PersistentGameState) {
        soundCoolDown -= delta

        for ((type, minions) in minionsByType) {
            val newCount = min(ceil(gameState.resettableState[type].minionCountOutside).toInt(), maxMinionCount)
            val currentCount = minions.size
            val y = yByType.getValue(type)
            if (currentCount < newCount) {
                minions += MinionSprite(
                    texture = textureByType.getValue(type),
                    pos = Vector2(gateX, y),
                    walkTarget = walkTarget(y, 0f),
                )
            }
            if (currentCount > newCount && newCount < maxMinionCount && minions.isNotEmpty()) {
                minions.removeAt(minions.indices.random())
            }
            for (m in minions) {
                m.walkState = (m.walkState + delta * walkStateSpeed * m.walkStateSpeedModifier) % 1f
                val targetReached = m.pos.moveTo(m.walkTarget, delta * walkSpeed)
                if (targetReached) m.walkTarget = walkTarget(y)
            }
        }

        attackCoolDown = max(0f, attackCoolDown - delta)
        val attacks = gameState.resettableState.bossFightState.minionAttacksLastFrame
        if (attacks > 0 && attackCoolDown <= 0f && projectiles.size < maxProjectiles) {
            attackCoolDown = maxAttackCoolDown
            playAttackSound()
            val source = minionsByType.getValue(MinionType.Archer).randomOrNull()
            if (source != null) projectiles += Projectile(source.pos.cpy(), bossPos)
        }

        val iter = projectiles.listIterator()
        while (iter.hasNext()) {
            val p = iter.next()
            val arrived = p.pos.moveTo(p.target, projectileSpeed * delta)
            if (arrived) iter.remove()
        }
    }

    private val soundTime = 0.17f
    private var soundCoolDown = 0f
    private fun playAttackSound() {
        if (soundCoolDown > 0) return
        soundCoolDown += soundTime * (Random.nextFloat() * 0.5f + 0.75f)
        soundController.playRandomArcherSound()
    }

    private fun walkTarget(y: Float, r: Float = Random.nextFloat()) =
        Vector2(
            walkX1 + r * (walkX2 - walkX1),
            y + (Random.nextFloat() - 0.5f) * 20f,
        )

    fun draw(batch: SpriteBatch) {
        for (minions in minionsByType.values) {
            for (m in minions) {
                val x = m.pos.x
                val y = m.pos.y + walkBobHeight * sin(m.walkState * MathUtils.PI)
                batch.draw(
                    m.texture,
                    x - m.texture.width / 2f,
                    y - m.texture.height / 2f,
                    m.texture.width / 2f,
                    m.texture.height / 2f,
                    m.texture.width.toFloat(),
                    m.texture.height.toFloat(),
                    3f,
                    3f,
                    0f,
                    0,
                    0,
                    m.texture.width,
                    m.texture.height,
                    m.pos.x > m.walkTarget.x,
                    false,
                )
            }
        }
        for (p in projectiles) {
            batch.draw(
                projectileTexture,
                p.pos.x - projectileTexture.width / 2f,
                p.pos.y - projectileTexture.height / 2f,
                projectileTexture.width / 2f,
                projectileTexture.height / 2f,
                projectileTexture.width.toFloat(),
                projectileTexture.height.toFloat(),
                0.2f,
                0.2f,
                p.angle,
                0,
                0,
                projectileTexture.width,
                projectileTexture.height,
                false,
                false,
            )
        }
    }
}

class MinionSprite(
    val texture: Texture,
    var pos: Vector2,
    var walkTarget: Vector2,
    var walkState: Float = Random.nextFloat(),
) {
    val walkStateSpeedModifier: Float = (Random.nextFloat() - 0.5f) / 3f + 1f
}

class Projectile(
    val pos: Vector2,
    val target: Vector2,
) {
    val angle = (target - pos).angleDeg()
}

private fun Vector2.moveTo(target: Vector2, distance: Float): Boolean {
    val d = dst(target)
    return if (distance >= d) {
        set(target.x, target.y)
        true
    } else {
        val l = lerp(target, distance / d)
        set(l.x, l.y)
        false
    }
}
