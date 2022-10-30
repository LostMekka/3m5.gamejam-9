package com.mygdx.game.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.collections.gdxArrayOf

object AssetDescriptors {

    // === FONTS
    val FONT: AssetDescriptor<BitmapFont> = AssetDescriptor(AssetPaths.fontPath, BitmapFont::class.java)
    // HP Fonts
    // counters

    // === SOUNDS
    val TEST_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.testSoundPath, Sound::class.java)

    // Click
    // repair
    // upgrade
    // collect ressources (3x)
    val REPAIR_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.repairSoundPath, Sound::class.java)
    // minion sounds (3x)
    // boss sounds
    // hits
    // factory sounds (?)
    // game music (?)

    // === TEXTURES
    // Buttons
        // GG
        // OPEN
    // Ressources (3x)
    // Minions (3x)
    // Boss (3x)
    // Factory (3x)
    // Holding cell
    // Ressourcenübersicht

    // == all descriptors ==
    val ALL = gdxArrayOf<AssetDescriptor<*>>(
        FONT,
        TEST_SOUND,
        REPAIR_SOUND,
    )

    fun setGamePlay() {
        TODO("implement if necessary")
    }
}
