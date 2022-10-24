package graphics

data class CartesianPlaneOnScreen(
    var realWidth: Int,
    var realHeight: Int,
    var xMin: Double,
    var xMax: Double,
    var yMin: Double,
    var yMax: Double
) {
    val width: Int
        get() = realWidth - 1
    val height: Int
        get() = realHeight - 1
    // Кол-во пикселей одной декартовой единицы по Ox на экране
    val xUnit: Double
        get() = width.toDouble() / (xMax - xMin)
    // Кол-во пикселей одной декартовой единицы по Oy на экране
    val yUnit: Double
        get() = height.toDouble() / (yMax - yMin)
}
