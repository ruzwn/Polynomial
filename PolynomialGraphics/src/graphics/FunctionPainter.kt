package graphics

import java.awt.Color
import java.awt.Graphics

class FunctionPainter(
    val plane: CrtPlaneOnScreen,
    var function: ((x: Double) -> Double)?,
    var color: Color,
    var isPaint: Boolean = true
    ): Painter {
    
    override fun paint(gr: Graphics?) {
        if (gr == null) {
            return
        }
        
        if (function == null) {
            return
        }

        if (isPaint) {
            val prevColor = gr.color
            gr.color = color
            for (i in 0..plane.realWidth) {
                val x1Scr = i
                val x1Crt = CrtConverter.xFromScrToCrt(x1Scr, plane)
                val y1Crt = function?.invoke(x1Crt) ?: break
                val y1Scr = CrtConverter.yFromCrtToScr(y1Crt, plane)

                val x2Scr = i + 1
                val x2Crt = CrtConverter.xFromScrToCrt(x2Scr, plane)
                val y2Crt = function?.invoke(x2Crt) ?: break
                val y2Scr = CrtConverter.yFromCrtToScr(y2Crt, plane)

                gr.drawLine(x1Scr, y1Scr, x2Scr, y2Scr)
            }

            gr.color = prevColor
        }
    }
}