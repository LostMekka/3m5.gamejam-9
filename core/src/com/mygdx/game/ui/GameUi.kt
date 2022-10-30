package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.layout.FlowGroup
import com.kotcrab.vis.ui.widget.VisDialog
import com.kotcrab.vis.ui.widget.VisLabel
import com.mygdx.game.MinionType
import com.mygdx.game.PersistentGameState
import com.mygdx.game.assetManager
import com.mygdx.game.assets.AssetDescriptors
import com.mygdx.game.common.soundController
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.actors.stage
import ktx.scene2d.*
import ktx.scene2d.vis.*
import kotlin.math.ceil

fun initUi() {
    VisUI.load()
    Scene2DSkin.defaultSkin = loadSkin()
}

private fun @Scene2dDsl KWidget<Actor>.repairAndUpgradeButtons(gameState: PersistentGameState, openPopup: () -> Unit) {
    flowGroup(vertical = false) {
        visImageButton {
            name = "repair"
            padLeft(22f)
            padBottom(4f)
            padTop(-4f)
            onClick {
                gameState.resettableState.onRepairClicked()
            }

            flowGroup {
                spacing = 5f

                visLabel("Repair")

                flowGroup {
                    visLabel("")
                    visLabel("")
                    visLabel("")
                }
            }
        }

        visImageButton {
            padLeft(22f)
            padBottom(4f)
            padTop(-4f)
            onClick {
                soundController.playCommonButtonSound()
                openPopup()
            }

            label("Upgrades")
        }

        spacing = 20f
    }
}

private fun @Scene2dDsl KWidget<Actor>.factoryHealth() {
    visLabel("") { name = "factoryHp" }
}

private fun @Scene2dDsl KWidget<Actor>.boss() {
    flowGroup(vertical = true) {
        visLabel("") { name = "bossHp" }
        flowGroup(vertical = false) {
            visLabel("Lvl ")
            visLabel("") { name = "bossLevel" }
        }

        spacing = 5f
    }
}

private fun @Scene2dDsl KWidget<Actor>.resources() {
    floatingGroup {
        flowGroup(vertical = true) {
            spacing = 16f

            visTable {
                visImage(assetManager.get(AssetDescriptors.TRIANGLE)) { cell ->
                    cell.padTop(5f)
                    cell.width(36f)
                    cell.height(36f)
                }
            }
            visLabel("") { name = "res1" }
        }

        flowGroup(vertical = true) {
            spacing = 16f
            x = 200f

            visTable {
                visImage(assetManager.get(AssetDescriptors.CIRCLE)) { cell ->
                    cell.padTop(5f)
                    cell.width(36f)
                    cell.height(36f)
                }
            }
            visLabel("") { name = "res2" }
        }

        flowGroup(vertical = true) {
            spacing = 16f
            x = 400f

            visTable {
                visImage(assetManager.get(AssetDescriptors.PENTAGON)) { cell ->
                    cell.padTop(5f)
                    cell.width(36f)
                    cell.height(36f)
                }
            }
            visLabel("") { name = "res3" }
        }
    }
}

private fun @Scene2dDsl KVisTable.factory(type: MinionType, gameState: PersistentGameState) {
    val minionTypeName = type.name.lowercase()

    visTable { table ->
        flowGroup(vertical = true) {
            visLabel(type.name) { name = "factory_${minionTypeName}_name" }
            flowGroup {
                visLabel("Lvl ")
                visLabel("") { name = "factory_${minionTypeName}_level" }
            }

            it.minWidth(180f)
            spacing = -5f
        }

        visImageButton(style = "flipped") {
            it.fillX()
            it.fillY()
            it.pad(20f)
            onClick { gameState.resettableState.onUpgradeFactoryClicked(type) }

            visTable {
                pad(10f)

                visLabel("Upgrade")
                row()

                flowGroup {
                    spacing = 2f

                    flowGroup {
                        name = "factory_${minionTypeName}_upgrade_res1_block"
                        spacing = 2f

                        visTable {
                            visImage(assetManager.get(AssetDescriptors.TRIANGLE)) { cell ->
                                cell.padTop(10f)
                                cell.width(24f)
                                cell.height(24f)
                            }
                        }
                        visLabel("") { name = "factory_${minionTypeName}_upgrade_res1" }
                    }

                    flowGroup {
                        name = "factory_${minionTypeName}_upgrade_res2_block"
                        spacing = 2f

                        visTable {
                            visImage(assetManager.get(AssetDescriptors.CIRCLE)) { cell ->
                                cell.padTop(10f)
                                cell.width(24f)
                                cell.height(24f)
                            }
                        }
                        visLabel("") { name = "factory_${minionTypeName}_upgrade_res2" }
                    }
                }
            }
        }

        table.fillX()
        table.padBottom(50f)
    }
}

data class FactoryLabels(
    val name: VisLabel?,
    val level: VisLabel?,
    val rate: VisLabel?,
    val upgrade_triangles: VisLabel?,
    val upgrade_triangles_block: FlowGroup?,
    val upgrade_circles: VisLabel?,
    val upgrade_circles_block: FlowGroup?,
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
    private val gate by lazy { findWidget<VisLabel>("gate") }

    private val factories by lazy {
        mapOf(
            MinionType.Tank to FactoryLabels(
                name = findWidget<VisLabel>("factory_tank_name"),
                level = findWidget<VisLabel>("factory_tank_level"),
                rate = findWidget<VisLabel>("factory_tank_rate"),
                upgrade_triangles = findWidget<VisLabel>("factory_tank_upgrade_res1"),
                upgrade_triangles_block = findWidget<FlowGroup>("factory_tank_upgrade_res1_block"),
                upgrade_circles = findWidget<VisLabel>("factory_tank_upgrade_res2"),
                upgrade_circles_block = findWidget<FlowGroup>("factory_tank_upgrade_res2_block"),
            ),
            MinionType.Archer to FactoryLabels(
                name = findWidget<VisLabel>("factory_archer_name"),
                level = findWidget<VisLabel>("factory_archer_level"),
                rate = findWidget<VisLabel>("factory_archer_rate"),
                upgrade_triangles = findWidget<VisLabel>("factory_archer_upgrade_res1"),
                upgrade_triangles_block = findWidget<FlowGroup>("factory_archer_upgrade_res1_block"),
                upgrade_circles = findWidget<VisLabel>("factory_archer_upgrade_res2"),
                upgrade_circles_block = findWidget<FlowGroup>("factory_archer_upgrade_res2_block"),
            ),
            MinionType.Miner to FactoryLabels(
                name = findWidget<VisLabel>("factory_miner_name"),
                level = findWidget<VisLabel>("factory_miner_level"),
                rate = findWidget<VisLabel>("factory_miner_rate"),
                upgrade_triangles = findWidget<VisLabel>("factory_miner_upgrade_res1"),
                upgrade_triangles_block = findWidget<FlowGroup>("factory_miner_upgrade_res1_block"),
                upgrade_circles = findWidget<VisLabel>("factory_miner_upgrade_res2"),
                upgrade_circles_block = findWidget<FlowGroup>("factory_miner_upgrade_res2_block"),
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
                    repairAndUpgradeButtons(gameState) {
                        stage.actors.forEach {
                            if (it !is VisDialog || it.name != "upgrades_popup") return@forEach

                            it.isVisible = true
                        }
                    }
                }
            }

            flowGroup(vertical = true) {
                width = 40f
                x = 310f
                y = stage.height - 20f

                resources()
            }

            flowGroup(vertical = true) {
                x = stage.width - 220f
                y = stage.height - 20f

                boss()
            }

            flowGroup {
                x = 130f
                y = 690f

                visTable {
                    val ninePatch = NinePatchDrawable(
                        NinePatch(assetManager.get(AssetDescriptors.FACTORY_BACKGROUND), 21, 21, 21, 21)
                    )

                    for (i in 1..3) {
                        if (i != 1) row()

                        visImage(ninePatch) {
                            it.width(430f)
                            it.height(160f)
                            it.padBottom(60f)
                        }
                    }
                }
            }

            flowGroup {
                x = 160f
                y = 700f

                visTable {
                    factory(MinionType.Tank, gameState)

                    row()
                    factory(MinionType.Archer, gameState)

                    row()
                    factory(MinionType.Miner, gameState)
                }
            }

            flowGroup(vertical = false) {
                spacing = 40f
                x = 660f
                y = 730f

                visLabel("Amount")
                flowGroup(vertical = true) {
                    spacing = 160f

                    visLabel("") { name = "count_tank_outside" }
                    visLabel("") { name = "count_archer_outside" }
                    visLabel("") { name = "count_miner_outside" }
                }
            }

//            flowGroup(vertical = false) {
//                spacing = 40f
//                x = 1160f
//                y = 730f
//
//                visLabel("")
//                flowGroup(vertical = true) {
//                    spacing = 160f
//
//                    visLabel("") { name = "count_tank_outside" }
//                    visLabel("") { name = "count_archer_outside" }
//                    visLabel("") { name = "count_miner_outside" }
//                }
//            }

//            flowGroup(vertical = true) {
//                x = 860f
//                y = 735f
//
//                visImageButton {
//                    onClick { gameState.resettableState.onToggleDoorClicked() }
//                    padLeft(22f)
//                    padRight(22f)
//                    padBottom(4f)
//                    padTop(-4f)
//
//                    visLabel("") {
//                        name = "gate"
//                    }
//                }
//            }

            visTable {
                x = 1432f
                y = 710f

                visImageButton {
                    pad(4f, 30f, 16f, 30f)
                    onClick {
                        soundController.playGGButtonSound()
                        gameState.onGGPressed()
                    }

                    label("GG", style = "number")
                }
            }

            visDialog("", style = "dialog") {
                val dialog = this
                val margin = 100f

                isVisible = false
                x = margin
                y = margin
                height = stage.height - margin * 2
                width = stage.width - margin * 2
                name = "upgrades_popup"

                contentTable += visTable(defaultSpacing = true) {
                    setFillParent(true)
                    align(Align.topLeft)
                    x = 20f
                    y = 20f

                    visLabel("Buy upgrades", style = "number")
                    row()

                    flowGroup(vertical = true) {
                        spacing = 35f

                        visImageButton {
                            onClick { gameState.onUpgradeDefence(MinionType.Tank) }

                            visLabel(MinionType.Tank.name + " Defense +")
                        }

                        flowGroup {
                            spacing = 20f

                            visImageButton {
                                onClick { gameState.onUpgradeDefence(MinionType.Archer) }

                                visLabel(MinionType.Archer.name + " Defense +")
                            }

                            visImageButton {
                                onClick { gameState.onUpgradeBaseAttack(MinionType.Archer) }

                                visLabel(MinionType.Archer.name + " Attack +")
                            }
                        }

                        flowGroup {
                            spacing = 20f

                            visImageButton {
                                onClick { gameState.onUpgradeDefence(MinionType.Miner) }

                                visLabel(MinionType.Miner.name + " Defense +")
                            }

                            visImageButton {
                                onClick { gameState.onUpgradeRoundtrip() }

                                visLabel(MinionType.Miner.name + " Roundtrip +")
                            }
                        }
                    }
                }

                buttonsTable += visTable {
                    setFillParent(true)
                    align(Align.bottomLeft)
                    padBottom(8f)

                    visTextButton("Close") {
                        onClick { dialog.isVisible = false }
                    }
                }
            }
        }
    }

    fun update() {
        val gs = gameState.resettableState

        factoryHp?.setText("${gs.factoryHp.current} / ${gs.factoryHp.total}")
        res1?.setText(gs.resourceInventory.triangles)
        res2?.setText(gs.resourceInventory.circles)
        res3?.setText(gs.resourceInventory.pentas)

        bossHp?.setText("${gs.bossHp.current} / ${gs.bossHp.total}")
        bossLevel?.setText(gs.bossLevel)

        gate?.setText((if (gs.doorIsOpen) "Close" else "Open") + " gate")

        mapOf(
            MinionType.Tank to gs.tankMinionData,
            MinionType.Archer to gs.archerMinionData,
            MinionType.Miner to gs.minerMinionData,
        ).forEach { (type, data) ->
            factories[type].also {
                val upgradeCost = gs.getUpgradeCost(type)

                it?.level?.setText(data.factoryLevel)
                it?.rate?.setText("")

                it?.upgrade_triangles?.setText(upgradeCost.triangles)
                it?.upgrade_circles?.setText(upgradeCost.circles)

                // TODO Find labels only once
                findWidget<VisLabel>("count_${type.name.lowercase()}_inside")
                    ?.setText(ceil(data.minionCountInside).toInt())
                findWidget<VisLabel>("count_${type.name.lowercase()}_outside")
                    ?.setText(ceil(data.minionCountOutside).toInt())
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
