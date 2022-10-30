package com.mygdx.game

import kotlin.math.min

class PersistentGameState {

    var resettableState = ResettableGameState()
    private fun levelMediator():Int {
        var sum=0
        for(minionType in MinionType.values()){
            sum+= resettableState[minionType].factoryLevel
        }
        return sum/3

    }
    fun onGGPressed() {
        // TODO: animation?
        // TODO: sound?
        val squares = resettableState.resourceInventory.squares
        resettableState = ResettableGameState()
        resettableState.resourceInventory.squares = squares
    }

    fun onUpgradeBaseAttack(minionType: MinionType){
        if (resettableState.resourceInventory.contains(strengthUpgradeCost(minionType,resettableState[minionType].attackMultiplier))){
            resettableState[minionType].attackMultiplier*= 1.2f
            resettableState.resourceInventory-=strengthUpgradeCost(minionType,resettableState[minionType].attackMultiplier);

        }
    }

    fun onUpgradeDefence(minionType: MinionType){
        if (resettableState.resourceInventory.contains(strengthUpgradeCost(minionType,resettableState[minionType].defenceMultiplier))){
            resettableState[minionType].defenceMultiplier*= 1.2f
            resettableState.resourceInventory-=strengthUpgradeCost(minionType,resettableState[minionType].defenceMultiplier);

        }
    }

    fun onUpgradeRoundtrip(){
        if (resettableState.resourceInventory.contains(roundtripUpgradeCost(resettableState.roundTripShortening))){
            resettableState.roundTripShortening*=0.9f;
            resettableState.resourceInventory-=roundtripUpgradeCost(resettableState.roundTripShortening);

        }

    }

    fun calculateFrame(delta: Float) {
        resettableState.calculateFrame(delta)
        resettableState.factoryHp= Hp(levelMediator()*100,min(resettableState.factoryHp.current,levelMediator()*100))
    }


}
