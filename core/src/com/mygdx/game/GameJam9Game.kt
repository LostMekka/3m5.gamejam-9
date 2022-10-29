package com.mygdx.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.assets.AssetDescriptors
import com.mygdx.game.common.Soundcontroller
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.SplashScreen
import ktx.app.KtxGame


/** ApplicationListener implementation. */
class MyGdxGame : KtxGame<Screen>() {

    lateinit var assetManager : AssetManager

    override fun create() {
        loadAssets()

        addScreen(GameScreen())
        addScreen(SplashScreen(this))
        setScreen<GameScreen>()

        // just for testing, don't be scared!
        Soundcontroller(assetManager).playTestSound()

    }

    private fun loadAssets() {
        assetManager = AssetManager()
        for (descriptor in AssetDescriptors.ALL) {
            assetManager.load(descriptor)
        }
        assetManager.finishLoading()
    }
}
