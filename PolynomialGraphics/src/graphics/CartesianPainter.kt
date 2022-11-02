package graphics

import math.polynomial.eq
import math.polynomial.geq
import math.polynomial.leq
import java.awt.Graphics


// todo: сделать вывод нуля, когда ось Ox исчезает
// todo: избавиться от + 0.4
class CartesianPainter(val plane: CrtPlaneOnScreen): Painter {
    private val _indentInPixelForLongestStroke: Int = 18
    private val _indentInPixelForNumber: Int = 30

    override fun paint(gr: Graphics?) {
        if (gr == null) {
            return
        }

        drawAxes(gr)
        drawStrokesOnAxes(gr)
        drawNumbersNearStrokes(gr)
    }

    private fun drawNumbersNearStrokes(gr: Graphics) {
        // Ox
        // todo: + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOxCount = (plane.xMax - plane.xMin + 0.4).toInt()
        val xMin = plane.xMin.toInt()
        val yForOxAxe =
            if (plane.yMin geq 0.0) plane.yMin
            else if (plane.yMax leq 0.0) plane.yMax
            else 0.0
        var yForOxAxeOnScreen: Int = CrtConverter.yFromCrtToScr(yForOxAxe, plane)
        if (yForOxAxe eq plane.yMin) {
            yForOxAxeOnScreen -= _indentInPixelForNumber
        }
        else {
            yForOxAxeOnScreen += _indentInPixelForNumber
        }

        for (i in 0..strokesOnOxCount) {
            val numNearStroke = xMin + i
            if (numNearStroke == 0 && yForOxAxe leq 0.0) {
                continue
            }

            gr.drawString(
                "$numNearStroke",
                CrtConverter.xFromCrtToScr((xMin + i).toDouble(), plane),
                yForOxAxeOnScreen
            )
        }
        // Oy
        // + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOyCount = (plane.yMax - plane.yMin + 0.4).toInt()
        val yMax = plane.yMax.toInt()
        val xForOyAxe =
            if (plane.xMin geq 0.0) plane.xMin
            else if (plane.xMax leq 0.0) plane.xMax
            else 0.0
        var xForOyAxeOnScreen: Int = CrtConverter.xFromCrtToScr(xForOyAxe, plane)
        if (xForOyAxe eq plane.xMin) {
            xForOyAxeOnScreen += _indentInPixelForNumber
        }
        else {
            xForOyAxeOnScreen -= _indentInPixelForNumber
        }
        for (i in 0..strokesOnOyCount) {
            val numNearStroke = yMax - i
            if (numNearStroke == 0) {
                gr.drawString(
                    "$numNearStroke",
                    xForOyAxeOnScreen,
                    CrtConverter.yFromCrtToScr((yMax - i).toDouble(), plane) + _indentInPixelForNumber,
                )
                continue
            }

            gr.drawString(
                "$numNearStroke",
                xForOyAxeOnScreen,
                (CrtConverter.yFromCrtToScr((yMax - i).toDouble(), plane)),
            )
        }
    }

    private fun drawStrokesOnAxes(gr: Graphics) {
        // Ox
        // + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOxCount = (plane.xMax - plane.xMin + 0.4).toInt()
        val xMin = plane.xMin.toInt()
        val yForOxAxe =
            if (plane.yMin geq 0.0) plane.yMin
            else if (plane.yMax leq 0.0) plane.yMax
            else 0.0
        for (i in 0..strokesOnOxCount) {
            var j = 1.1
            while (j geq 0.0) {
                j -= 0.1
                if (j eq 0.5) {
                    gr.drawLine(
                        (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                        CrtConverter.yFromCrtToScr(yForOxAxe, plane) - (_indentInPixelForLongestStroke - 8),
                        (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                        CrtConverter.yFromCrtToScr(yForOxAxe, plane) + (_indentInPixelForLongestStroke - 8)
                    )
                    continue
                }

                gr.drawLine(
                    (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                    CrtConverter.yFromCrtToScr(yForOxAxe, plane) - (_indentInPixelForLongestStroke - 13),
                    (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                    CrtConverter.yFromCrtToScr(yForOxAxe, plane) + (_indentInPixelForLongestStroke - 13)
                )
            }
            gr.drawLine(
                (CrtConverter.xFromCrtToScr((xMin + i).toDouble(), plane)),
                CrtConverter.yFromCrtToScr(yForOxAxe, plane) - _indentInPixelForLongestStroke,
                (CrtConverter.xFromCrtToScr((xMin + i).toDouble(), plane)),
                CrtConverter.yFromCrtToScr(yForOxAxe, plane) + _indentInPixelForLongestStroke
            )
        }
        // Oy
        // + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOyCount = (plane.yMax - plane.yMin + 0.4).toInt()
        val yMax = plane.yMax.toInt()
        val xForOyAxe =
            if (plane.xMin geq 0.0) plane.xMin
            else if (plane.xMax leq 0.0) plane.xMax
            else 0.0
        for (i in 0..strokesOnOyCount) {
            var j = -0.1
            while (j leq 1.0) {
                j += 0.1
                if (j eq 0.5) {
                    gr.drawLine(
                        CrtConverter.xFromCrtToScr(xForOyAxe, plane) - (_indentInPixelForLongestStroke - 8),
                        (CrtConverter.yFromCrtToScr(yMax - i + j, plane)),
                        CrtConverter.xFromCrtToScr( xForOyAxe, plane) + (_indentInPixelForLongestStroke - 8),
                        (CrtConverter.yFromCrtToScr(yMax - i + j, plane))
                    )
                    continue
                }

                gr.drawLine(
                    CrtConverter.xFromCrtToScr(xForOyAxe, plane) - (_indentInPixelForLongestStroke - 13),
                    (CrtConverter.yFromCrtToScr(yMax - i + j, plane)),
                    CrtConverter.xFromCrtToScr(xForOyAxe, plane) + (_indentInPixelForLongestStroke - 13),
                    (CrtConverter.yFromCrtToScr(yMax - i + j, plane))
                )
            }
            gr.drawLine(
                CrtConverter.xFromCrtToScr(xForOyAxe, plane) - _indentInPixelForLongestStroke,
                (CrtConverter.yFromCrtToScr((yMax - i).toDouble(), plane)),
                CrtConverter.xFromCrtToScr(xForOyAxe, plane) + _indentInPixelForLongestStroke,
                (CrtConverter.yFromCrtToScr((yMax - i).toDouble(), plane)),
            )
        }
    }

    private fun drawAxes(gr: Graphics) {
        val xForOxAxe =
            if (plane.yMin geq 0.0) plane.yMin
            else if (plane.yMax leq 0.0) plane.yMax
            else 0.0
        val yForOyAxe =
            if (plane.xMin geq 0.0) plane.xMin
            else if (plane.xMax leq 0.0) plane.xMax
            else 0.0

        // Ox axe
        gr.drawLine(
            0,
            CrtConverter.yFromCrtToScr(xForOxAxe, plane),
            plane.width,
            CrtConverter.yFromCrtToScr(xForOxAxe, plane)
        )

        // Oy axe
        gr.drawLine(
            CrtConverter.xFromCrtToScr(yForOyAxe, plane),
            0,
            CrtConverter.xFromCrtToScr(yForOyAxe, plane),
            plane.height
        )
    }
}
