package com.mygdx.game.common

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.mygdx.game.assets.AssetDescriptors

class Soundcontroller() {

    private var assetManager: AssetManager = TODO()

    private lateinit var testSound: Sound

    private val volume = 1f

    constructor(assetManager: AssetManager) : this() {
        this.assetManager = assetManager
        init()
    }

    private fun init() {
        testSound = assetManager.get(AssetDescriptors.TEST_SOUND)
    }

    // === play sound methods
    // they shall be called from the game world when something happens

    fun playTestSound() {
        testSound.play(volume)
    }

}