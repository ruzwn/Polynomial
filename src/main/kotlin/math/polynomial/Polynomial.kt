package math.polynomial

import java.lang.StringBuilder

// TODO: добавить деление многочленов (доп. баллы)
class Polynomial constructor(vararg coeffs: Double) {
    private val _coeff: MutableList<Double>
    val coeff: List<Double>
        get() = _coeff.toList()
    val degree: Int
        get() = _coeff.size - 1

    init {
        // Если, например, Polynomial(1.0, 4.0, 0.0), то степень будет 1, а не 2
        val temp = coeffs.toMutableList()
        var tempDegree = 0
        for (i in temp.count() - 1 downTo  0) {
            if (temp[i] != 0.0) {
                tempDegree = i
                break
            }
        }
        _coeff = MutableList(tempDegree + 1, init = { i -> temp[i] })
    }
    constructor(): this(0.0)

    override fun toString(): String {
        val strBuilder = StringBuilder()
        _coeff.asReversed().forEachIndexed { i, v ->
            if (v != 0.0) {
                if (i > 0) {
                    strBuilder.append("+")
                }
                strBuilder.append("($v)x^${_coeff.size - i - 1}")
            }
            if (strBuilder.isEmpty()) {
                strBuilder.append(0.0)
            }
        }
        return strBuilder.toString()
    }

    operator fun plus(other: Polynomial) : Polynomial {
        val (min, max) =
            if (degree < other.degree) {
                arrayOf(coeff, other.coeff)
            }
            else {
                arrayOf(other.coeff, coeff)
            }
        val res = max.toDoubleArray()
        min.forEachIndexed { i, v ->
            res[i] += v
        }
        return Polynomial(*res)
    }

    operator fun minus(other: Polynomial) : Polynomial {
        return this + (other * -1.0)
    }

    operator fun times(other: Polynomial) : Polynomial {
        val res = DoubleArray(degree + other.degree + 1)
        coeff.forEachIndexed {i, vi ->
            other.coeff.forEachIndexed { j, vj ->
                res[i+j] += vi * vj
            }
        }
        return Polynomial(*res)
    }

    operator fun times(num: Double) : Polynomial {
        val res = DoubleArray(degree + 1)
        coeff.forEachIndexed { i, v ->
            res[i] = v * num
        }
        return Polynomial(*res)
    }

//    operator fun div(other: Polynomial) : Polynomial {
//        // Можно ли делить в таком случае?
//        // this - dividend, other - divisor
//        if (degree < other.degree) {
//            throw Exception("Degree of dividend cannot be less than degree of divisor")
//        }
//        val remainder = coeff.toDoubleArray()
//        val quotient = DoubleArray(coeff.count() - other.coeff.count(), init = {0.0})
//        for (i in 0 until quotient.count()) {
//            val num = remainder[remainder.count() - i - 1] / other.coeff.last()
//            quotient[quotient.count() - i - 1] = num
//            for (j in 0 until coeff.count()) {
//                remainder[remainder.count() - i - j - 1] = num * coeff[coeff.count() - j - 1]
//            }
//        }
//
//        return Polynomial(*quotient, *remainder)
//    }

    operator fun div(num: Double) : Polynomial {
        if (num == 0.0) {
            throw Exception("Division by zero forbidden")
        }
        return this * (1.0 / num)
    }
}