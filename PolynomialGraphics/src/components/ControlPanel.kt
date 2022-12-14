package components

import javax.swing.*
import javax.swing.border.EtchedBorder

class ControlPanel : JPanel(){

    private val lXMin: JLabel
    private val lXMax: JLabel
    private val lYMin: JLabel
    private val lYMax: JLabel

    private val sXMin: JSpinner
    private val sXMax: JSpinner
    private val sYMin: JSpinner
    private val sYMax: JSpinner

    val smXMin: SpinnerNumberModel
    val smXMax: SpinnerNumberModel
    val smYMin: SpinnerNumberModel
    val smYMax: SpinnerNumberModel

    companion object{
        private const val MIN_SZ = GroupLayout.PREFERRED_SIZE
        private const val MAX_SZ = GroupLayout.DEFAULT_SIZE
    }

    private val valChangeListeners = mutableListOf<()->Unit>()

    init{
        border = EtchedBorder()

        lXMin = JLabel()
        lXMax = JLabel()
        lYMin = JLabel()
        lYMax = JLabel()
        lXMin.text = "xMin:"
        lXMax.text = "xMax:"
        lYMin.text = "yMin:"
        lYMax.text = "yMax:"

        smXMin = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        smXMax = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)
        smYMin = SpinnerNumberModel(-5.0, -100.0, 4.9, 0.1)
        smYMax = SpinnerNumberModel(5.0, -4.9, 100.0, 0.1)

        smXMin.addChangeListener{
            smXMax.minimum = smXMin.number.toDouble() + 0.1
            valChangeListeners.forEach { it() }
        }
        smXMax.addChangeListener{
            smXMin.maximum = smXMax.number.toDouble() - 0.1
            valChangeListeners.forEach { it() }
        }
        smYMin.addChangeListener{
            smYMax.minimum = smYMin.number.toDouble() + 0.1
            valChangeListeners.forEach { it() }
        }
        smYMax.addChangeListener{
            smYMin.maximum = smYMax.number.toDouble() - 0.1
            valChangeListeners.forEach { it() }
        }

        sXMin = JSpinner(smXMin)
        sXMax = JSpinner(smXMax)
        sYMin = JSpinner(smYMin)
        sYMax = JSpinner(smYMax)

        val gl = GroupLayout(this)
        gl.setVerticalGroup(gl.createSequentialGroup()
            .addGap(4)
            .addGroup(
                gl.createParallelGroup()
                    .addComponent(lXMin, MIN_SZ, MIN_SZ, MIN_SZ)
                    .addComponent(sXMin, MIN_SZ, MIN_SZ, MIN_SZ)
                    .addComponent(lXMax, MIN_SZ, MIN_SZ, MIN_SZ)
                    .addComponent(sXMax, MIN_SZ, MIN_SZ, MIN_SZ)
            )
            .addGap(8)
            .addGroup(
                gl.createParallelGroup()
                    .addComponent(lYMin, MIN_SZ, MIN_SZ, MIN_SZ)
                    .addComponent(sYMin, MIN_SZ, MIN_SZ, MIN_SZ)
                    .addComponent(lYMax, MIN_SZ, MIN_SZ, MIN_SZ)
                    .addComponent(sYMax, MIN_SZ, MIN_SZ, MIN_SZ)
            )
            .addGap(4)
        )

        gl.setHorizontalGroup(
            gl.createSequentialGroup()
                .addGap(4)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(lXMin, MIN_SZ, MIN_SZ, MIN_SZ)
                        .addComponent(lYMin, MIN_SZ, MIN_SZ, MIN_SZ)
                )
                .addGap(2)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(sXMin, MIN_SZ, 100, MAX_SZ)
                        .addComponent(sYMin, MIN_SZ, 100, MAX_SZ)
                )
                .addGap(8, 8, Int.MAX_VALUE)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(lXMax, MIN_SZ, MIN_SZ, MIN_SZ)
                        .addComponent(lYMax, MIN_SZ, MIN_SZ, MIN_SZ)
                )
                .addGap(2)
                .addGroup(
                    gl.createParallelGroup()
                        .addComponent(sXMax, MIN_SZ, 100, MAX_SZ)
                        .addComponent(sYMax, MIN_SZ, 100, MAX_SZ)
                )
                .addGap(4)
        )
        layout = gl
    }

    fun addValChangeListener(l: ()->Unit){
        valChangeListeners.add(l)
    }

    fun removeValChangeListener(l: ()->Unit){
        valChangeListeners.remove(l)
    }
}