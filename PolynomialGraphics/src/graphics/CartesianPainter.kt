package graphics

import math.polynomial.eq
import math.polynomial.geq
import math.polynomial.leq
import java.awt.Graphics


class CartesianPainter(val plane: CrtPlaneOnScreen): Painter {
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
        // + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOxCount = (plane.xMax - plane.xMin + 0.4).toInt()
        val xMin = plane.xMin.toInt()
        for (i in 0..strokesOnOxCount) {
            val numNearStroke = xMin + i
            if (numNearStroke == 0) {
                continue
            }

            gr.drawString(
                "$numNearStroke",
                (CrtConverter.xFromCrtToScr((xMin + i).toDouble(), plane) - 0.3).toInt(),
                CrtConverter.yFromCrtToScr(-0.35, plane)
            )
        }
        // Oy
        // + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOyCount = (plane.yMax - plane.yMin + 0.4).toInt()
        val yMax = plane.yMax.toInt()
        for (i in 0..strokesOnOyCount) {
            val numNearStroke = yMax - i
            if (numNearStroke == 0) {
                gr.drawString(
                    "$numNearStroke",
                    CrtConverter.xFromCrtToScr(-0.35, plane),
                    (CrtConverter.yFromCrtToScr((yMax - i).toDouble() - 0.35, plane)),
                )
                continue
            }

            gr.drawString(
                "$numNearStroke",
                CrtConverter.xFromCrtToScr(-0.35, plane),
                (CrtConverter.yFromCrtToScr((yMax - i).toDouble(), plane)),
            )
        }
    }

    private fun drawStrokesOnAxes(gr: Graphics) {
        // Ox
        // + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOxCount = (plane.xMax - plane.xMin + 0.4).toInt()
        val xMin = plane.xMin.toInt()
        for (i in 0..strokesOnOxCount) {
            var j = 1.1
            while (j geq 0.0) {
                j -= 0.1
                if (j eq 0.5) {
                    gr.drawLine(
                        (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                        CrtConverter.yFromCrtToScr(-0.15, plane),
                        (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                        CrtConverter.yFromCrtToScr(0.15, plane)
                    )
                    continue
                }

                gr.drawLine(
                    (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                    CrtConverter.yFromCrtToScr(-0.08, plane),
                    (CrtConverter.xFromCrtToScr(xMin + i - j, plane)),
                    CrtConverter.yFromCrtToScr(0.08, plane)
                )
            }
            gr.drawLine(
                (CrtConverter.xFromCrtToScr((xMin + i).toDouble(), plane)),
                CrtConverter.yFromCrtToScr(-0.2, plane),
                (CrtConverter.xFromCrtToScr((xMin + i).toDouble(), plane)),
                CrtConverter.yFromCrtToScr(0.2, plane)
            )
        }
        // Oy
        // + 0.4 чтобы при приведении к инту не потерять одну единицу
        val strokesOnOyCount = (plane.yMax - plane.yMin + 0.4).toInt()
        val yMax = plane.yMax.toInt()
        for (i in 0..strokesOnOyCount) {
            var j = -0.1
            while (j leq 1.0) {
                j += 0.1
                if (j eq 0.5) {
                    gr.drawLine(
                        CrtConverter.xFromCrtToScr(-0.15, plane),
                        (CrtConverter.yFromCrtToScr(yMax - i + j, plane)),
                        CrtConverter.xFromCrtToScr(0.15, plane),
                        (CrtConverter.yFromCrtToScr(yMax - i + j, plane))
                    )
                    continue
                }

                gr.drawLine(
                    CrtConverter.xFromCrtToScr(-0.08, plane),
                    (CrtConverter.yFromCrtToScr(yMax - i + j, plane)),
                    CrtConverter.xFromCrtToScr(0.08, plane),
                    (CrtConverter.yFromCrtToScr(yMax - i + j, plane))
                )
            }
            gr.drawLine(
                CrtConverter.xFromCrtToScr(-0.2, plane),
                (CrtConverter.yFromCrtToScr((yMax - i).toDouble(), plane)),
                CrtConverter.xFromCrtToScr(0.2, plane),
                (CrtConverter.yFromCrtToScr((yMax - i).toDouble(), plane)),
            )
        }
    }

    private fun drawAxes(gr: Graphics) {
        val xForOxAxe =
            if (plane.yMin geq 0.0) plane.yMin + 0.01
            else if (plane.yMax leq 0.0) plane.yMax - 0.01
            else 0.0
        val yForOyAxe =
            if (plane.xMin geq 0.0) plane.xMin + 0.01
            else if (plane.xMax leq 0.0) plane.xMax - 0.01
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
