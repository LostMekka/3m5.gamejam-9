package com.mygdx.game.common

import com.badlogic.gdx.audio.Music
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

    private val archer1Sound: Sound = assetManager.get(AssetDescriptors.ARCHER_1_SOUND)
    private val archer2Sound: Sound = assetManager.get(AssetDescriptors.ARCHER_2_SOUND)
    private val archer3Sound: Sound = assetManager.get(AssetDescriptors.ARCHER_3_SOUND)
    private val archer4Sound: Sound = assetManager.get(AssetDescriptors.ARCHER_4_SOUND)
    private val archer5Sound: Sound = assetManager.get(AssetDescriptors.ARCHER_5_SOUND)
    private val archer6Sound: Sound = assetManager.get(AssetDescriptors.ARCHER_6_SOUND)

    private val boss1Sound: Sound = assetManager.get(AssetDescriptors.BOSS_1_SOUND)
    private val boss2Sound: Sound = assetManager.get(AssetDescriptors.BOSS_2_SOUND)
    private val boss3Sound: Sound = assetManager.get(AssetDescriptors.BOSS_3_SOUND)

    private val buttonGGSound: Sound = assetManager.get(AssetDescriptors.BUTTON_GG_SOUND)
    private val buttonCommonSound: Sound = assetManager.get(AssetDescriptors.BUTTON_COMMON_SOUND)
    private val buttonRepairSound: Sound = assetManager.get(AssetDescriptors.BUTTON_REPAIR_SOUND)
    private val buttonUpgrade1Sound: Sound = assetManager.get(AssetDescriptors.BUTTON_UPGRADE_1_SOUND)
    private val buttonUpgrade2Sound: Sound = assetManager.get(AssetDescriptors.BUTTON_UPGRADE_2_SOUND)

    private val gameMusic: Music = assetManager.get(AssetDescriptors.GAME_MUSIC)

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
        ).random().play(volume * 0.66f)
    }

    fun playRandomArcherSound() {
        listOf(
            archer1Sound,
            archer2Sound,
            archer3Sound,
            archer4Sound,
            archer5Sound,
            archer6Sound,
        ).random().play(volume * 0.50f)
    }

    fun playAnyRandomSound() {
        listOf(
            testSound,
            repairSound,
            punch1Sound,
            punch2Sound,
            punch3Sound,
            punch4Sound,
            punch5Sound,
        ).random().play()
    }

    fun playGameMusic() {
        gameMusic.volume = volume * 0.66f
        gameMusic.isLooping = true
        gameMusic.play()
    }

    fun playGGButtonSound() {
        buttonGGSound.play()
    }

    fun playCommonButtonSound() {
        buttonCommonSound.play()
    }

    fun playRepairButtonSound() {
        buttonRepairSound.play()
    }

    fun playRandomBossSound() {
        listOf(
            boss1Sound,
            boss2Sound,
            boss3Sound,
        ).random().play()
    }

    fun playUpgradeButtonSound() {
        listOf(
            buttonUpgrade1Sound,
            buttonUpgrade2Sound,
        ).random().play()
    }
}
