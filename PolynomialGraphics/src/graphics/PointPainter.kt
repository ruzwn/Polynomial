package graphics

import math.polynomial.Point
import java.awt.Color
import java.awt.Graphics

class PointPainter(val plane: CrtPlaneOnScreen): Painter {
    private val _points: MutableList<Point> = MutableList(0) { Point(0, 0, 0.0, 0.0) }
    private val _diameterInPixels: Int = 10

    override fun paint(gr: Graphics?) {
        if (gr == null) {
            return
        }

        gr.color = Color.ORANGE
        _points.forEach { point ->
            val xOnScreen: Int = CrtConverter.xFromCrtToScr(point.xCrt, plane)
            val yOnScreen: Int = CrtConverter.yFromCrtToScr(point.yCrt, plane)
            gr.fillOval(
                xOnScreen - (_diameterInPixels / 2),
                yOnScreen - (_diameterInPixels / 2),
                _diameterInPixels,
                _diameterInPixels)
        }
        gr.color = Color.BLACK
    }

    fun addPoint(point: Point) {
        _points.add(point)
    }

    fun addPoints(points: Iterable<Point>) {
        _points.addAll(points)
    }

    fun removePoint(point: Point) {
        if (_points.contains(point)) {
            _points.remove(point)
        }
    }

    fun removePoints(points: Iterable<Point>) {
        for (point in points) {
            if (_points.contains(point)) {
                _points.remove(point)
            }
        }
    }
}