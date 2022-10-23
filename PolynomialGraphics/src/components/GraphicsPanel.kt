package components

import graphics.Painter
import java.awt.Graphics
import javax.swing.JPanel

class GraphicsPanel : JPanel() {
    private var painters: MutableList<Painter> = mutableListOf()

    fun addPainter(painter: Painter){
        painters.add(painter)
    }

    fun removePainter(painter: Painter){
        painters.remove(painter)
    }

    fun addPainters(painters: List<Painter>){
        this.painters.addAll(painters)
    }

    override fun paint(gr: Graphics?) {
        super.paint(gr)
        gr?.let{ g -> painters.forEach { it.paint(g) } }
    }
}