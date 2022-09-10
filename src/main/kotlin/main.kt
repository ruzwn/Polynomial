import ru.smak.math.polynomial.Polynomial

fun main() {
    val p1 = Polynomial()
    val p2 = Polynomial(3.0, 0.0, -2.0, -1.0)
    val p3 = Polynomial(-3.0, 10.0)
    println(p1)
    println(p2)
    println(p3 + p2)
}