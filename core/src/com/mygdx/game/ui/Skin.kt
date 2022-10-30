package com.mygdx.game.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.kotcrab.vis.ui.VisUI
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors
import ktx.style.label

fun loadSkin(): Skin {
    val skin = VisUI.getSkin()

    return skin.apply {
        label { font = assetManager.get(AssetDescriptors.NUMBER_FONT) }
    }
}
