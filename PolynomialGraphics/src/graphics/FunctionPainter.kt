package graphics

import math.polynomial.Newton
import java.awt.Color
import java.awt.Graphics

class FunctionPainter(
    val plane: CrtPlaneOnScreen, 
    val polynomial: Newton,
    val polynomialColor: Color,
    val derivativeColor: Color
    ): Painter {
    
    override fun paint(gr: Graphics?) {
        if (gr == null) {
            return
        }

        var prevColor = gr.color
        gr.color = polynomialColor
        for (i in 0..plane.realWidth) {
            val x1Scr = i
            val x1Crt = CrtConverter.xFromScrToCrt(x1Scr, plane)
            val y1Crt = polynomial(x1Crt)
            val y1Scr = CrtConverter.yFromCrtToScr(y1Crt, plane)
            
            val x2Scr = i + 1
            val x2Crt = CrtConverter.xFromScrToCrt(x2Scr, plane)
            val y2Crt = polynomial(x2Crt)
            val y2Scr = CrtConverter.yFromCrtToScr(y2Crt, plane)
            
            gr.drawLine(x1Scr, y1Scr, x2Scr, y2Scr)
        }
        
        gr.color = prevColor
        
        // Print derivative
        val derivative = polynomial.getDerivative()
        prevColor = gr.color
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