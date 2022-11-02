package math.polynomial

import kotlin.math.abs

class Point(
    val xScr: Int,
    val yScr: Int,
    val xCrt: Double,
    val yCrt: Double,
) {
    private val _radiusInPixels: Int = 5

    override fun equals(other: Any?): Boolean {
        if (other is Point) {
            if (
                abs(xScr - other.xScr) <= _radiusInPixels
                && abs(yScr - other.yScr) <= _radiusInPixels
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