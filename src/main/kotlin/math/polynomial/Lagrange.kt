package math.polynomial

class Lagrange constructor(nodesAndFunValuePairs: MutableMap<Double, Double>) : Polynomial() {
    init {
        val lagrange = Polynomial()
        nodesAndFunValuePairs.forEach { (node, funVal) ->
            lagrange += getFundamentalLagrangePolynomial(nodesAndFunValuePairs, node) * funVal
        }
        _coeffs = lagrange.coeffs.toMutableList()
    }

    private fun getFundamentalLagrangePolynomial(
        nodesAndFunValuePairs: MutableMap<Double, Double>,
        currNode: Double
    ): Polynomial {
        val polynomial = Polynomial(1.0)
        nodesAndFunValuePairs.forEach { (node, _) ->
            if (node neq currNode) {
                polynomial *= Polynomial(-1.0 * node / (currNode - node), 1.0 / (currNode - node))
            }
        }
        return polynomial
    }
}