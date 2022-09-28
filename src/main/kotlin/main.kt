import math.polynomial.Lagrange
import math.polynomial.Newton
import math.polynomial.NewtonRecursive
import math.polynomial.Polynomial
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main() {
//    val p2 = Polynomial(3.0, 0.0, -2.0, -1.0)
//    val p3 = Polynomial(-3.0, 10.0, 5.0)
//    println("0: ${Polynomial()}")
//    println("1: ${Polynomial(1.0)}")
//    println("-1: ${Polynomial(-1.0)}")
//    println("big: ${Polynomial(0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 7.0)}")
//    println("p2: $p2")
//    println("p3: $p3")
//    println("p3+p2: ${p3 + p2}")
//    println("p3-p2: ${p3 - p2}")
//    println("p3*p2: ${p3 * p2}")
//    println("p3*2: ${p3 * 2.0}")
//    println("p3/2: ${p3 / 2.0}")
//    val p4 = Polynomial(1.0, 1.0)
//    val p5 = Polynomial(-1.0, 1.0)
//    println("p4: $p4")
//    println("p5: $p5")
//    println("p4 - p5: ${p4 - p5}\ndegree=${(p4 - p5).degree}")
//
//    val coeffs = HashMap<Double, Double>()
//    coeffs[-1.0] = 0.0
//    coeffs[0.0] = 1.0
//    coeffs[1.0] = 2.0
//    coeffs[2.0] = 9.0
//    println(Lagrange(coeffs))
//    val coeffs2 = HashMap<Double, Double>()
//    coeffs2[-1.0] = 1.0
//    coeffs2[0.0] = 0.0
//    coeffs2[1.0] = 1.0
//    println(Lagrange(coeffs2))
//    p4 += p5
//
//    println(p2(3.0))

    val coeffs3 = HashMap<Double, Double>()
    coeffs3[0.0] = -1.0
    coeffs3[1.0] = 1.0
    println(NewtonRecursive(coeffs3))
    println(NewtonRecursive(coeffs3).addNode(0.5, 1.0))
    println(Newton(coeffs3))
    println(Newton(coeffs3).addNode(0.5, 1.0))


    // BENCHMARK
    val coeffs4 = HashMap<Double, Double>()
    for (i in 0..1000) {
        coeffs4[i.toDouble()] = (-1000..1000).random().toDouble()
    }

    // calculations
    var timeLagrange = measureTimeMillis {
        Lagrange(coeffs4)
    }
    println("Lagrange: $timeLagrange")
    val newton: Newton
    var timeNewton = measureTimeMillis {
        newton = Newton(coeffs4)
    }
    println("Newton: $timeNewton")
    val timeRecursiveNewton = measureTimeMillis {
        NewtonRecursive(coeffs4)
    }
    println("Recursive Newton: $timeRecursiveNewton")

    // add node
    coeffs4[-1.0] = 312.0
    timeLagrange = measureTimeMillis {
        Lagrange(coeffs4)
    }
    println("Lagrange add node: $timeLagrange")
    timeNewton = measureTimeMillis {
        newton.addNode(-1.0, 312.0)
    }
    println("Newton add node: $timeNewton")
}
// TODO: сравнение вещественных чисел переделать, добавить сравнение?
// TODO: вычисление полином Лагранжа методом Ньютона (не надо пересчитывать полностью при добавлении узла)
// TODO: бенчмарк полинома Лагранжа и полинома Ньютона (просто и при добавлении узла)
// TODO: использовать формулу, которая не рекурсивная