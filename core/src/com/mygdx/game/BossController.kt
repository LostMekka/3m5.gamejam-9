package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import kotlin.math.sin

class BossController (var texture: Texture){

fun updateBoss(newTexture: Texture){texture=newTexture}

fun display (batch: SpriteBatch){
    if (texture!=null)
    batch.draw(
        texture,
        500f,
        500f,
        texture.width/3f,
        texture.height/3f,
    )
}


}
