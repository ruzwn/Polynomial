package graphics

import java.awt.Color
import java.awt.Graphics


class CartesianPainter(override var width: Int, override var height: Int, val plane: CartesianPlaneOnScreen): Painter {
    override fun paint(gr: Graphics?) {
        drawAxes(gr)
        drawStrokesOnAxes(gr)
    }

    private fun drawStrokesOnAxes(gr: Graphics?) {
        if (gr == null) {
            return
        }

        gr.color = Color.BLACK

        // Ox
        val strokesOnOxCount = (plane.xMax - plane.xMin).toInt()
        for (i in 0..strokesOnOxCount) {
            gr.drawLine(
                (plane.xUnit * i + CrtConverter.xFromCrtToScr(plane.xMin.toInt().toDouble(), plane)).toInt(),
                CrtConverter.yFromCrtToScr(-0.2, plane),
                (plane.xUnit * i + CrtConverter.xFromCrtToScr(plane.xMin.toInt().toDouble(), plane)).toInt(),
                CrtConverter.yFromCrtToScr(0.2, plane)
            )
            gr.drawString(
                "${(CrtConverter.xFromScrToCrt((plane.xUnit * i + CrtConverter.xFromCrtToScr(plane.xMin.toInt().toDouble(), plane)).toInt(),plane) + eps(CrtConverter.xFromScrToCrt((plane.xUnit * i + CrtConverter.xFromCrtToScr(plane.xMin.toInt().toDouble(), plane)).toInt(), plane))).toInt()}",
                (plane.xUnit * i + CrtConverter.xFromCrtToScr(plane.xMin.toInt().toDouble(), plane) - 0.2).toInt(),
                CrtConverter.yFromCrtToScr(0.35, plane)
            )
        }
    }

    private fun eps(num: Double): Double {
        return if (num < 0) {
            -0.1
        } else {
            0.1
        }
    }

    private fun drawAxes(gr: Graphics?) {
        if (gr == null) {
            return
        }

        gr.color = Color.BLACK
        // Ox
        gr.drawLine(
            0,
            CrtConverter.yFromCrtToScr(0.0, plane),
            plane.width,
            CrtConverter.yFromCrtToScr(0.0, plane)
        )
        // Oy
        gr.drawLine(
            CrtConverter.xFromCrtToScr(0.0, plane),
            0,
            CrtConverter.xFromCrtToScr(0.0, plane),
            plane.height
        )

        // Точка слева посередине
        gr.color = Color.GREEN
        gr.drawOval(0, CrtConverter.yFromCrtToScr(0.0, plane), 5, 5)
        // Справа посередине
        gr.drawOval(plane.width, CrtConverter.yFromCrtToScr(0.0, plane), 5, 5)
        // Посередине сверху
        gr.drawOval(CrtConverter.xFromCrtToScr(0.0, plane), 0, 5, 5)
        // Посередине снизу
        gr.drawOval(CrtConverter.xFromCrtToScr(0.0, plane), plane.height, 5, 5)
    }
}