package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisLabel
import com.mygdx.game.MinionType
import com.mygdx.game.PersistentGameState
import com.mygdx.game.ResettableGameState
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors
import ktx.actors.onClick
import ktx.actors.stage
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actors
import ktx.scene2d.vis.KVisTable
import ktx.scene2d.vis.flowGroup
import ktx.scene2d.vis.visImage
import ktx.scene2d.vis.visImageButton
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton
import ktx.scene2d.vis.visTooltip

fun initUi() {
    VisUI.load()
    Scene2DSkin.defaultSkin = loadSkin()
}

private fun @Scene2dDsl KWidget<Actor>.repairButton(gameState: ResettableGameState) {
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
        visLabel("") { name = "bossLevel" }

        spacing = 10f
    }
}

private fun @Scene2dDsl KWidget<Actor>.resources() {
    flowGroup {
        spacing = 20f

        flowGroup {
            spacing = 8f

            visImage(assetManager.get(AssetDescriptors.TRIANGLE)) {
                width = 10f
                height = 10f
            }
            visLabel("") { name = "res1" }
        }

        flowGroup {
            spacing = 8f

            visImage(assetManager.get(AssetDescriptors.CIRCLE))
            visLabel("") { name = "res2" }
        }

        flowGroup {
            spacing = 8f

            visImage(assetManager.get(AssetDescriptors.PENTAGON))
            visLabel("") { name = "res3" }
        }
    }
}

private fun @Scene2dDsl KVisTable.factory(type: MinionType, gameState: ResettableGameState) {
    val minionTypeName = type.name.lowercase()

    visTable { table ->
        flowGroup(vertical = true) {
            visLabel(type.name) { name = "factory_${minionTypeName}_name" }
            visLabel("") { name = "factory_${minionTypeName}_level" }
            visLabel("") { name = "factory_${minionTypeName}_rate" }

            it.minWidth(80f)
            spacing = -10f
        }

        visImageButton {
            it.fillX()
            it.fillY()
            it.pad(20f)
            onClick { gameState.onUpgradeFactoryClicked(type) }

            visTable {
                pad(10f)

                visLabel("Upgrade")
            }

            visTooltip(
                visTable {
                    visImage(assetManager.get(AssetDescriptors.TRIANGLE)) { cell ->
                        cell.width(20f)
                        cell.height(20f)
                        cell.padRight(10f)
                    }
                    visLabel("") { name = "factory_${minionTypeName}_upgrade_res1" }

                    row()
                    visImage(assetManager.get(AssetDescriptors.CIRCLE)) { cell ->
                        cell.width(20f)
                        cell.height(20f)
                        cell.padRight(10f)
                    }
                    visLabel("") { name = "factory_${minionTypeName}_upgrade_res2" }

                    row()
                    visImage(assetManager.get(AssetDescriptors.PENTAGON)) { cell ->
                        cell.width(20f)
                        cell.height(20f)
                        cell.padRight(10f)
                    }
                    visLabel("") { name = "factory_${minionTypeName}_upgrade_res3" }

                    pad(10f)
                },
            )
        }

        table.fillX()
        table.padBottom(10f)
    }
}

data class FactoryLabels(
    val name: VisLabel?,
    val level: VisLabel?,
    val rate: VisLabel?,
//    val upgrade_triangles: VisLabel?,
//    val upgrade_circles: VisLabel?,
//    val upgrade_squares: VisLabel?,
)

class GameUi(private val gameState: PersistentGameState) {
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    val stage = createStage()

    private val factoryHp by lazy { findWidget<VisLabel>("factoryHp") }
    private val bossHp by lazy { findWidget<VisLabel>("bossHp") }
    private val bossLevel by lazy { findWidget<VisLabel>("bossLevel") }
    private val res1 by lazy { findWidget<VisLabel>("res1") }
    private val res2 by lazy { findWidget<VisLabel>("res2") }
    private val res3 by lazy { findWidget<VisLabel>("res3") }

    private val factories by lazy {
        mapOf(
            MinionType.Tank to FactoryLabels(
                name = findWidget<VisLabel>("factory_tank_name"),
                level = findWidget<VisLabel>("factory_tank_level"),
                rate = findWidget<VisLabel>("factory_tank_rate"),
//                upgrade_triangles = findWidget<VisLabel>("factory_tank_upgrade_res1"),
//                upgrade_circles = findWidget<VisLabel>("factory_tank_upgrade_res2"),
//                upgrade_squares = findWidget<VisLabel>("factory_tank_upgrade_res3"),
            ),
            MinionType.Archer to FactoryLabels(
                name = findWidget<VisLabel>("factory_archer_name"),
                level = findWidget<VisLabel>("factory_archer_level"),
                rate = findWidget<VisLabel>("factory_archer_rate"),
//                upgrade_triangles = findWidget<VisLabel>("factory_archer_upgrade_res1"),
//                upgrade_circles = findWidget<VisLabel>("factory_archer_upgrade_res2"),
//                upgrade_squares = findWidget<VisLabel>("factory_archer_upgrade_res3"),
            ),
            MinionType.Miner to FactoryLabels(
                name = findWidget<VisLabel>("factory_miner_name"),
                level = findWidget<VisLabel>("factory_miner_level"),
                rate = findWidget<VisLabel>("factory_miner_rate"),
//                upgrade_triangles = findWidget<VisLabel>("factory_miner_upgrade_res1"),
//                upgrade_circles = findWidget<VisLabel>("factory_miner_upgrade_res2"),
//                upgrade_squares = findWidget<VisLabel>("factory_miner_upgrade_res3"),
            ),
        )
    }

    init {
        stage.actors {
            flowGroup(vertical = true) {
                x = 20f
                y = stage.height - 20f
                spacing = 400f

                flowGroup(vertical = true) {
                    spacing = 20f

                    factoryHealth()
                    repairButton(gameState.resettableState)
                }
            }

            flowGroup(vertical = true) {
                width = 40f
                x = (stage.width - it.width) / 2
                y = stage.height - 20f

                resources()
            }

            flowGroup(vertical = true) {
                x = stage.width - 220f
                y = stage.height - 20f

                boss()
            }

            flowGroup(vertical = true) {
                x = 160f
                y = 700f
                spacing = 200f

                visTable {
                    factory(MinionType.Tank, gameState.resettableState)

                    row()
                    factory(MinionType.Archer, gameState.resettableState)

                    row()
                    factory(MinionType.Miner, gameState.resettableState)
                }

                flowGroup(vertical = true) {
                    spacing = 70f

                    visLabel("") { name = "count_tank_inside" }
                    visLabel("") { name = "count_archer_inside" }
                    visLabel("") { name = "count_miner_inside" }
                }

                visImageButton {
                    onClick { gameState.resettableState.onToggleDoorClicked() }

                    visLabel("Switch")
                    // TODO Current state
                }

                flowGroup(vertical = true) {
                    spacing = 70f

                    visLabel("") { name = "count_tank_outside" }
                    visLabel("") { name = "count_archer_outside" }
                    visLabel("") { name = "count_miner_outside" }
                }
            }
        }
    }

    fun update() {
        factoryHp?.setText("${gameState.resettableState.factoryHp.current} / ${gameState.resettableState.factoryHp.total}")
        res1?.setText(gameState.resettableState.resourceInventory.triangles)
        res2?.setText(gameState.resettableState.resourceInventory.circles)
        res3?.setText(gameState.resettableState.resourceInventory.squares)

        bossHp?.setText("${gameState.resettableState.bossHp.current} / ${gameState.resettableState.bossHp.total}")
        bossLevel?.setText(gameState.resettableState.bossLevel)

        mapOf(
            MinionType.Tank to gameState.resettableState.tankMinionData,
            MinionType.Archer to gameState.resettableState.archerMinionData,
            MinionType.Miner to gameState.resettableState.minerMinionData,
        ).forEach { (type, data) ->
            factories[type].also {
                val upgradeCost = gameState.resettableState.getUpgradeCost(type)

                it?.level?.setText(data.factoryLevel)
                it?.rate?.setText("")

                findWidget<VisLabel>("factory_${type.name.lowercase()}_upgrade_res1")
                    ?.setText(upgradeCost.triangles)
                findWidget<VisLabel>("factory_${type.name.lowercase()}_upgrade_res2")
                    ?.setText(upgradeCost.circles)
                findWidget<VisLabel>("factory_${type.name.lowercase()}_upgrade_res3")
                    ?.setText(upgradeCost.squares)

                findWidget<VisLabel>("count_${type.name.lowercase()}_inside")
                    ?.setText(data.minionCountInside.toInt())
                findWidget<VisLabel>("count_${type.name.lowercase()}_outside")
                    ?.setText(data.minionCountOutside.toInt())

                // Can't do this :(
                // it?.upgrade_triangles?.setText("t" + upgradeCost.triangles)
                // it?.upgrade_circles?.setText("c" + upgradeCost.circles)
                // it?.upgrade_squares?.setText("s" + upgradeCost.squares)
            }
        }
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
