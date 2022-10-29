package com.mygdx.game

data class ResourcePackage(
    var triangles: Int = 0,
    var circles: Int = 0,
    var squares: Int = 0,
) {
    operator fun plusAssign(other: ResourcePackage) {
        triangles += other.triangles
        circles += other.circles
        squares += other.squares
    }
}
