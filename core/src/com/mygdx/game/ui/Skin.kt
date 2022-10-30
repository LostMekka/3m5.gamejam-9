package com.mygdx.game.ui

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.kotcrab.vis.ui.VisUI
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors
import ktx.style.button
import ktx.style.label
import ktx.style.visImageButton
import ktx.style.visTooltip

fun loadSkin(): Skin {
    val skin = VisUI.getSkin()
    val numberFont = assetManager.get(AssetDescriptors.NUMBER_FONT)
    val textFont = assetManager.get(AssetDescriptors.TEXT_FONT)

    return skin.apply {
        label { font = textFont }
        label("number") { font = numberFont }

        button {
            up = TextureRegionDrawable(assetManager.get(AssetDescriptors.BUTTON))
        }

        visImageButton {
            up = TextureRegionDrawable(assetManager.get(AssetDescriptors.BUTTON))
        }

        visTooltip {
            background = TextureRegionDrawable(assetManager.get(AssetDescriptors.FRAME))
        }
    }
}
