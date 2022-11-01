package com.mygdx.game

data class ResourcePackage(
    var triangles: Int = 0,
    var circles: Int = 0,
    var pentas: Int = 0,
) {
    operator fun plusAssign(other: ResourcePackage) {
        triangles += other.triangles
        circles += other.circles
        pentas += other.pentas
    }

    operator fun minusAssign(other: ResourcePackage) {
        triangles -= other.triangles
        circles -= other.circles
        pentas -= other.pentas
    }

    operator fun contains(other: ResourcePackage): Boolean {
        return triangles >= other.triangles && circles >= other.circles && pentas >= other.pentas
    }

    operator fun times(amount: Int) =
        ResourcePackage(
            triangles = triangles * amount,
            circles = circles * amount,
            pentas = pentas * amount,
        )

    operator fun div(other: ResourcePackage) =
        minOf(
            other.triangles.let { if (it == 0) Int.MAX_VALUE else triangles / it },
            other.circles.let { if (it == 0) Int.MAX_VALUE else circles / it },
            other.pentas.let { if (it == 0) Int.MAX_VALUE else pentas / it },
        )

    fun negative():Boolean{
        return triangles<0||circles<0||pentas<0
    }

    override fun toString(): String {
        return "△ $triangles | ◯ $circles | ⬠ $pentas"
    }
}
