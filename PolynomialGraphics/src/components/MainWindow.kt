package components

import graphics.CartesianPainter
import graphics.CrtConverter
import graphics.CrtPlaneOnScreen
import graphics.PointPainter
import math.polynomial.Point
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import javax.swing.GroupLayout
import javax.swing.JFrame
import kotlin.math.abs

class MainWindow : JFrame() {
    private val minSize = Dimension(550, 400)
    private val graphicsPanel: GraphicsPanel
    private val controlPanel: ControlPanel
    private val pointRadiusInPixels: Int = 5
    private val pointColor: Color = Color.RED
    
    init{
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(minSize.width+200, minSize.height+400)
        graphicsPanel = GraphicsPanel()
        graphicsPanel.background = Color.WHITE
        controlPanel = ControlPanel()
        val gl = GroupLayout(contentPane)

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addGap(4)
            .addComponent(graphicsPanel, minSize.height, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
            .addGap(4)
            .addComponent(controlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(4))

        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGap(4)
            .addGroup(
                gl.createParallelGroup()
                    .addComponent(graphicsPanel, minSize.width, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
            )
            .addGap(4))
        
        layout = gl
        
        pack()
        
        val crtPlane = CrtPlaneOnScreen(
            graphicsPanel.width, graphicsPanel.height,
            controlPanel.smXMin.number.toDouble(),
            controlPanel.smXMax.number.toDouble(),
            controlPanel.smYMin.number.toDouble(),
            controlPanel.smYMax.number.toDouble()
        )
        val crtPainter = CartesianPainter(crtPlane)
        
        graphicsPanel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                crtPainter.plane.realWidth = graphicsPanel.width
                crtPainter.plane.realHeight = graphicsPanel.height
            }
        })
        
        controlPanel.addValChangeListener {
            crtPainter.plane.xMin = controlPanel.smXMin.number.toDouble()
            crtPainter.plane.xMax = controlPanel.smXMax.number.toDouble()
            crtPainter.plane.yMin = controlPanel.smYMin.number.toDouble()
            crtPainter.plane.yMax = controlPanel.smYMax.number.toDouble()
            graphicsPanel.repaint()
        }
        
        graphicsPanel.addPainter(crtPainter)

        val pointPainter = PointPainter(crtPlane, 2 * pointRadiusInPixels, pointColor)
        graphicsPanel.addPainter(pointPainter)
        graphicsPanel.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e == null) {
                    return
                }

                if (e.button == MouseEvent.BUTTON1) {
                    val points = pointPainter.points
                    if (points.any { p -> 
                            abs(p.xScr - e.x) <= p.radiusInPixels 
                    }) {
                        return
                    }
                    
                    pointPainter.addPoint(
                        Point(
                            e.x,
                            e.y,
                            CrtConverter.xFromScrToCrt(e.x, crtPlane),
                            CrtConverter.yFromScrToCrt(e.y, crtPlane),
                            pointRadiusInPixels
                        )
                    )
                }
                
                if (e.button == MouseEvent.BUTTON3) {
                    pointPainter.removePoint(
                        Point(
                            e.x,
                            e.y,
                            CrtConverter.xFromScrToCrt(e.x, crtPlane),
                            CrtConverter.yFromScrToCrt(e.y, crtPlane),
                            pointRadiusInPixels
                        )
                    )
                }
                
                graphicsPanel.repaint()
            }
        })
    }
}