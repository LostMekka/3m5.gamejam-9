package com.mygdx.game

import kotlin.math.*

enum class Fightmode{
    NEUTRAL,
    OFFENCE,
    DEFENCE
}

enum class BossState{
    SPAWNING,
    FIGHTERATTACK,
    MINERATTACK,
    CASTLEATTACK
}

class Boss_Fight(val state:GameState) {
    var fightmode=Fightmode.NEUTRAL
    var archer_defense=1
    var archer_offence=4
    var tank_defence=4
    var tank_offence=2
    var baseDammage=10
    var miner_defense=1
    var bossState=BossState.SPAWNING


    fun setmode(f:Fightmode){
        fightmode=f
    }

    fun minionattack():Boolean{
        var attackvalue=0f
        attackvalue+= (state.archerMinionData.minionCountOutside*archer_offence)
        attackvalue+= (state.tankMinionData.minionCountOutside*tank_offence)
        val multi:Float= when (fightmode){
            Fightmode.NEUTRAL->1f
            Fightmode.DEFENCE->0.5f
            Fightmode.OFFENCE->2f
        }
        state.bossHp-=(attackvalue * multi).toInt()

        if (state.bossHp<=0){
            giveLoot()
            respawnBoss()
            return true
        }
        return false
    }

    fun respawnBoss(){
        state.bossHp=1000
        state.bossLevel=(state.archerMinionData.factoryLevel+state.tankMinionData.factoryLevel/2)
    }

    fun giveLoot(){
        state.resourceInventory.circles+=state.bossLevel
        state.resourceInventory.squares+=(state.bossLevel/10)

    }



    fun bossattack(){

        val multi:Float= when (fightmode){
            Fightmode.NEUTRAL->1f
            Fightmode.DEFENCE->0.5f
            Fightmode.OFFENCE->2f
        }
        var dammage:Float= (state.bossLevel*baseDammage).toFloat()

        if (state.tankMinionData.minionCountOutside>0||state.archerMinionData.minionCountOutside>0){
            bossState=BossState.FIGHTERATTACK
            dammage=(dammage*multi)
            var dealt= min(state.tankMinionData.minionCountOutside,(dammage/tank_defence))
            state.tankMinionData.minionCountOutside-=dealt
            dealt= min(state.archerMinionData.minionCountOutside,dammage/archer_defense)
            state.archerMinionData.minionCountOutside-=dealt
        }else{

            if(state.minerMinionData.minionCountOutside>0){
                bossState=BossState.MINERATTACK

                state.minerMinionData.minionCountOutside-=min(state.minerMinionData.minionCountOutside,dammage/miner_defense)
            }else{
                bossState=BossState.CASTLEATTACK

                state.factoryHp-= min(state.factoryHp.toFloat(),dammage).toInt()

                if (state.factoryHp<=0){
                    lost()
                }
            }

        }


    }

    fun lost(){

    }

    fun round(){
        if (bossState!=BossState.CASTLEATTACK&&state.factoryHp<state.factoryMaxHp)
            state.factoryHp=min(state.factoryMaxHp,state.factoryHp+5)
        if (!minionattack()){
            bossattack()
        }
    }

}



