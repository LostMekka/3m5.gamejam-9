package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.PersistentGameState
import com.mygdx.game.ui.GameUi
import ktx.app.KtxScreen
import ktx.graphics.use

class GameScreen : KtxScreen {
    // Notice no `lateinit var` - ExampleScreen has no create()
    // method and is constructed after libGDX is fully initiated
    // in ExampleGame.create method.
    val font = BitmapFont()
    val batch = SpriteBatch().apply {
        color = Color.WHITE
    }

    var gameState = PersistentGameState()
    private val ui = GameUi(gameState)


    override fun render(delta: Float) {
        ui.stage.act()
        gameState.calculateFrame(delta)

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit()

        ui.update()

        batch.use {

            it.color = Color.WHITE
            ui.stage.draw()
        }
    }

    override fun resize(width: Int, height: Int) {
        ui.stage.viewport.update(width, height)
    }

    override fun dispose() {
        // Will be automatically disposed of by the game instance.
        font.dispose()
        batch.dispose()
    }
}
