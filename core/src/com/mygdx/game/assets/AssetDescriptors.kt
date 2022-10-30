package com.mygdx.game.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.Asset
import ktx.collections.gdxArrayOf
import org.w3c.dom.Text

object AssetDescriptors {

    // === FONTS
    val FONT: AssetDescriptor<BitmapFont> = AssetDescriptor(AssetPaths.fontPath, BitmapFont::class.java)
    // HP Fonts
    // counters

    // === SOUNDS
    val TEST_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.testSoundPath, Sound::class.java)

    val PUNCH_1_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch1SoundPath, Sound::class.java)
    val PUNCH_2_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch2SoundPath, Sound::class.java)
    val PUNCH_3_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch3SoundPath, Sound::class.java)
    val PUNCH_4_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch4SoundPath, Sound::class.java)
    val PUNCH_5_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch5SoundPath, Sound::class.java)

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

    //lateinit var GAME_PLAY: AssetDescriptor<TextureAtlas>

    // === TEXTURES
    val BACKGROUND: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.background, Texture::class.java)

    val CIRCLE: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.circle, Texture::class.java)
    val PENTAGON: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.pentagon, Texture::class.java)
    val TRIANGLE: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.triangle, Texture::class.java)

    val BUTTON: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.button, Texture::class.java)
    val FRAME: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.frame, Texture::class.java)

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
        BACKGROUND,
        CIRCLE,
        PENTAGON,
        TRIANGLE,
        BUTTON,
        FRAME,
        PUNCH_1_SOUND,
        PUNCH_2_SOUND,
        PUNCH_3_SOUND,
        PUNCH_4_SOUND,
        PUNCH_5_SOUND,
    )

    fun setGamePlay() {
        TODO("implement if necessary")
    }
}
