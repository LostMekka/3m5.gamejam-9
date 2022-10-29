package com.mygdx.game

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



    fun setmode(f:Fightmode){
        fightmode=f
    }

    fun minionattack(){
        var attackvalue=0f;
        attackvalue+= (state.minionArcherCountOutside*archer_offence)
        attackvalue+= (state.minionTankCountOutside*tank_offence)
        state.bossHp-=(attackvalue).toInt()

        if (state.bossHp<=0){
            giveLoot()
            respawnBoss()
        }
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

    }

    fun round(){
        minionattack()
        bossattack()
    }

}

