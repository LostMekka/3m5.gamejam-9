package com.mygdx.game.common

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors

val soundController by lazy { SoundController() }

class SoundController {
    private val testSound: Sound = assetManager.get(AssetDescriptors.TEST_SOUND)
    private val repairSound: Sound = assetManager.get(AssetDescriptors.REPAIR_SOUND)

    private val volume = 1f

    // === play sound methods
    // they shall be called from the game world when something happens

    fun playTestSound() {
        testSound.play(volume)
    }

    fun playRepairSound() {
        repairSound.play(volume)
    }
}
