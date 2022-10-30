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

    operator fun minusAssign(other: ResourcePackage) {
        triangles -= other.triangles
        circles -= other.circles
        squares -= other.squares
    }

    operator fun contains(other: ResourcePackage): Boolean {
        return triangles >= other.triangles && circles >= other.circles && squares >= other.squares
    }

    operator fun times(amount: Int) =
        ResourcePackage(
            triangles = triangles * amount,
            circles = circles * amount,
            squares = squares * amount,
        )

    operator fun div(other: ResourcePackage) =
        minOf(
            other.triangles.let { if (it == 0) Int.MAX_VALUE else triangles / it },
            other.circles.let { if (it == 0) Int.MAX_VALUE else circles / it },
            other.squares.let { if (it == 0) Int.MAX_VALUE else squares / it },
        )

    fun negative():Boolean{
        return triangles<0||circles<0||squares<0
    }
}
