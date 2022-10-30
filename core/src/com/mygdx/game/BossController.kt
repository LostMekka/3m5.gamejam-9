package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import kotlin.math.sin

class BossController (var texture: Texture){


fun updateBoss(newTexture: Texture){texture=newTexture}

fun display (batch: SpriteBatch,delta:Float,effects:MutableList<Attack>){
   for (e in effects){
        e.expire-=delta
    }
    while (effects.size>0&&effects.get(0).expire<=0) {
        effects.get(0).expire=1f
        effects.removeFirst()}
    if (effects.size>0){

        batch.draw(
            effects.get(0).picture,
            1050f,
            750f,
            texture.width/4f,
            texture.height/4f,
        )
        effects.get(0).time2-=delta
        if (effects.get(0).time2<=0) {
            effects.get(0).time2=effects.get(0).time
            effects.get(0).expire=1f
            effects.removeFirst()
        }
    }else {


    batch.draw(
        texture,
        1050f,
        750f,
        texture.width/4f,
        texture.height/4f,
    )
}
}


}
