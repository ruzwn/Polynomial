package components

import graphics.*
import math.polynomial.Newton
import math.polynomial.Point
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
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
    
    private val pointsIsPaintCheckBox = JCheckBox("Скрыть точки")
    private val functionIsPaintCheckBox = JCheckBox("Скрыть функцию")
    private val derivativeIsPaintCheckBox = JCheckBox("Скрыть производную")
    
    private val graphicsPanel: GraphicsPanel
    private val controlPanel: ControlPanel
    
    private val pnlColorForPoints: JPanel
    private val pnlColorForFunction: JPanel
    private val pnlColorForDerivative: JPanel
    
    private val crtPainter: CartesianPainter
    private val pointPainter: PointPainter
    private val funcPainter: FunctionPainter
    private val derivativePainter: FunctionPainter
    private var polynomial: Newton? = null
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
        funcPainter = FunctionPainter(crtPlane, null, functionDefaultColor)
        derivativePainter = FunctionPainter(crtPlane, null, derivativeDefaultColor)
        
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
                    funcPainter.color = it
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
                    derivativePainter.color = it
                    graphicsPanel.repaint()
                }
            }
        })
        
        pointsIsPaintCheckBox.addChangeListener {
            pointPainter.isPaint = !pointPainter.isPaint
            graphicsPanel.repaint()
        }
        functionIsPaintCheckBox.addChangeListener {
            funcPainter.isPaint = !funcPainter.isPaint
            graphicsPanel.repaint()
        }
        derivativeIsPaintCheckBox.addChangeListener {
            derivativePainter.isPaint = !derivativePainter.isPaint
            graphicsPanel.repaint()
        }
        
        
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
        graphicsPanel.addPainter(derivativePainter)

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

                    if (funcPainter.function == null) {
                        polynomial = Newton(mutableMapOf(Pair(xCrt, yCrt)))
                        funcPainter.function = polynomial?.let { it::invoke }//{ x -> polynomial?.invoke(x) ?: 0.0 }
                        derivativePainter.function = { x -> polynomial?.getDerivative()?.invoke(x) ?: 0.0}
                    }
                    else {
                        polynomial?.addNode(xCrt, yCrt)
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
                        polynomial?.removeNode(index)
                    }
                    
                    if (pointsOnScreen.isEmpty()) {
                        funcPainter.function = null
                        derivativePainter.function = null
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
                .addComponent(graphicsPanel, minSize.height, GROW, GROW)
                .addGap(4)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(
                            controlPanel,
                            GROW,
                            GROW,
                            GROW
                        )
                        .addGap(4)
                        .addGroup(gl.createSequentialGroup()
                        .addGroup(gl.createParallelGroup()
                            .addComponent(
                                pointsIsPaintCheckBox,
                                GROW,
                                GROW,
                                GROW
                            )
                            .addGap(4)
                            .addComponent(pnlColorForPoints, 20, 20, 20)
                            .addGap(4)
                            .addComponent(
                                pointsColorChooserLabel,
                                SHRINK,
                                SHRINK,
                                SHRINK
                            )
                        )
                        .addGap(4)
                        .addGroup(gl.createParallelGroup()
                            .addComponent(
                                functionIsPaintCheckBox,
                                GROW,
                                GROW,
                                GROW
                            )
                            .addGap(4)
                            .addComponent(pnlColorForFunction, 20, 20, 20)
                            .addGap(4)
                            .addComponent(
                                functionColorChooserLabel,
                                SHRINK,
                                SHRINK,
                                SHRINK
                            )
                        )
                        .addGap(4)
                        .addGroup(gl.createParallelGroup()
                            .addComponent(
                                derivativeIsPaintCheckBox,
                                GROW,
                                GROW,
                                GROW
                            )
                            .addGap(4)
                            .addComponent(pnlColorForDerivative, 20, 20, 20)
                            .addGap(4)
                            .addComponent(
                                derivativeColorChooserLabel,
                                SHRINK,
                                SHRINK,
                                SHRINK
                            )
                        )
                        )
                )
                .addGap(4)
        )
        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(graphicsPanel, minSize.width, GROW, GROW)
                        .addGroup(
                            gl.createSequentialGroup()
                                .addComponent(
                                    controlPanel,
                                    GROW,
                                    GROW,
                                    GROW
                                )
                                .addGap(4)
                                .addGroup(gl.createParallelGroup()
                                .addGroup(gl.createSequentialGroup()
                                    .addComponent(
                                        pointsIsPaintCheckBox,
                                        GROW,
                                        GROW,
                                        GROW
                                    )
                                    .addGap(4)
                                    .addComponent(pnlColorForPoints, 20, 20, 20)
                                    .addGap(4)
                                    .addComponent(
                                        pointsColorChooserLabel,
                                        SHRINK,
                                        SHRINK,
                                        SHRINK
                                    )
                                )
                                .addGap(4)
                                .addGroup(gl.createSequentialGroup()
                                    .addComponent(
                                        functionIsPaintCheckBox,
                                        GROW,
                                        GROW,
                                        GROW
                                    )
                                    .addGap(4)
                                    .addComponent(pnlColorForFunction, 20, 20, 20)
                                    .addGap(4)
                                    .addComponent(
                                        functionColorChooserLabel,
                                        SHRINK,
                                        SHRINK,
                                        SHRINK
                                    )
                                )
                                .addGroup(gl.createSequentialGroup()
                                    .addComponent(
                                        derivativeIsPaintCheckBox,
                                        GROW,
                                        GROW,
                                        GROW
                                    )
                                    .addGap(4)
                                    .addComponent(pnlColorForDerivative, 20, 20, 20)
                                    .addGap(4)
                                    .addComponent(
                                        derivativeColorChooserLabel, 
                                        SHRINK, 
                                        SHRINK, 
                                        SHRINK
                                    )
                                )
                        )
                        )
                )
                .addGap(4)
        )
        layout = gl
        pack()
    }

    companion object{
        val SHRINK = GroupLayout.PREFERRED_SIZE
        val GROW = GroupLayout.DEFAULT_SIZE
    }
}