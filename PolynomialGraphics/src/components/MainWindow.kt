package components

import graphics.*
import math.polynomial.Newton
import math.polynomial.Point
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JFrame
import kotlin.math.abs


class MainWindow : JFrame() {
    private val minSize = Dimension(550, 400)
    private val graphicsPanel: GraphicsPanel
    private val controlPanel: ControlPanel
    private val colorPaletteForPoints: JColorChooser
    private val colorPaletteForFunction: JColorChooser
    private val colorPaletteForDerivative: JColorChooser
    private val pointRadiusInPixels: Int = 5
    private val pointColor: Color = Color.RED
    
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(minSize.width + 200, minSize.height + 400)
        graphicsPanel = GraphicsPanel()
        graphicsPanel.background = Color.WHITE
        controlPanel = ControlPanel()
        
        colorPaletteForPoints = JColorChooser()
        colorPaletteForFunction = JColorChooser()
        colorPaletteForDerivative = JColorChooser()
        val button = JButton("Choose color")
        button.addActionListener {
            colorPaletteForPoints.setBounds(button.x, button.y + 20, 600, 300)
            colorPaletteForPoints.isVisible = true
            contentPane.add(colorPaletteForPoints)
            contentPane.validate()
            contentPane.repaint()
        }
        button.setBounds(10, 11, 150, 23)
        contentPane.add(button)


        val gl = GroupLayout(contentPane)

        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(graphicsPanel, minSize.height, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
//                .addComponent(
//                    controlPanel,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE
//                )
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(
                            controlPanel,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addGap(4)
                        .addComponent(
                            button,
                            GroupLayout.DEFAULT_SIZE,
                            GroupLayout.DEFAULT_SIZE,
                            GroupLayout.DEFAULT_SIZE
                        )
                )
                .addGap(4)
//                .addComponent(
//                    button,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE
//                )
//                .addGap(4)
//                .addComponent(
//                    colorPaletteForPoints,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE
//                )
//                .addGap(4)
//                .addComponent(
//                    colorPaletteForFunction,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE,
//                    GroupLayout.PREFERRED_SIZE
//                )
        )

        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(graphicsPanel, minSize.width, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                        .addGroup(
                            gl.createSequentialGroup()
                                .addComponent(
                                    controlPanel,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE
                                )
                                .addComponent(
                                    button,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE
                                )
                        )
//                        .addComponent(
//                            controlPanel,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE
//                        )
//                        .addComponent(
//                            button,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE
//                        )
//                        .addComponent(
//                            colorPaletteForPoints,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE
//                        )
//                        .addComponent(
//                            colorPaletteForFunction,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE,
//                            GroupLayout.DEFAULT_SIZE
//                        )
                )
                .addGap(4)
        )

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

        var funcPainter: FunctionPainter? = null
        
        // To simplify removing points from polynomial
        val pointsOnScreen = mutableListOf<Pair<Int, Int>>()
        
        graphicsPanel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (e == null) {
                    return
                }

                val xCrt = CrtConverter.xFromScrToCrt(e.x, crtPlane)
                val yCrt = CrtConverter.yFromScrToCrt(e.y, crtPlane)
                
                if (e.button == MouseEvent.BUTTON1) {
                    val points = pointPainter.points
                    if (points.any { p ->
                            abs(p.xScr - e.x) <= p.radiusInPixels
                        }) {
                        return
                    }
                    
                    pointsOnScreen.add(Pair(e.x, e.y))
                    
                    pointPainter.addPoint(
                        Point(
                            e.x,
                            e.y,
                            xCrt,
                            yCrt,
                            pointRadiusInPixels
                        )
                    )

                    if (funcPainter == null) {
                        val p = Newton(mutableMapOf(Pair(xCrt, yCrt)))
                        funcPainter = FunctionPainter(crtPlane, p, Color.GREEN, Color.BLUE)
                        graphicsPanel.addPainter(funcPainter!!)
                    }
                    else {
                        funcPainter!!.polynomial.addNode(
                            xCrt,
                            yCrt
                        )
                    }
                }

                if (e.button == MouseEvent.BUTTON3) {
                    pointPainter.removePoint(
                        Point(
                            e.x,
                            e.y,
                            xCrt,
                            yCrt,
                            pointRadiusInPixels
                        )
                    )
                    
                    val point = pointsOnScreen.find { p ->
                        abs(p.first - e.x) <= pointRadiusInPixels && abs(p.second - e.y) <= pointRadiusInPixels
                    }
                    if (point != null) {
                        val index = pointsOnScreen.indexOf(point)
                        pointsOnScreen.removeAt(index)
                        funcPainter?.polynomial?.removeNode(index)
                        if (pointsOnScreen.isEmpty()) {
                            funcPainter?.let { graphicsPanel.removePainter(it) }
                            funcPainter = null
                        }
                    }
                }

                graphicsPanel.repaint()
            }
        })
    }
}