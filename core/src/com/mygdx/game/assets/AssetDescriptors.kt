package com.mygdx.game.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.assets.Asset
import ktx.collections.gdxArrayOf

object AssetDescriptors {

    // === FONTS
    val NUMBER_FONT: AssetDescriptor<BitmapFont> = AssetDescriptor(AssetPaths.fontPath, BitmapFont::class.java)
    val TEXT_FONT: AssetDescriptor<BitmapFont> = AssetDescriptor(AssetPaths.textFontPath, BitmapFont::class.java)

    // === SOUNDS
    val TEST_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.testSoundPath, Sound::class.java)

    val PUNCH_1_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch1SoundPath, Sound::class.java)
    val PUNCH_2_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch2SoundPath, Sound::class.java)
    val PUNCH_3_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch3SoundPath, Sound::class.java)
    val PUNCH_4_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch4SoundPath, Sound::class.java)
    val PUNCH_5_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.punch5SoundPath, Sound::class.java)

    val ARCHER_1_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.archer1SoundPath, Sound::class.java)
    val ARCHER_2_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.archer2SoundPath, Sound::class.java)
    val ARCHER_3_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.archer3SoundPath, Sound::class.java)
    val ARCHER_4_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.archer4SoundPath, Sound::class.java)
    val ARCHER_5_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.archer5SoundPath, Sound::class.java)
    val ARCHER_6_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.archer6SoundPath, Sound::class.java)

    val BOSS_1_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.boss1SoundPath, Sound::class.java)
    val BOSS_2_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.boss2SoundPath, Sound::class.java)
    val BOSS_3_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.boss3SoundPath, Sound::class.java)

    val BUTTON_COMMON_SOUND: AssetDescriptor<Sound> =
        AssetDescriptor(AssetPaths.buttonCommonSoundPath, Sound::class.java)
    val BUTTON_GG_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.buttonGGSoundPath, Sound::class.java)
    val BUTTON_REPAIR_SOUND: AssetDescriptor<Sound> =
        AssetDescriptor(AssetPaths.buttonRepairSoundPath, Sound::class.java)
    val BUTTON_UPGRADE_1_SOUND: AssetDescriptor<Sound> =
        AssetDescriptor(AssetPaths.buttonUpgrade1SoundPath, Sound::class.java)
    val BUTTON_UPGRADE_2_SOUND: AssetDescriptor<Sound> =
        AssetDescriptor(AssetPaths.buttonUpgrade2SoundPath, Sound::class.java)

    val REPAIR_SOUND: AssetDescriptor<Sound> = AssetDescriptor(AssetPaths.repairSoundPath, Sound::class.java)

    val GAME_MUSIC: AssetDescriptor<Music> = AssetDescriptor(AssetPaths.gameMusicPath, Music::class.java)

    // Click
    // repair
    // upgrade
    // collect ressources (3x)
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

    val FACTORY: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.factory, Texture::class.java)
    val MINION_ARCHER: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.minion_proto, Texture::class.java)
    val MINION_TANK: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.minion_tank, Texture::class.java)
    val MINION_WORKER: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.minion_worker, Texture::class.java)
    val BOSS: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.boss, Texture::class.java)
    val BOSS_ATTACK: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.boss_attack, Texture::class.java)
    val BOSS_HIT: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.boss_hit, Texture::class.java)
    val PROJECTILE: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.projectile, Texture::class.java)

    val BUTTON: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.button, Texture::class.java)
    val BUTTON_PRESSED: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.button_pressed, Texture::class.java)
    val FRAME: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.frame, Texture::class.java)
    val FACTORY_BACKGROUND: AssetDescriptor<Texture> = AssetDescriptor(AssetPaths.factory_bg, Texture::class.java)

    // Buttons
    // GG
    // OPEN
    // Holding cell
    // Ressourcenübersicht

    // == all descriptors ==
    val ALL = gdxArrayOf<AssetDescriptor<*>>(
        NUMBER_FONT,
        TEST_SOUND,
        REPAIR_SOUND,
        BACKGROUND,
        CIRCLE,
        PENTAGON,
        TRIANGLE,
        BUTTON,
        BUTTON_PRESSED,
        FRAME,
        PUNCH_1_SOUND,
        PUNCH_2_SOUND,
        PUNCH_3_SOUND,
        PUNCH_4_SOUND,
        PUNCH_5_SOUND,
        TEXT_FONT,
        FACTORY,
        MINION_ARCHER,
        MINION_TANK,
        MINION_WORKER,
        BOSS,
        FACTORY_BACKGROUND,
        PROJECTILE,
        BOSS_ATTACK,
        BOSS_HIT,
        GAME_MUSIC,
        ARCHER_1_SOUND,
        ARCHER_2_SOUND,
        ARCHER_3_SOUND,
        ARCHER_4_SOUND,
        ARCHER_5_SOUND,
        ARCHER_6_SOUND,
        BOSS_1_SOUND,
        BOSS_2_SOUND,
        BOSS_3_SOUND,
        BUTTON_COMMON_SOUND,
        BUTTON_GG_SOUND,
        BUTTON_REPAIR_SOUND,
        BUTTON_UPGRADE_1_SOUND,
        BUTTON_UPGRADE_2_SOUND,
    )

    fun setGamePlay() {
        TODO("implement if necessary")
    }
}
