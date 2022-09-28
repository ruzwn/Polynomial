package math.polynomial

import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.pow

open class Polynomial constructor(vararg coeffArgs: Double) {
    protected var _coeffs: MutableList<Double>
    val coeffs: List<Double>
        get() = _coeffs.toList()
    val degree: Int
        get() = _coeffs.size - 1

    init {
        // Если, например, Polynomial(1.0, 4.0, 0.0), то степень будет 1, а не 2
        val tempCoeffs = coeffArgs.toMutableList()
        var tempDegree = 0
        for (i in tempCoeffs.count() - 1 downTo  0) {
            if (tempCoeffs[i] neq 0.0) {
                tempDegree = i
                break
            }
        }
        this._coeffs = MutableList(tempDegree + 1, init = { i -> tempCoeffs[i] })
    }

    constructor(): this(0.0)

    operator fun invoke(x: Double) : Double =
        DoubleArray(degree + 1, init = { i -> x.pow(i) * _coeffs[i] }).sum()

    override fun toString(): String {
        val builder = StringBuilder()
        _coeffs.asReversed().forEachIndexed { i, v ->
            if (v neq 0.0) {
                if (i > 0) {
                    if (v > 0) {
                        builder.append("+")
                    }
                }
                if (v < 0) {
                    builder.append("-")
                }
                if (abs(v) eq 1.0 && i != _coeffs.size - 1) {

                }
                else {
                    builder.append("${abs(v)}")
                }
                if (i < _coeffs.size - 2) {
                    builder.append("x^${_coeffs.size - i - 1}")
                }
                if (i == _coeffs.size - 2) {
                    builder.append("x")
                }
            }
        }
        if (builder.isEmpty()) {
            builder.append(0.0)
        }
        return builder.toString()
    }

    operator fun plus(other: Polynomial) : Polynomial {
        val (min, max) =
            if (degree < other.degree) {
                arrayOf(coeffs, other.coeffs)
            }
            else {
                arrayOf(other.coeffs, coeffs)
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
        _coeffs.forEachIndexed { i, vi ->
            other._coeffs.forEachIndexed { j, vj ->
                res[i+j] += vi * vj
            }
        }
        return Polynomial(*res)
    }

    operator fun times(num: Double) : Polynomial {
        val res = DoubleArray(degree + 1, init = { i -> _coeffs[i] * num })
        return Polynomial(*res)
    }

    operator fun div(num: Double) : Polynomial {
        if (num eq 0.0) {
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
        this._coeffs = (this + other)._coeffs
    }

    operator fun minusAssign(other: Polynomial) {
        this._coeffs = (this - other)._coeffs
    }

    operator fun timesAssign(other: Polynomial) {
        this._coeffs = (this * other)._coeffs
    }

    override fun equals(other: Any?) : Boolean {
        if (other !is Polynomial) {
            return false
        }
        if (other.degree != this.degree) {
            return false
        }
        for (i in 0 until this.degree) {
            if (other._coeffs[i] neq this._coeffs[i]) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return _coeffs.hashCode()
    }
}