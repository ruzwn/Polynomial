package graphics

import math.polynomial.Point
import java.awt.Color
import java.awt.Graphics
import java.time.temporal.IsoFields

// todo: изменить тип _points?
class PointPainter(
    val plane: CrtPlaneOnScreen, 
    val diameterInPixels: Int, 
    var color: Color,
    var isPaint: Boolean = true
    ): Painter {
    private val _points: MutableCollection<Point> = MutableList(0) { 
        Point(0, 0, 0.0, 0.0, diameterInPixels / 2) 
    }
    val points: MutableList<Point>
            get() = mutableListOf(*_points.toTypedArray())
    
    override fun paint(gr: Graphics?) {
        if (!isPaint) {
            return
        }
        
        if (gr == null) {
            return
        }

        val prevColor = gr.color
        gr.color = color
        _points.forEach { point ->
            val xOnScreen: Int = CrtConverter.xFromCrtToScr(point.xCrt, plane)
            val yOnScreen: Int = CrtConverter.yFromCrtToScr(point.yCrt, plane)
            gr.fillOval(
                xOnScreen - (diameterInPixels / 2),
                yOnScreen - (diameterInPixels / 2),
                diameterInPixels,
                diameterInPixels)
        }
        gr.color = prevColor
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