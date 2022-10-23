package graphics

import java.awt.Color
import java.awt.Graphics


class CartesianPainter(override var width: Int, override var height: Int, val plane: CartesianPlaneOnScreen): Painter {
    override fun paint(gr: Graphics?) {
        drawAxes(gr)
    }

    private fun drawAxes(gr: Graphics?) {
        if (gr != null) {
            gr.color = Color.BLACK
            gr.drawLine(
//            CartesianAndScreenConverter.xFromCrtToScr(0.0, plane),
                0,
                CartesianAndScreenConverter.yFromCrtToScr(0.0, plane),
//            CartesianAndScreenConverter.xFromCrtToScr(plane.width.toDouble(), plane),
                plane.width,
                CartesianAndScreenConverter.yFromCrtToScr(0.0, plane)
            )
            gr.drawLine(
                CartesianAndScreenConverter.xFromCrtToScr(0.0, plane),
//            CartesianAndScreenConverter.yFromCrtToScr(0.0, plane),
                0,
                CartesianAndScreenConverter.xFromCrtToScr(0.0, plane),
//            CartesianAndScreenConverter.yFromCrtToScr(plane.height.toDouble(), plane),
                plane.height
            )
        }
    }
}