package com.mygdx.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.mygdx.game.assets.AssetDescriptors
import com.mygdx.game.common.soundController
import com.mygdx.game.screens.GameScreen
import com.mygdx.game.screens.SplashScreen
import com.mygdx.game.ui.initUi
import ktx.app.KtxGame


val assetManager by lazy { AssetManager() }

class MyGdxGame : KtxGame<Screen>() {
    override fun create() {
        loadAssets()
        initUi()

        addScreen(GameScreen())
        addScreen(SplashScreen(this))
        setScreen<GameScreen>()

        // just for testing, don't be scared!
        soundController.playTestSound()
    }

    private fun loadAssets() {
        for (descriptor in AssetDescriptors.ALL) {
            assetManager.load(descriptor)
        }
        assetManager.finishLoading()
    }
}
