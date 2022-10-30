package com.mygdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import kotlin.math.sin

class BossController (var texture: Texture){


fun updateBoss(newTexture: Texture){texture=newTexture}
var effect_time=0f
fun display (batch: SpriteBatch,delta:Float,effects:List<Effect>){
    when(effect)
    if (effects.size>0){
        batch.draw(
            effects.get(0).,
            1050f,
            750f,
            texture.width/4f,
            texture.height/4f,
        )
        effects.get(0).time-=delta
        if (effects.get(0).time<=0) effects.drop(0);
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
