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

    fun calculateFrame(delta: Float) {
        resettableState.calculateFrame(delta)
        resettableState.factoryHp= Hp(levelMediator()*100,min(resettableState.factoryHp.current,levelMediator()*100))
    }


}
