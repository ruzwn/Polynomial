package graphics

import math.polynomial.Newton
import java.awt.Color
import java.awt.Graphics

class FunctionPainter(
    val plane: CrtPlaneOnScreen, 
    var polynomial: Newton?,
    var polynomialColor: Color,
    var derivativeColor: Color,
    var isPaintFunction: Boolean = true,
    var isPaintDerivative: Boolean = true
    ): Painter {
    
    override fun paint(gr: Graphics?) {
        if (gr == null) {
            return
        }
        
        if (polynomial == null) {
            return
        }

        if (isPaintFunction) {
            val prevColor = gr.color
            gr.color = polynomialColor
            for (i in 0..plane.realWidth) {
                val x1Scr = i
                val x1Crt = CrtConverter.xFromScrToCrt(x1Scr, plane)
                // Not thread-safe because of not null assertion
                // (during the execution of the loop, the property may become null in another thread) ?
                val y1Crt = polynomial!!(x1Crt)
                val y1Scr = CrtConverter.yFromCrtToScr(y1Crt, plane)

                val x2Scr = i + 1
                val x2Crt = CrtConverter.xFromScrToCrt(x2Scr, plane)
                val y2Crt = polynomial!!(x2Crt)
                val y2Scr = CrtConverter.yFromCrtToScr(y2Crt, plane)

                gr.drawLine(x1Scr, y1Scr, x2Scr, y2Scr)
            }

            gr.color = prevColor
        }

        // Print derivative
        if (isPaintDerivative) {
            val derivative = polynomial!!.getDerivative()
            val prevColor = gr.color
            gr.color = derivativeColor
            for (i in 0..plane.realWidth) {
                val x1Scr = i
                val x1Crt = CrtConverter.xFromScrToCrt(x1Scr, plane)
                val y1Crt = derivative(x1Crt)
                val y1Scr = CrtConverter.yFromCrtToScr(y1Crt, plane)

                val x2Scr = i + 1
                val x2Crt = CrtConverter.xFromScrToCrt(x2Scr, plane)
                val y2Crt = derivative(x2Crt)
                val y2Scr = CrtConverter.yFromCrtToScr(y2Crt, plane)

                gr.drawLine(x1Scr, y1Scr, x2Scr, y2Scr)
            }

            gr.color = prevColor
        }
    }
}