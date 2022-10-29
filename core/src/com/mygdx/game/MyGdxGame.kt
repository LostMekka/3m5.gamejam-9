package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.graphics.use

class ExampleScreen : KtxScreen {
    // Notice no `lateinit var` - ExampleScreen has no create()
    // method and is constructed after libGDX is fully initiated
    // in ExampleGame.create method.
    val font = BitmapFont()
    val batch = SpriteBatch().apply {
        color = Color.WHITE
    }
    var gameState = GameState()

    override fun render(delta: Float) {
        calculateFactory()
        calculateCombat()
        batch.use {
            font.draw(it, "Hello Kotlin!", 100f, 100f)
        }
    }

    private fun calculateFactory() {
        // TODO
    }

    private fun calculateCombat() {
        // TODO
    }

    override fun dispose() {
        // Will be automatically disposed of by the game instance.
        font.dispose()
        batch.dispose()
    }
}

/** ApplicationListener implementation. */
class MyGdxGame : KtxGame<Screen>() {
    override fun create() {
        // Registering ExampleScreen in the game object: it will be
        // accessible through ExampleScreen class:
        addScreen(ExampleScreen())
        // Changing current screen to the registered instance of the
        // ExampleScreen class:
        setScreen<ExampleScreen>()
    }
}

// TODO: delete me!
class MyGdxGameOld : ApplicationAdapter() {
    var batch: SpriteBatch? = null
    var img: Texture? = null
    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        batch!!.begin()
        batch!!.draw(img, 0f, 0f)
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
        img!!.dispose()
    }
}
