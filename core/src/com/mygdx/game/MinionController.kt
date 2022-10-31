package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.common.soundController
import java.util.LinkedList
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random
import ktx.graphics.use
import ktx.math.minus

class MinionController(
    tankTexture: Texture,
    archerTexture: Texture,
    minerTexture: Texture,
    private val projectileTexture: Texture,
    private val triangleTexture: Texture,
) {
    private val projectileSpeed get() = 900f
    private val maxProjectiles get() = 50
    private val maxAttackCoolDown get() = 0.025f
    private var attackCoolDown = 0f

    private val gateX get() = 750f
    private val walkX1 get() = 960f
    private val walkX2 get() = 1440f
    private val walkYDiff get() = 30f
    private val bossPos = Vector2(1135f, 810f)

    private val walkSpeed get() = 120f
    private val walkStateSpeed get() = 3.5f
    private val walkBobHeight get() = 10f
    private val minionScale get() = 3f

    private val maxMinionCount get() = 200
    private val maxCorpseCount get() = 200
    private val maxMiningDebrisRate get() = 10f

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

    private val virtualMinionCountsByType = mutableMapOf(
        MinionType.Tank to 0,
        MinionType.Archer to 0,
        MinionType.Miner to 0,
    )
    private val minionsByType = mutableMapOf(
        MinionType.Tank to LinkedList<MinionSprite>(),
        MinionType.Archer to LinkedList<MinionSprite>(),
        MinionType.Miner to LinkedList<MinionSprite>(),
    )
    private val projectiles = LinkedList<Projectile>()
    private val corpses = LinkedList<Corpse>()
    private val miningDebris = LinkedList<MiningDebris>()

    private val debugRenderer = ShapeRenderer().apply { setAutoShapeType(true) }
    private val renderDebugShapes get() = false

    fun reset() {
        virtualMinionCountsByType.replaceAll { _, _ -> 0 }
        minionsByType.replaceAll { _, _ -> LinkedList() }
        projectiles.clear()
        corpses.clear()
        miningDebris.clear()
        miningDebrisCoolDown = 10f
    }

    fun update(delta: Float, gameState: PersistentGameState) {
        soundCoolDown -= delta

        for ((type, minions) in minionsByType) {
            val actualCount = ceil(gameState.resettableState[type].minionCountOutside).toInt()
            val currentDisplayedCount = minions.size
            val currentVirtualCount = virtualMinionCountsByType.getValue(type)
            val y = yByType.getValue(type)

            if (currentVirtualCount < actualCount) {
                // we have not enough minions on screen. add some!
                if (currentDisplayedCount >= maxMinionCount) {
                    // max minions reached.
                    // just repurpose some of the many existing minions. nobody will notice!
                    minions.random().apply {
                        pos = Vector2(gateX, y)
                        walkTarget = walkTarget(y, 0f)
                    }
                } else {
                    minions += MinionSprite(
                        texture = textureByType.getValue(type),
                        pos = Vector2(gateX, y),
                        walkTarget = walkTarget(y, 0f),
                    )
                }
            }
            if (currentVirtualCount > actualCount) {
                // we have too many minions here. cull some of them!
                // show no mercy! (except when there are at the corpse sprite limit of course)
                var currCount = currentVirtualCount
                var corpseCount = 0
                while (currCount > actualCount && minions.isNotEmpty()) {
                    val pos = if (currCount <= maxMinionCount) {
                        minions.removeAt(minions.indices.random()).pos
                    } else {
                        walkTarget(y)
                    }
                    currCount--
                    corpseCount++
                    if (corpseCount <= maxCorpseCount) corpses += Corpse(
                        texture = textureByType.getValue(type),
                        pos = pos,
                        velocity = Vector2(randomAroundZero(200f), randomAroundZero(100f)),
                        angularVelocity = randomAroundZero(180f),
                        lifetime = randomBetween(0.5f, 0.8f),
                    )
                }
            }
            virtualMinionCountsByType[type] = actualCount

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
            if (source != null) projectiles += Projectile(
                pos = source.pos.cpy(),
                target = Vector2(
                    bossPos.x + randomAroundZero(30f),
                    bossPos.y + randomAroundZero(30f),
                ),
            )
        }

        val projectileIterator = projectiles.listIterator()
        while (projectileIterator.hasNext()) {
            val p = projectileIterator.next()
            val arrived = p.pos.moveTo(p.target, projectileSpeed * delta)
            if (arrived) projectileIterator.remove()
        }

        val corpseIterator = corpses.listIterator()
        while (corpseIterator.hasNext()) {
            val c = corpseIterator.next()
            c.lifetime -= delta
            if (c.lifetime <= 0f) {
                corpseIterator.remove()
            } else {
                c.velocity.y -= 1000f * delta
                c.pos.x += c.velocity.x * delta
                c.pos.y += c.velocity.y * delta
                c.angle += c.angularVelocity * delta
            }
        }

        createMiningDebris(delta)
        updateMiningDebris(delta)
    }

    private var miningDebrisCoolDown = 10f
    private fun createMiningDebris(delta: Float) {
        val miners = minionsByType.getValue(MinionType.Miner)
        if (miners.isEmpty()) return
        val virtualCount = virtualMinionCountsByType.getValue(MinionType.Miner)
        miningDebrisCoolDown -= delta
        if (miningDebrisCoolDown <= 0f) {
            // TODO: get actual mining round trip time here!
            val rate = min(virtualCount / 10f, maxMiningDebrisRate)
            miningDebrisCoolDown += 1f / rate
            val x = miners.random().pos.x.coerceIn(walkX1, walkX2)
            val y = randomBetween(30f, 140f)
            repeat(Random.nextInt(3, 7)) {
                miningDebris += MiningDebris(
                    pos = Vector2(x, y),
                    velocity = Vector2(randomAroundZero(400f), randomBetween(-100f, 400f)),
                    scale = randomBetween(0.2f, 0.4f),
                    angularVelocity = randomAroundZero(1000f),
                    lifetime = randomBetween(0.5f, 1.2f),
                )
            }
        }
    }

    private fun updateMiningDebris(delta: Float) {
        val iter = miningDebris.listIterator()
        while (iter.hasNext()) {
            val d = iter.next()
            d.lifetime -= delta
            if (d.lifetime <= 0f) {
                iter.remove()
            } else {
                d.velocity.y -= 1000f * delta
                d.pos.x += d.velocity.x * delta
                d.pos.y += d.velocity.y * delta
                d.angle += d.angularVelocity * delta
            }
        }
    }

    private val soundTime = 0.17f
    private var soundCoolDown = 0f
    private fun playAttackSound() {
        if (soundCoolDown > 0) return
        soundCoolDown += soundTime * randomAroundOne(0.25f)
        soundController.playRandomArcherSound()
    }

    private fun walkTarget(y: Float, r: Float = Random.nextFloat()): Vector2 {
        return Vector2(
            walkX1 + r * (walkX2 - walkX1),
            y + randomAroundZero(walkYDiff),
        )
    }

    fun draw(batch: SpriteBatch) {
        for (minions in minionsByType.values) {
            for (m in minions) {
                batch.drawSimple(
                    texture = m.texture,
                    x = m.pos.x,
                    y = m.pos.y + walkBobHeight * sin(m.walkState * MathUtils.PI),
                    scale = minionScale,
                    angle = 0f,
                    flipX = m.pos.x > m.walkTarget.x,
                )
            }
        }
        for (p in projectiles) {
            batch.drawSimple(
                texture = projectileTexture,
                x = p.pos.x,
                y = p.pos.y,
                scale = 0.2f,
                angle = p.angle,
            )
        }
        for (c in corpses) {
            batch.color = Color.RED
            batch.drawSimple(
                texture = c.texture,
                x = c.pos.x,
                y = c.pos.y,
                scale = minionScale,
                angle = c.angle,
            )
            batch.color = Color.WHITE
        }
        for (d in miningDebris) {
            batch.drawSimple(
                texture = triangleTexture,
                x = d.pos.x,
                y = d.pos.y,
                scale = d.scale,
                angle = d.angle,
            )
        }
        if (renderDebugShapes) debugRenderer.use(ShapeRenderer.ShapeType.Line) {
            for ((_, y) in yByType) {
                it.rect(walkX1, y - walkYDiff, walkX2 - walkX1, 2 * walkYDiff)
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
    val walkStateSpeedModifier: Float = randomAroundOne(0.2f)
}

private class Projectile(
    val pos: Vector2,
    val target: Vector2,
) {
    val angle = (target - pos).angleDeg()
}

private class Corpse(
    val texture: Texture,
    val pos: Vector2,
    val velocity: Vector2,
    var angularVelocity: Float,
    var lifetime: Float,
) {
    var angle = 0f
}

private class MiningDebris(
    val pos: Vector2,
    val velocity: Vector2,
    val scale: Float,
    var angularVelocity: Float,
    var lifetime: Float,
) {
    var angle = 0f
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

private fun SpriteBatch.drawSimple(
    texture: Texture,
    x: Float,
    y: Float,
    scale: Float,
    angle: Float = 0f,
    flipX: Boolean = false,
) {
    draw(
        texture,
        x - texture.width / 2f,
        y - texture.height / 2f,
        texture.width / 2f,
        texture.height / 2f,
        texture.width.toFloat(),
        texture.height.toFloat(),
        scale,
        scale,
        angle,
        0,
        0,
        texture.width,
        texture.height,
        flipX,
        false,
    )
}

private fun randomAroundZero(max: Float) = (Random.nextFloat() - 0.5f) * 2f * max
private fun randomAroundOne(max: Float) = (Random.nextFloat() - 0.5f) * 2f * max + 1f
private fun randomBetween(min: Float, max: Float) = Random.nextFloat() * (max - min) + min
