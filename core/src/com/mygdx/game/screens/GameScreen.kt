package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.BossController
import com.mygdx.game.MinionController
import com.mygdx.game.PersistentGameState
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors
import com.mygdx.game.common.soundController
import com.mygdx.game.ui.GameUi
import ktx.app.KtxScreen
import ktx.graphics.use

class GameScreen : KtxScreen {
    private val font = BitmapFont()
    private val batch = SpriteBatch().apply { color = Color.WHITE }

    private val background = assetManager.get(AssetDescriptors.BACKGROUND)
    private val triangleTexture = assetManager.get(AssetDescriptors.TRIANGLE)
    private val tankTexture = assetManager.get(AssetDescriptors.MINION_TANK)
    private val archerTexture = assetManager.get(AssetDescriptors.MINION_ARCHER)
    private val minerTexture = assetManager.get(AssetDescriptors.MINION_WORKER)
    private val projectileTexture = assetManager.get(AssetDescriptors.PROJECTILE)

    private var gameState = PersistentGameState()
    private val ui = GameUi(gameState, this)
    private val minionController = MinionController(
        tankTexture = tankTexture,
        archerTexture = archerTexture,
        minerTexture = minerTexture,
        projectileTexture = projectileTexture,
        triangleTexture = triangleTexture,
    )
    private val bossController = BossController(gameState.resettableState.boss.image)

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit()
        val modifiedDelta = delta * 1f

        soundController.playGameMusic()

        ui.stage.act()
        gameState.calculateFrame(modifiedDelta)
        ui.update()
        minionController.update(modifiedDelta, gameState)
        bossController.updateBoss(gameState.resettableState.boss.image)


        batch.use {
            it.draw(background, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.width.toFloat())
            repeat(5) { i ->
                it.draw(
                    triangleTexture,
                    Gdx.graphics.width * (0.6f + 0.07f * i),
                    35f,
                    triangleTexture.width / 2f,
                    triangleTexture.height / 2f,
                    triangleTexture.width.toFloat(),
                    triangleTexture.height.toFloat(),
                    1f,
                    1f,
                    0f,
                    0,
                    0,
                    triangleTexture.width,
                    triangleTexture.height,
                    i % 2 == 0,
                    false,
                )
                if (i != 0) it.draw(
                    triangleTexture,
                    Gdx.graphics.width * (0.6f + 0.07f * (i - 0.5f)),
                    70f,
                    triangleTexture.width / 2f,
                    triangleTexture.height / 2f,
                    triangleTexture.width.toFloat(),
                    triangleTexture.height.toFloat(),
                    1f,
                    1f,
                    0f,
                    0,
                    0,
                    triangleTexture.width,
                    triangleTexture.height,
                    i % 2 == 0,
                    false,
                )
            }
            minionController.draw(it)
            bossController.display(it, delta, gameState.resettableState.currentEffect)
        }

        ui.stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        ui.stage.viewport.update(width, height)
    }

    override fun dispose() {
        // Will be automatically disposed of by the game instance.
        font.dispose()
        batch.dispose()
    }

    fun onGGPressed() {
        minionController.reset()
    }
}
