package components

import graphics.CartesianPainter
import graphics.CrtPlaneOnScreen
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.GroupLayout
import javax.swing.JFrame

class MainWindow : JFrame() {
    private val minSize = Dimension(550, 400)
    private val mainPanel: GraphicsPanel

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(minSize.width + 200, minSize.height + 400)
        mainPanel = GraphicsPanel()
        mainPanel.background = Color.WHITE

        val gl = GroupLayout(contentPane)
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(mainPanel, minSize.height, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
        )
        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addComponent(mainPanel, minSize.width, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
        )
        layout = gl
        pack()

        val plane = CrtPlaneOnScreen(
            mainPanel.width,
            mainPanel.height,
            -7.5,
            3.09,
            -4.8,
            8.3
//            controlPanel.smXMin.number.toDouble(),
//            controlPanel.smXMax.number.toDouble(),
//            controlPanel.smYMin.number.toDouble(),
//            controlPanel.smYMax.number.toDouble()
        )
        val crtPainter = CartesianPainter(mainPanel.width, mainPanel.height, plane)
        mainPanel.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                crtPainter.plane.realWidth = mainPanel.width
                crtPainter.plane.realHeight = mainPanel.height
            }
        })
        mainPanel.addPainter(crtPainter)
    }
}