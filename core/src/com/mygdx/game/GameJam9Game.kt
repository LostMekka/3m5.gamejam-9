package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.Screen
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.SplashScreen
import ktx.app.KtxGame


/** ApplicationListener implementation. */
class MyGdxGame : KtxGame<Screen>() {
    override fun create() {
        addScreen(GameScreen())
        addScreen(SplashScreen(this))
        setScreen<SplashScreen>()
    }
}
