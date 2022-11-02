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

class MainWindow : JFrame() {
    private val minSize = Dimension(550, 400)
    private val mainPanel: GraphicsPanel
    private val controlPanel: ControlPanel
    init{
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(minSize.width+200, minSize.height+400)
        mainPanel = GraphicsPanel()
        mainPanel.background = Color.WHITE
        controlPanel = ControlPanel()
        val gl = GroupLayout(contentPane)

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addGap(4)
            .addComponent(mainPanel, minSize.height, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
            .addGap(4)
            .addComponent(controlPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(4)
        )

        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addGap(4)
            .addGroup(
                gl.createParallelGroup()
                    .addComponent(mainPanel, minSize.width, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                    .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
            )
            .addGap(4))
        layout = gl
        pack()
        val plane = CrtPlaneOnScreen(
            mainPanel.width, mainPanel.height,
            controlPanel.smXMin.number.toDouble(),
            controlPanel.smXMax.number.toDouble(),
            controlPanel.smYMin.number.toDouble(),
            controlPanel.smYMax.number.toDouble()
        )
        val dp = CartesianPainter(plane)
        mainPanel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                dp.plane.realWidth = mainPanel.width
                dp.plane.realHeight = mainPanel.height
            }
        })
        controlPanel.addValChangeListener {
            dp.plane.xMin = controlPanel.smXMin.number.toDouble()
            dp.plane.xMax = controlPanel.smXMax.number.toDouble()
            dp.plane.yMin = controlPanel.smYMin.number.toDouble()
            dp.plane.yMax = controlPanel.smYMax.number.toDouble()
            mainPanel.repaint()
        }
        mainPanel.addPainter(dp)

        val pointPainter = PointPainter(plane)
        mainPanel.addPainter(pointPainter)
        mainPanel.addMouseListener(object: MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e == null) {
                    return
                }

                if (e.button == MouseEvent.BUTTON1) {
                    pointPainter.addPoint(
                        Point(
                            e.x,
                            e.y,
                            CrtConverter.xFromScrToCrt(e.x, plane),
                            CrtConverter.yFromScrToCrt(e.y, plane)
                        )
                    )
                }
                if (e.button == MouseEvent.BUTTON3) {
                    pointPainter.removePoint(
                        Point(
                            e.x,
                            e.y,
                            CrtConverter.xFromScrToCrt(e.x, plane),
                            CrtConverter.yFromScrToCrt(e.y, plane)
                        )
                    )
                }
                mainPanel.repaint()
            }
        })
    }
}