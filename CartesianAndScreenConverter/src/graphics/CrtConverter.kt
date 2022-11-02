package graphics

object CrtConverter {
    fun xFromCrtToScr(x: Double, plane: CrtPlaneOnScreen): Int {
        var xOnScreen = (plane.xUnit * (x - plane.xMin)).toInt()
        if (xOnScreen < -plane.width) {
            xOnScreen = -plane.width
        }
        if (xOnScreen > 2 * plane.width) {
            xOnScreen = 2 * plane.width
        }
        return xOnScreen
    }

    fun yFromCrtToScr(y: Double, plane: CrtPlaneOnScreen): Int {
        var yOnScreen = (plane.yUnit * (plane.yMax - y)).toInt()
        if (yOnScreen < -plane.height) {
            yOnScreen = -plane.height
        }
        if (yOnScreen > 2 * plane.height) {
            yOnScreen = 2 * plane.height
        }
        return yOnScreen
    }

    fun xFromScrToCrt(x: Int, plane: CrtPlaneOnScreen): Double {
        var _x = x
        if (_x < -plane.width) {
            _x = -plane.width
        }
        if (_x > 2 * plane.width) {
            _x = 2 * plane.width
        }
        return _x / plane.xUnit + plane.xMin
    }

    fun yFromScrToCrt(y: Int, plane: CrtPlaneOnScreen): Double {
        var _y = y
        if (_y < -plane.height) {
            _y = -plane.height
        }
        if (_y > 2 * plane.height) {
            _y = 2 * plane.height
        }
        return _y / plane.yUnit + plane.yMin
    }
}