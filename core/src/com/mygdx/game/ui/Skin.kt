package com.mygdx.game.ui

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.kotcrab.vis.ui.VisUI
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors
import ktx.style.*

fun loadSkin(): Skin {
    val skin = VisUI.getSkin()
    val numberFont = assetManager.get(AssetDescriptors.NUMBER_FONT)
    val textFont = assetManager.get(AssetDescriptors.TEXT_FONT)
    val buttonPatch = NinePatchDrawable(
        NinePatch(assetManager.get(AssetDescriptors.BUTTON), 6, 6, 6, 6)
    )
    val buttonPressedPatch = NinePatchDrawable(
        NinePatch(assetManager.get(AssetDescriptors.BUTTON_PRESSED), 6, 6, 6, 6)
    )

    return skin.apply {
        label { font = textFont }
        label("number") { font = numberFont }

        button {
            up = buttonPatch
            down = buttonPressedPatch
        }

        button("flipped", extend = defaultStyle) {
            up = buttonPressedPatch
            down = buttonPatch
        }

        visImageButton {
            up = buttonPatch
            down = buttonPressedPatch
        }

        visImageButton("flipped", extend = defaultStyle) {
            up = buttonPressedPatch
            down = buttonPatch
        }

        visTooltip {
            background = TextureRegionDrawable(assetManager.get(AssetDescriptors.FRAME))
        }
    }
}
