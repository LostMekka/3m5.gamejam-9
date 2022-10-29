package com.mygdx.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.graphics.use

class SplashScreen(private var game: KtxGame<Screen>) : KtxScreen {
    private val batch = SpriteBatch()
    private val img = Texture("blattgold_beaat_goldd.png")
    private var elapsed = 0f


    override fun render(delta: Float) {
        batch.use {
            batch.draw(img, (Gdx.graphics.width - img.width) / 2f, (Gdx.graphics.height - img.height) / 2f)
        }
        elapsed += delta
        if (elapsed > 2f) {
            this.hide()
            game.setScreen<GameScreen>()
        }
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}
