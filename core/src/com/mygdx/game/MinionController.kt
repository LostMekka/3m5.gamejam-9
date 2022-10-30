package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import java.util.LinkedList
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

class MinionController(
    tankTexture: Texture,
    archerTexture: Texture,
    minerTexture: Texture,
) {
    private val gateX = Gdx.graphics.width * 0.5f
    private val walkX1 = Gdx.graphics.width * 0.6f
    private val walkX2 = Gdx.graphics.width * 0.9f

    private val walkSpeed = 120f
    private val walkStateSpeed = 3.5f
    private val walkBobHeight = 10f

    private val maxMinionCount = 200

    private val yByType = mapOf(
        MinionType.Tank to Gdx.graphics.height * 2f / 3f,
        MinionType.Archer to Gdx.graphics.height * 2.5f / 6f,
        MinionType.Miner to Gdx.graphics.height * 1f / 6f,
    )
    private val textureByType = mapOf(
        MinionType.Tank to tankTexture,
        MinionType.Archer to archerTexture,
        MinionType.Miner to minerTexture,
    )

    private val minionsByType = MinionType.values().associateWith { LinkedList<MinionSprite>() }

    fun update(delta: Float, gameState: PersistentGameState) {
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
                val distance = m.pos.dst(m.walkTarget)
                val walkThisFrame = delta * walkSpeed
                if (walkThisFrame >= distance) {
                    m.pos = m.walkTarget
                    m.walkTarget = walkTarget(y)
                } else {
                    m.pos = m.pos.lerp(m.walkTarget, walkThisFrame / distance)
                }
            }
        }
    }

    private fun walkTarget(y: Float, r: Float = Random.nextFloat()) =
        Vector2(
            walkX1 + r * (walkX2 - walkX1),
            y + (Random.nextFloat() - 0.5f) * 20f,
        )

    fun draw(batch: SpriteBatch) {
        for (minions in minionsByType.values) {
            for (m in minions) {
                batch.draw(
                    m.texture,
                    m.pos.x,
                    m.pos.y + walkBobHeight * sin(m.walkState * MathUtils.PI),
                    m.texture.width * 3f,
                    m.texture.height * 3f,
                )
            }
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
