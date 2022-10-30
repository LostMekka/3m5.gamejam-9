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
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors
import ktx.scene2d.*
import ktx.actors.*
import ktx.scene2d.vis.*

fun initUi() {
    val font = assetManager.get(AssetDescriptors.FONT)

    VisUI.load()
    Scene2DSkin.defaultSkin = loadSkin()
}

private fun @Scene2dDsl KWidget<Actor>.repairButton(gameState: GameState) {
    flowGroup(vertical = false) {
        visTextButton("Repair") {
            onClick {
                gameState.onRepairClicked()
            }

            it.name = "repair"
        }
    }
}

private fun @Scene2dDsl KWidget<Actor>.factoryHealth() {
    visLabel("") { name = "factoryHp" }
}

private fun @Scene2dDsl KWidget<Actor>.boss() {
    flowGroup(vertical = false) {
        visLabel("") { name = "bossHp" }
        visLabel("1") { name = "bossLevel" }

        spacing = 10f
    }
}


private fun @Scene2dDsl KWidget<Actor>.resources(stage: Stage) {
    flowGroup {
        spacing = 20f

        flowGroup {
            visLabel("") { name = "res1" }
        }

        flowGroup {
            visLabel("") { name = "res2" }
        }

        flowGroup {
            visLabel("") { name = "res3" }
        }
    }
}

class GameUi(private val gameState: GameState) {
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    val stage = createStage()

    private val factoryHp by lazy { findWidget<VisLabel>("factoryHp") }
    private val bossHp by lazy { findWidget<VisLabel>("bossHp") }
    private val res1 by lazy { findWidget<VisLabel>("res1") }
    private val res2 by lazy { findWidget<VisLabel>("res2") }
    private val res3 by lazy { findWidget<VisLabel>("res3") }

    init {
        stage.actors {
            flowGroup(vertical = true) {
                x = 20f
                y = stage.height - 20f
                spacing = 600f

                flowGroup(vertical = true) {
                    spacing = 20f

                    factoryHealth()
                    repairButton(gameState)
                }

                resources(stage)

                boss()
            }
        }
    }

    fun update() {
        factoryHp?.setText("${gameState.factoryHp.current} / ${gameState.factoryHp.total}")
        res1?.setText(gameState.resourceInventory.triangles)
        res2?.setText(gameState.resourceInventory.circles)
        res3?.setText(gameState.resourceInventory.squares)

        bossHp?.setText("${gameState.bossHp.current} / ${gameState.bossHp.total}")
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
