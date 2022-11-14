package components

import graphics.*
import math.polynomial.Newton
import math.polynomial.Point
import java.awt.Color
import java.awt.Dimension
import java.awt.event.*
import javax.swing.*
import kotlin.math.abs


class MainWindow : JFrame() {
    private val minSize = Dimension(550, 400)
    private val pointDefaultRadiusInPixels: Int = 5
    private val pointDefaultColor: Color = Color.RED
    private val functionDefaultColor: Color = Color.GREEN
    private val derivativeDefaultColor: Color = Color.BLUE
    
    private val pointsColorChooserLabel = JLabel("Цвет точек")
    private val functionColorChooserLabel = JLabel("Цвет функции")
    private val derivativeColorChooserLabel = JLabel("Цвет производной")
    
    private val graphicsPanel: GraphicsPanel
    private val controlPanel: ControlPanel
    
    private val pnlColorForPoints: JPanel
    private val pnlColorForFunction: JPanel
    private val pnlColorForDerivative: JPanel
    
    private val crtPainter: CartesianPainter
    private val pointPainter: PointPainter
    private var funcPainter: FunctionPainter
    
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(minSize.width + 200, minSize.height + 400)
        
        graphicsPanel = GraphicsPanel()
        graphicsPanel.background = Color.WHITE
        
        controlPanel = ControlPanel()

        val crtPlane = CrtPlaneOnScreen(
            graphicsPanel.width, graphicsPanel.height,
            controlPanel.smXMin.number.toDouble(),
            controlPanel.smXMax.number.toDouble(),
            controlPanel.smYMin.number.toDouble(),
            controlPanel.smYMax.number.toDouble()
        )

        crtPainter = CartesianPainter(crtPlane)
        pointPainter = PointPainter(crtPlane, 2 * pointDefaultRadiusInPixels, pointDefaultColor)
        funcPainter = FunctionPainter(crtPlane, null, functionDefaultColor, derivativeDefaultColor)
        
        pnlColorForPoints = JPanel()
        pnlColorForPoints.background = pointDefaultColor
        pnlColorForPoints.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                JColorChooser.showDialog(
                    this@MainWindow,
                    "Выбор цвета точек",
                    pnlColorForPoints.background
                )?.let{
                    pnlColorForPoints.background = it
                    pointPainter.color = it
                    graphicsPanel.repaint()
                }
            }
        })
        pnlColorForFunction = JPanel()
        pnlColorForFunction.background = functionDefaultColor
        pnlColorForFunction.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                JColorChooser.showDialog(
                    this@MainWindow,
                    "Выбор цвета функции",
                    pnlColorForFunction.background
                )?.let { 
                    pnlColorForFunction.background = it
                    funcPainter.polynomialColor = it
                    graphicsPanel.repaint()
                }
            }
        })
        pnlColorForDerivative = JPanel()
        pnlColorForDerivative.background = derivativeDefaultColor
        pnlColorForDerivative.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                JColorChooser.showDialog(
                    this@MainWindow,
                    "Выбор цвета производной",
                    pnlColorForDerivative.background
                )?.let {
                    pnlColorForDerivative.background = it
                    funcPainter.derivativeColor = it
                    graphicsPanel.repaint()
                }
            }
        })
        
        
        // Add components and their listeners to GraphicsPanel and ControlPanel

        
        controlPanel.addValChangeListener {
            crtPainter.plane.xMin = controlPanel.smXMin.number.toDouble()
            crtPainter.plane.xMax = controlPanel.smXMax.number.toDouble()
            crtPainter.plane.yMin = controlPanel.smYMin.number.toDouble()
            crtPainter.plane.yMax = controlPanel.smYMax.number.toDouble()
            graphicsPanel.repaint()
        }

        graphicsPanel.addPainter(crtPainter)
        graphicsPanel.addPainter(pointPainter)
        graphicsPanel.addPainter(funcPainter)

        graphicsPanel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                crtPainter.plane.realWidth = graphicsPanel.width
                crtPainter.plane.realHeight = graphicsPanel.height
            }
        })
        
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
                            pointDefaultRadiusInPixels
                        )
                    )

                    if (funcPainter.polynomial == null) {
                        funcPainter.polynomial = Newton(mutableMapOf(Pair(xCrt, yCrt)))
                    }
                    else {
                        funcPainter.polynomial!!.addNode(xCrt, yCrt)
                    }
                }

                if (e.button == MouseEvent.BUTTON3) {
                    pointPainter.removePoint(
                        Point(
                            e.x,
                            e.y,
                            xCrt,
                            yCrt,
                            pointDefaultRadiusInPixels
                        )
                    )
                    
                    val point = pointsOnScreen.find { p ->
                        abs(p.first - e.x) <= pointDefaultRadiusInPixels && abs(p.second - e.y) <= pointDefaultRadiusInPixels
                    }
                    if (point != null) {
                        val index = pointsOnScreen.indexOf(point)
                        pointsOnScreen.removeAt(index)
                        funcPainter.polynomial?.removeNode(index)
                    }
                }

                graphicsPanel.repaint()
            }
        })

        
        // Initializing layout of the main window
        
        
        val gl = GroupLayout(contentPane)
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(graphicsPanel, minSize.height, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(
                            controlPanel,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addGap(4)
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(pointsColorChooserLabel, 50, 50, 100)
                            .addComponent(pnlColorForPoints, 20, 20, 20)
                        )
//                        .addComponent(pnlColorForPoints, 20, 20, 20)
                        .addGap(4)
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(functionColorChooserLabel, 50, 50, 100)
                            .addComponent(pnlColorForFunction, 20, 20, 20)
                        )
//                        .addComponent(pnlColorForFunction, 20, 20, 20)
                        .addGap(4)
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(derivativeColorChooserLabel, 50, 50, 100)
                            .addComponent(pnlColorForDerivative, 20, 20, 20)
                        )
//                        .addComponent(pnlColorForDerivative, 20, 20, 20)
                        .addGap(4)
                )
                .addGap(4)
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
                                .addGap(4)
                                .addGroup(gl.createParallelGroup()
                                    .addComponent(pointsColorChooserLabel, 50, 50, 100)
                                    .addComponent(pnlColorForPoints, 20, 20, 20)
                                )
//                                .addComponent(pnlColorForPoints, 20, 20, 20)
                                .addGap(4)
                                .addGroup(gl.createParallelGroup()
                                    .addComponent(functionColorChooserLabel, 50, 50, 100)
                                    .addComponent(pnlColorForFunction, 20, 20, 20)
                                )
//                                .addComponent(pnlColorForFunction, 20, 20, 20)
                                .addGap(4)
                                .addGroup(gl.createParallelGroup()
                                    .addComponent(derivativeColorChooserLabel, 50, 50, 100)
                                    .addComponent(pnlColorForDerivative, 20, 20, 20)
                                )
//                                .addComponent(pnlColorForDerivative, 20, 20, 20)
                                .addGap(4)
                        )
                )
                .addGap(4)
        )
        layout = gl
        pack()
    }
}