package com.mygdx.game

class PersistentGameState {
    var resettableState = ResettableGameState()

    fun onGGPressed() {
        // TODO: animation?
        // TODO: sound?
        val squares = resettableState.resourceInventory.squares
        resettableState = ResettableGameState()
        resettableState.resourceInventory.squares = squares
    }

    fun calculateFrame(delta: Float) {
        resettableState.calculateFrame(delta)
    }
}
