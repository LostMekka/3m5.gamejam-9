package com.mygdx.game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
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
import com.mygdx.game.screens.GameScreen
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

            visLabel("Buy ")
            visTable { visLabel("GG", style = "number") { it.pad(-20f, 0f, -3f, 0f) } }
            visLabel(" upgrades")
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

enum class IconSize {
    SMALL,
    LARGE;

    fun toFloat(): Float = when {
        this == SMALL -> 24f
        this == LARGE -> 36f
        else -> 36f
    }
}

private fun @Scene2dDsl KWidget<Actor>.icon(
    size: IconSize,
    texture: AssetDescriptor<Texture>,
    paddingTop: Float = 0f
) {
    visTable {
        visImage(assetManager.get(texture)) { cell ->
            cell.padTop(paddingTop)
            cell.width(size.toFloat())
            cell.height(size.toFloat())
        }
    }
}

private fun @Scene2dDsl KWidget<Actor>.resources() {
    floatingGroup {
        flowGroup(vertical = true) {
            spacing = 16f

            icon(IconSize.LARGE, AssetDescriptors.TRIANGLE, 5f)
            visLabel("") { name = "res1" }
        }

        flowGroup(vertical = true) {
            spacing = 16f
            x = 200f

            icon(IconSize.LARGE, AssetDescriptors.CIRCLE, 5f)
            visLabel("") { name = "res2" }
        }

        flowGroup(vertical = true) {
            spacing = 16f
            x = 400f

            icon(IconSize.LARGE, AssetDescriptors.PENTAGON, 5f)
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
                visLabel("Prod.-Lvl ")
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

                        icon(IconSize.SMALL, AssetDescriptors.TRIANGLE, 10f)
                        visLabel("") { name = "factory_${minionTypeName}_upgrade_res1" }
                    }

                    flowGroup {
                        name = "factory_${minionTypeName}_upgrade_res2_block"
                        spacing = 2f

                        icon(IconSize.SMALL, AssetDescriptors.CIRCLE, 10f)
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
    val count: VisLabel?,
)

class GameUi(
    private val gameState: PersistentGameState,
    private val screen: GameScreen,
) {
    private val spriteBatch = SpriteBatch().apply {
        color = Color.WHITE
    }
    val stage = createStage()

    // Labels for state of current round
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
                upgrade_triangles = findWidget<VisLabel>("factory_tank_upgrade_res1"),
                upgrade_triangles_block = findWidget<FlowGroup>("factory_tank_upgrade_res1_block"),
                upgrade_circles = findWidget<VisLabel>("factory_tank_upgrade_res2"),
                upgrade_circles_block = findWidget<FlowGroup>("factory_tank_upgrade_res2_block"),
                count = findWidget<VisLabel>("count_tank"),
            ),
            MinionType.Archer to FactoryLabels(
                name = findWidget<VisLabel>("factory_archer_name"),
                level = findWidget<VisLabel>("factory_archer_level"),
                rate = findWidget<VisLabel>("factory_archer_rate"),
                upgrade_triangles = findWidget<VisLabel>("factory_archer_upgrade_res1"),
                upgrade_triangles_block = findWidget<FlowGroup>("factory_archer_upgrade_res1_block"),
                upgrade_circles = findWidget<VisLabel>("factory_archer_upgrade_res2"),
                upgrade_circles_block = findWidget<FlowGroup>("factory_archer_upgrade_res2_block"),
                count = findWidget<VisLabel>("count_archer"),
            ),
            MinionType.Miner to FactoryLabels(
                name = findWidget<VisLabel>("factory_miner_name"),
                level = findWidget<VisLabel>("factory_miner_level"),
                rate = findWidget<VisLabel>("factory_miner_rate"),
                upgrade_triangles = findWidget<VisLabel>("factory_miner_upgrade_res1"),
                upgrade_triangles_block = findWidget<FlowGroup>("factory_miner_upgrade_res1_block"),
                upgrade_circles = findWidget<VisLabel>("factory_miner_upgrade_res2"),
                upgrade_circles_block = findWidget<FlowGroup>("factory_miner_upgrade_res2_block"),
                count = findWidget<VisLabel>("count_miner"),
            ),
        )
    }

    // GG upgrades
    private val ggTankDefenseRes2 by lazy { findWidget<VisLabel>("gg_upgrade_tank_defense_res2") }
    private val ggTankDefenseRes3 by lazy { findWidget<VisLabel>("gg_upgrade_tank_defense_res3") }
    private val ggArcherDefenseRes2 by lazy { findWidget<VisLabel>("gg_upgrade_archer_defense_res2") }
    private val ggArcherDefenseRes3 by lazy { findWidget<VisLabel>("gg_upgrade_archer_defense_res3") }
    private val ggArcherAttackRes2 by lazy { findWidget<VisLabel>("gg_upgrade_archer_attack_res2") }
    private val ggArcherAttackRes3 by lazy { findWidget<VisLabel>("gg_upgrade_archer_attack_res3") }
    private val ggMinerDefenseRes2 by lazy { findWidget<VisLabel>("gg_upgrade_miner_defense_res2") }
    private val ggMinerDefenseRes3 by lazy { findWidget<VisLabel>("gg_upgrade_miner_defense_res3") }
    private val ggMinerTripRes2 by lazy { findWidget<VisLabel>("gg_upgrade_miner_trip_res2") }
    private val ggMinerTripRes3 by lazy { findWidget<VisLabel>("gg_upgrade_miner_trip_res3") }

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

                    visLabel("") { name = "count_tank" }
                    visLabel("") { name = "count_archer" }
                    visLabel("") { name = "count_miner" }
                }
            }

            visTable {
                x = 1432f
                y = 710f

                visImageButton {
                    pad(4f, 30f, 16f, 30f)
                    onClick {
                        soundController.playGGButtonSound()
                        gameState.onGGPressed()
                        screen.onGGPressed()
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

                    flowGroup {
                        it.padTop(30f)
                        visLabel("Buy ")
                        visTable { visLabel("GG", style = "number") { it.pad(-20f, 0f, -3f, 0f) } }
                        visLabel(" upgrades")
                    }
                    row()

                    visTable {
                        visLabel(MinionType.Tank.name)

                        visImageButton {
                            onClick { gameState.onUpgradeDefence(MinionType.Tank) }

                            flowGroup(vertical = false) {
                                visLabel("Defense ↑")
                                icon(IconSize.SMALL, AssetDescriptors.CIRCLE, 10f)
                                visLabel("") { name = "gg_upgrade_tank_defense_res2" }
                                icon(IconSize.SMALL, AssetDescriptors.PENTAGON, 10f)
                                visLabel("") { name = "gg_upgrade_tank_defense_res3" }

                                spacing = 3f
                                it.pad(0f, 20f, 0f, 20f)
                            }

                            it.space(10f)
                            it.fill()
                        }

                        row()

                        visLabel(MinionType.Archer.name)

                        visImageButton {
                            onClick { gameState.onUpgradeDefence(MinionType.Archer) }

                            flowGroup(vertical = false) {
                                visLabel("Defense ↑")
                                icon(IconSize.SMALL, AssetDescriptors.CIRCLE, 10f)
                                visLabel("") { name = "gg_upgrade_archer_defense_res2" }
                                icon(IconSize.SMALL, AssetDescriptors.PENTAGON, 10f)
                                visLabel("") { name = "gg_upgrade_archer_defense_res3" }

                                spacing = 3f
                                it.pad(0f, 20f, 0f, 20f)
                            }

                            it.space(10f)
                            it.fill()
                        }

                        visImageButton {
                            onClick { gameState.onUpgradeBaseAttack(MinionType.Archer) }

                            flowGroup(vertical = false) {
                                visLabel("Attack ↑")
                                icon(IconSize.SMALL, AssetDescriptors.CIRCLE, 10f)
                                visLabel("") { name = "gg_upgrade_archer_attack_res2" }
                                icon(IconSize.SMALL, AssetDescriptors.PENTAGON, 10f)
                                visLabel("") { name = "gg_upgrade_archer_attack_res3" }

                                spacing = 3f
                                it.pad(0f, 20f, 0f, 20f)
                            }

                            it.space(10f)
                            it.fill()
                        }

                        row()

                        visLabel(MinionType.Miner.name)

                        visImageButton {
                            onClick { gameState.onUpgradeDefence(MinionType.Miner) }

                            flowGroup(vertical = false) {
                                visLabel("Defense ↑")
                                icon(IconSize.SMALL, AssetDescriptors.CIRCLE, 10f)
                                visLabel("") { name = "gg_upgrade_miner_defense_res2" }
                                icon(IconSize.SMALL, AssetDescriptors.PENTAGON, 10f)
                                visLabel("") { name = "gg_upgrade_miner_defense_res3" }

                                spacing = 3f
                                it.pad(0f, 20f, 0f, 20f)
                            }

                            it.space(10f)
                            it.fill()
                        }

                        visImageButton {
                            onClick { gameState.onUpgradeRoundTrip() }

                            flowGroup(vertical = false) {
                                visLabel("Round trip ↓")
                                icon(IconSize.SMALL, AssetDescriptors.CIRCLE, 10f)
                                visLabel("") { name = "gg_upgrade_miner_trip_res2" }
                                icon(IconSize.SMALL, AssetDescriptors.PENTAGON, 10f)
                                visLabel("") { name = "gg_upgrade_miner_trip_res3" }

                                spacing = 3f
                                it.pad(0f, 20f, 0f, 20f)
                            }

                            it.space(10f)
                            it.fill()
                        }
                    }
                }

                buttonsTable += visTable {
                    setFillParent(true)
                    align(Align.bottomLeft)
                    padBottom(8f)

                    visImageButton {
                        onClick { dialog.isVisible = false }

                        visLabel("Close")
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

                it?.count?.setText(ceil(data.minionCountOutside).toInt())
            }
        }

        gameState.getDefenseUpgradeCost(MinionType.Tank).also {
            ggTankDefenseRes2?.setText(it.circles)
            ggTankDefenseRes3?.setText(it.pentas)
        }

        gameState.getDefenseUpgradeCost(MinionType.Archer).also {
            ggArcherDefenseRes2?.setText(it.circles)
            ggArcherDefenseRes3?.setText(it.pentas)
        }

        gameState.getBaseAttackUpgradeCost(MinionType.Archer).also {
            ggArcherAttackRes2?.setText(it.circles)
            ggArcherAttackRes3?.setText(it.pentas)
        }

        gameState.getDefenseUpgradeCost(MinionType.Miner).also {
            ggMinerDefenseRes2?.setText(it.circles)
            ggMinerDefenseRes3?.setText(it.pentas)
        }

        gameState.getRoundTripUpgradeCost().also {
            ggMinerTripRes2?.setText(it.circles)
            ggMinerTripRes3?.setText(it.pentas)
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
