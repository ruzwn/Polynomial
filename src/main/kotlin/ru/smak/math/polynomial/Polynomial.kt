package ru.smak.math.polynomial

import java.lang.StringBuilder

class Polynomial(vararg coeffs: Double) {
    private val _coeff: MutableList<Double>
    val coeff: List<Double>
        get() = _coeff.toList()

    val order: Int
        get() = _coeff.size - 1

    init{
        _coeff = coeffs.toMutableList()
    }
    constructor(): this(0.0)

    override fun toString(): String {
        val sb = StringBuilder()
        _coeff.asReversed().forEachIndexed { index, d ->
            if (index > 0) sb.append("+")
            sb.append("(")
            sb.append(d)
            sb.append(")x^")
            sb.append(_coeff.size - index - 1)
        }
        return sb.toString()
    }

    operator fun plus(other: Polynomial) : Polynomial{
        val (min, max) =
            if (order < other.order) arrayOf(coeff, other.coeff) else arrayOf(other.coeff, coeff)
        val res = max.toDoubleArray()
        min.forEachIndexed { i, v -> res[i] += v }
        return Polynomial(*res)
    }
}