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
    val multiplikator=2
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
        var attackvalue=0f;
        attackvalue+= (state.minionArcherCountOutside*archer_offence)
        attackvalue+= (state.minionTankCountOutside*tank_offence)
        var multi:Float= when (fightmode){
            Fightmode.NEUTRAL->1f
            Fightmode.DEFENCE->0.5f
            Fightmode.OFFENCE->2f
        }
        state.bossHp-=(attackvalue * multi).toInt()

        if (state.bossHp<=0){
            giveLoot()
            respawnBoss()
            return true;
        }
        return false;
    }

    fun respawnBoss(){
        state.bossHp=1000;
        state.bossLevel=(state.minionArcherFactoryLevel+state.minionTankFactoryLevel/2).toInt()
    }

    fun giveLoot(){
        state.resourceCircle+=state.bossLevel
        state.resourceSquare+=(state.bossLevel/10)

    }



    fun bossattack(){

        var multi:Float= when (fightmode){
            Fightmode.NEUTRAL->1f
            Fightmode.DEFENCE->0.5f
            Fightmode.OFFENCE->2f
        }
        var dammage:Float= (state.bossLevel*baseDammage).toFloat()

        if (state.minionTankCountOutside>0||state.minionArcherCountOutside>0){
            bossState=BossState.FIGHTERATTACK
            dammage=(dammage*multi)
            var dealt= min(state.minionTankCountOutside,(dammage.toFloat()/tank_defence))
            state.minionTankCountOutside-=dealt;
            dealt= min(state.minionArcherCountOutside,dammage/archer_defense)
            state.minionArcherCountOutside-=dealt;
        }else{

            if(state.minionMinerCountOutside>0){
                bossState=BossState.MINERATTACK
                var dealt= min(state.minionMinerCountOutside,dammage/miner_defense)
                state.minionMinerCountOutside-=dealt;
            }else{
                bossState=BossState.CASTLEATTACK
                var dealt= min(state.minionMinerCountOutside,dammage)
                state.factoryHp-= dealt.toInt()

                if (state.factoryHp<=0){
                    lost()
                }
            }

        }


    }

    fun lost(){

    }

    fun round(){
        if (!minionattack()){
            bossattack()
        }
    }

}



