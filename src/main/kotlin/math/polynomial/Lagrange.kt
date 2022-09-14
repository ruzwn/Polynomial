package math.polynomial

class Lagrange constructor(nodesAndFunValuePairs: MutableMap<Double, Double>) : Polynomial() {
    init {
        var lagrange = Polynomial()
        nodesAndFunValuePairs.forEach { (node, funVal) ->
            lagrange += getFundamentalLagrangePolynomial(nodesAndFunValuePairs, node) * funVal
        }
        _coeff = lagrange.coeff.toMutableList()
    }

    private fun getFundamentalLagrangePolynomial(
        nodesAndFunValuePairs: MutableMap<Double, Double>,
        currNode: Double
    ): Polynomial {
        var polynomial = Polynomial(1.0)
        nodesAndFunValuePairs.forEach { (node, _) ->
            if (node != currNode) {
                polynomial *= Polynomial(-1.0 * node / (currNode - node), 1.0 / (currNode - node))
            }
        }
        return polynomial
    }
}