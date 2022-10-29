package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import ktx.scene2d.*
import ktx.actors.*
import ktx.scene2d.vis.*

fun initUi() {
    VisUI.load()
    Scene2DSkin.defaultSkin = loadSkin()
}

fun createRepairButton(stage: Stage) {
    stage.actors {
        flowGroup(vertical = false) {
            y = stage.height - 70f
            x = 20f

            visTextButton("Repair")
        }
    }
}

fun createResourceInfo(stage: Stage) {
    stage.actors {
        flowGroup(vertical = false) {
            y = stage.height - 20f
            x = 20f

            visLabel("12340")
        }
    }
}

class GameUi {
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    val stage = createStage()
    init {
        createResourceInfo(stage)
        createRepairButton(stage)

        stage.actors.forEach {
            println(it.name)
        }
    }

    private fun createStage(): Stage {
        val stage = stage(viewport = ScreenViewport(), batch = spriteBatch)
        Gdx.input.inputProcessor = stage

        return stage
    }
}