package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisLabel
import com.mygdx.game.GameState
import ktx.scene2d.*
import ktx.actors.*
import ktx.scene2d.vis.*

fun initUi() {
    VisUI.load()
    Scene2DSkin.defaultSkin = loadSkin()
}

fun createRepairButton(stage: Stage, gameState: GameState) {
    stage.actors {
        flowGroup(vertical = false) {
            y = stage.height - 70f
            x = 20f

            visTextButton("Repair") {
                onClick {
                    gameState.onRepairClicked()
                }

                it.name = "repair"
            }
        }
    }
}

fun createHealth(stage: Stage) {
    stage.actors {
        flowGroup(vertical = false) {
            y = stage.height - 20f
            x = 20f

            visLabel("") { name = "factoryHp" }
        }
    }
}

fun createResources(stage: Stage) {
    stage.actors {
        flowGroup(vertical = true) {
            y = stage.height - 20f
            x = 120f

            flowGroup {
                visLabel("") { name = "res1" }
            }

            flowGroup {
                visLabel("") { name = "res2" }
            }

            flowGroup {
                visLabel("") { name = "res3" }
            }
        }.also {
            it.spacing = 20f
        }
    }
}

class GameUi(private val gameState: GameState) {
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    val stage = createStage()

    private val factoryHp by lazy { findWidget<VisLabel>("factoryHp") }
    private val res1 by lazy { findWidget<VisLabel>("res1") }
    private val res2 by lazy { findWidget<VisLabel>("res2") }
    private val res3 by lazy { findWidget<VisLabel>("res3") }

    init {
        createHealth(stage)
        createRepairButton(stage, gameState)
        createResources(stage)
    }

    fun update() {
        factoryHp?.setText(
            gameState.factoryHp.current.toString() + " / " + gameState.factoryHp.total.toString()
        )
        res1?.setText(gameState.resourceInventory.triangles)
        res2?.setText(gameState.resourceInventory.circles)
        res3?.setText(gameState.resourceInventory.squares)
    }

    private fun <T : Actor?> findWidget(name: String): T? {
        stage.actors.forEach {
            if (it is Group) {
                val actor = it.findActor<T>(name)
                if (actor != null) {
                    return actor
                }
            }
        }

        return null
    }

    private fun createStage(): Stage {
        val stage = stage(viewport = ScreenViewport(), batch = spriteBatch)
        Gdx.input.inputProcessor = stage

        return stage
    }
}