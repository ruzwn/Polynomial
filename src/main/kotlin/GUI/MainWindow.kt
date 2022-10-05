package GUI

import GUI.components.GraphicsPanel
import java.awt.Color
import java.awt.Dimension
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.JPanel

class MainWindow: JFrame() {
    private val _minSize = Dimension(600, 450)
    private val _cartesianPlane = GraphicsPanel()
    private val _inputParamsPanel = JPanel()
    private val _inputColorsOfGraphPanel = JPanel()

    init {
        size = _minSize
        defaultCloseOperation = EXIT_ON_CLOSE

        _cartesianPlane.background = Color.BLUE
        _inputParamsPanel.background = Color.GREEN
        _inputColorsOfGraphPanel.background = Color.RED

        val groupLayout = GroupLayout(contentPane)
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
            .addGap(8)
            .addComponent(_cartesianPlane, 50, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(8)
            .addComponent(_inputParamsPanel, 50, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(8)
            .addComponent(_inputColorsOfGraphPanel, 50, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(8)
        )
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
            .addGap(8)
            .addGroup(groupLayout.createParallelGroup()
                .addComponent(_cartesianPlane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
                .addComponent(_inputParamsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(_inputColorsOfGraphPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            )
            .addGap(8)
        )

        layout = groupLayout
    }
}