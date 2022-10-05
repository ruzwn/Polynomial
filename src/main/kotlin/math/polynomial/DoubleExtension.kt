package math.polynomial

import kotlin.math.abs
import kotlin.math.max

/** == */
infix fun Double.eq(other: Double) = abs(this - other) < max(Math.ulp(this), Math.ulp(other) * 10)
/** != */
// TODO: в сравнении с 0 работает неверно?
infix fun Double.neq(other: Double) = abs(this - other) >= max(Math.ulp(this), Math.ulp(other) * 10)
/** <= */
infix fun Double.leq(other: Double) = this < other || abs(this - other) < max(Math.ulp(this), Math.ulp(other) * 10)
/** >= */
infix fun Double.geq(other: Double) = this > other || abs(this - other) < max(Math.ulp(this), Math.ulp(other) * 10)
/** < */
infix fun Double.lt(other: Double) = this < other && abs(this - other) >= max(Math.ulp(this), Math.ulp(other) * 10)
// > */
infix fun Double.gt(other: Double) = this > other && abs(this - other) >= max(Math.ulp(this), Math.ulp(other) * 10)