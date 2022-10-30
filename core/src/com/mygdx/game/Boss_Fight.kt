package com.mygdx.game

import kotlin.math.*
import kotlin.random.Random

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
    var baseDammage=1
    var bossState=BossState.SPAWNING






    fun setmode(f:Fightmode){
        fightmode=f
    }

    fun minionattack():Boolean{
        var attackvalue=0f
        attackvalue+= (state.archerMinionData.minionCountOutside*state.archerMinionData.offence)
        attackvalue+= (state.tankMinionData.minionCountOutside*state.tankMinionData.offence)
        if (attackvalue>0){
        val multi:Float= when (fightmode){
            Fightmode.NEUTRAL->1f
            Fightmode.DEFENCE->0.5f
            Fightmode.OFFENCE->2f
        }
        state.bossHp.current-=(attackvalue * multi).toInt()

        if (state.bossHp.isDead){
            giveLoot()
            respawnBoss()
            return true
        }
        }
        return false
    }

    fun respawnBoss(){
        state.bossLevel=(state.archerMinionData.factoryLevel+state.tankMinionData.factoryLevel/2)
        state.bossHp.total=10*state.bossLevel
        state.bossHp.current=state.bossHp.total
        val bosspos=state.bosses.filter{it.level==state.bossLevel}
        state.boss=bosspos.get((0..bosspos.size-1).random())
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
        val newAttack=state.boss.nextAttack();
        var dammage:Float= (state.boss.level*baseDammage*newAttack.dammage)

        if (state.tankMinionData.minionCountOutside>0){
            bossState=BossState.FIGHTERATTACK
            dammage=(dammage*multi)
            state.tankMinionData.minionCountOutside-=min(state.tankMinionData.minionCountOutside,(dammage/state.tankMinionData.defence))}else {
            if (state.archerMinionData.minionCountOutside > 0) {
                bossState=BossState.FIGHTERATTACK
                dammage=(dammage*multi)
                state.archerMinionData.minionCountOutside -= min(state.archerMinionData.minionCountOutside, dammage / state.archerMinionData.defence)
            } else {
                if (state.minerMinionData.minionCountOutside > 0) {
                    bossState = BossState.MINERATTACK
                    state.minerMinionData.minionCountOutside -= min(
                        state.minerMinionData.minionCountOutside,
                        dammage / state.minerMinionData.defence
                    )
                } else {
                    bossState = BossState.CASTLEATTACK
                    state.factoryHp.current -= min(state.factoryHp.current.toFloat(), dammage).toInt()

                    if (state.factoryHp.isDead) {
                        lost()
                    }
                }

            }
        }

    }

    fun lost(){

    }

    fun round(){
        if (bossState!=BossState.CASTLEATTACK&&state.factoryHp.current<state.factoryHp.total)
            state.factoryHp.current=min(state.factoryHp.total,state.factoryHp.current+5)
        if (!minionattack()){
            bossattack()
        }
    }

}

class Attack(val dammage:Float,var picture:String?){

}

class Boss(val level:Int,val image:String,val name:String,val attacks: List<Attack>){
    var point=0

    fun nextAttack():Attack{
        var res: Attack =Attack(1f,null)
        if (!attacks.isEmpty()){
            res=attacks.get(point)
            point =(point+1)%attacks.size
            if (res.picture==null)
                res.picture=image;
        }
        return res
    }


}

