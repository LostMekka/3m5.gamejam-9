package com.mygdx.game.common

import com.badlogic.gdx.audio.Sound
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors

val soundController by lazy { SoundController() }

class SoundController {
    private val testSound: Sound = assetManager.get(AssetDescriptors.TEST_SOUND)
    private val repairSound: Sound = assetManager.get(AssetDescriptors.REPAIR_SOUND)

    private val punch1Sound: Sound = assetManager.get(AssetDescriptors.PUNCH_1_SOUND)
    private val punch2Sound: Sound = assetManager.get(AssetDescriptors.PUNCH_2_SOUND)
    private val punch3Sound: Sound = assetManager.get(AssetDescriptors.PUNCH_3_SOUND)
    private val punch4Sound: Sound = assetManager.get(AssetDescriptors.PUNCH_4_SOUND)
    private val punch5Sound: Sound = assetManager.get(AssetDescriptors.PUNCH_5_SOUND)

    private val volume = 1f

    // === play sound methods
    // they shall be called from the game world when something happens

    fun playTestSound() {
        testSound.play(volume)
    }

    fun playRepairSound() {
        repairSound.play(volume)
    }

    fun playRandomHitSound() {
        listOf(
            punch1Sound,
            punch2Sound,
            punch3Sound,
            punch4Sound,
            punch5Sound,
        ).random().play()
    }
}
