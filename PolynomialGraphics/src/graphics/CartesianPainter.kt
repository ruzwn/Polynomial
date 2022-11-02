package graphics

import java.awt.Graphics


class CartesianPainter(override var width: Int, override var height: Int, val plane: CrtPlaneOnScreen): Painter {
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
        val strokesOnOxCount = (plane.xMax - plane.xMin).toInt()
        val xMin = plane.xMin.toInt()
        for (i in 0..strokesOnOxCount) {
            val numNearStroke = xMin + i
            if (numNearStroke == 0) {
                continue
            }

            gr.drawString(
                "$numNearStroke",
                (plane.xUnit * i + CrtConverter.xFromCrtToScr(plane.xMin.toInt().toDouble(), plane) - 0.3).toInt(),
                CrtConverter.yFromCrtToScr(0.35, plane)
            )
        }
        // Oy
        val strokesOnOyCount = (plane.yMax - plane.yMin).toInt()
        val yMax = plane.yMax.toInt()
        for (i in 0..strokesOnOyCount) {
            val numNearStroke = yMax - i
            if (numNearStroke == 0) {
                gr.drawString(
                    "$numNearStroke",
                    CrtConverter.xFromCrtToScr(-0.35, plane),
                    (plane.yUnit * i + CrtConverter.yFromCrtToScr(plane.xMin.toInt().toDouble() + 0.3, plane)).toInt(),
                )
                continue
            }

            gr.drawString(
                "$numNearStroke",
                CrtConverter.xFromCrtToScr(-0.35, plane),
                (plane.yUnit * i + CrtConverter.yFromCrtToScr(plane.xMin.toInt().toDouble(), plane)).toInt(),
            )
        }
    }

    private fun drawStrokesOnAxes(gr: Graphics) {
        // Ox
        val strokesOnOxCount = (plane.xMax - plane.xMin).toInt()
        for (i in 0..strokesOnOxCount) {
            gr.drawLine(
                (CrtConverter.xFromCrtToScr((plane.xMin + i).toInt().toDouble(), plane)),
                CrtConverter.yFromCrtToScr(-0.2, plane),
                (CrtConverter.xFromCrtToScr((plane.xMin + i).toInt().toDouble(), plane)),
                CrtConverter.yFromCrtToScr(0.2, plane)
            )
        }
        // Oy
        val strokesOnOyCount = (plane.yMax - plane.yMin).toInt()
        for (i in 0..strokesOnOyCount) {
            gr.drawLine(
                CrtConverter.xFromCrtToScr(-0.2, plane),
                (CrtConverter.yFromCrtToScr((plane.yMax - i).toInt().toDouble(), plane)),
                CrtConverter.xFromCrtToScr(0.2, plane),
                (CrtConverter.yFromCrtToScr((plane.yMax - i).toInt().toDouble(), plane)),
            )
        }
    }

    private fun drawAxes(gr: Graphics) {
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

//        // Точка слева посередине
//        gr.drawOval(0, CrtConverter.yFromCrtToScr(0.0, plane), 5, 5)
//        // Справа посередине
//        gr.drawOval(plane.width, CrtConverter.yFromCrtToScr(0.0, plane), 5, 5)
//        // Посередине сверху
//        gr.drawOval(CrtConverter.xFromCrtToScr(0.0, plane), 0, 5, 5)
//        // Посередине снизу
//        gr.drawOval(CrtConverter.xFromCrtToScr(0.0, plane), plane.height, 5, 5)
    }
}