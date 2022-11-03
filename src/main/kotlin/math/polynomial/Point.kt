package math.polynomial

import kotlin.math.abs

class Point(
    val xScr: Int,
    val yScr: Int,
    val xCrt: Double,
    val yCrt: Double,
    // todo: убрать radiusInPixels из этого класса?
    val radiusInPixels: Int
) {
    override fun equals(other: Any?): Boolean {
        if (other is Point) {
            if (
                abs(xScr - other.xScr) <= radiusInPixels
                && abs(yScr - other.yScr) <= radiusInPixels
            ) {
                return true
            }

            return false
        }

        return false
    }

    override fun hashCode(): Int {
        var result = xCrt.hashCode()
        result = 31 * result + yCrt.hashCode()
        return result
    }
}