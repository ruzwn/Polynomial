package math.polynomial

import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.pow

open class Polynomial constructor(vararg coeffs: Double) {
    protected var _coeff: MutableList<Double>
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

    operator fun invoke(x: Double) : Double =
        DoubleArray(degree + 1, init = { i -> x.pow(i) * _coeff[i] }).sum()

    override fun toString(): String {
        val strBuilder = StringBuilder()
        _coeff.asReversed().forEachIndexed { i, v ->
            if (v != 0.0) {
                if (i > 0) {
                    if (v > 0) {
                        strBuilder.append("+")
                    }
                }
                if (v < 0) {
                    strBuilder.append("-")
                }
                if (abs(v) == 1.0 && i != _coeff.size - 1) {

                }
                else {
                    strBuilder.append("${abs(v)}")
                }
                if (i < _coeff.size - 2) {
                    strBuilder.append("x^${_coeff.size - i - 1}")
                }
                if (i == _coeff.size - 2) {
                    strBuilder.append("x")
                }
            }
        }
        if (strBuilder.isEmpty()) {
            strBuilder.append(0.0)
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
        val res = DoubleArray(degree + 1, init = { i -> coeff[i] * num })
        return Polynomial(*res)
    }

    operator fun div(num: Double) : Polynomial {
        if (num == 0.0) {
            throw Exception("Division by zero forbidden")
        }
        return this * (1.0 / num)
    }

    operator fun unaryMinus() : Polynomial {
        return this * (-1.0)
    }

    operator fun unaryPlus() : Polynomial {
        return this + Polynomial()
    }

    operator fun plusAssign(other: Polynomial) {
        this._coeff = (this + other)._coeff
    }

    operator fun minusAssign(other: Polynomial) {
        this._coeff = (this - other)._coeff
    }

    operator fun timesAssign(other: Polynomial) {
        this._coeff = (this * other)._coeff
    }

    override fun equals(other: Any?) : Boolean {
        if (other !is Polynomial) {
            return false
        }
        if (other.degree != this.degree) {
            return false
        }
        for (i in 0 until this.degree) {
            if (other.coeff[i] != this.coeff[i]) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return _coeff.hashCode()
    }
    // TODO: все операторы и еще операторы нераветсв
}