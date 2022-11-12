package math.polynomial

class Newton constructor(nodesAndFunValuePairs: MutableMap<Double, Double>): Polynomial() {
    fun addNode(node: Double, funVal: Double) {
        if (_nodes.contains(node)) {
            throw Exception("Can't add existing node")
        }
        
        _nodes.add(node)
        _funValues.add(funVal)
        _valueOfW *= Polynomial(-1.0 * _nodes[_nodes.size - 2], 1.0)
        
        _coeffs = (Polynomial(*_coeffs.toDoubleArray()) + (_valueOfW * getDividedDifference(_nodes.size - 1)))
            .coeffs
            .toMutableList()
    }
    
    fun removeNode(index: Int) {
        _nodes.removeAt(index)
        _funValues.removeAt(index)
        if (_nodes.isNotEmpty()) {
            _coeffs = Newton(mutableMapOf(*_nodes.mapIndexed { i, v -> Pair(v, _funValues[i]) }.toTypedArray()))
                ._coeffs
        }
    }

    private val _nodes: ArrayList<Double> = ArrayList(nodesAndFunValuePairs.size)
    private val _funValues: ArrayList<Double> = ArrayList(nodesAndFunValuePairs.size)
    private val _valueOfW: Polynomial = Polynomial(1.0)

    init {
        var i = 0
        nodesAndFunValuePairs.forEach { (node, funVal) ->
            _nodes.add(node)
            _funValues.add(funVal)
            i++
        }

        val newton = Polynomial()
        newton += _valueOfW * getDividedDifference(0)
        for (j in 1 until _nodes.size) {
            _valueOfW *= Polynomial(-1.0 * _nodes[j - 1], 1.0)
            newton += _valueOfW * getDividedDifference(j)
        }
        
        _coeffs = newton.coeffs.toMutableList()
    }

    private fun getDividedDifference(end: Int): Double {
        var dividedDiff = 0.0
        if (end == 0) {
            return _funValues[0]
        }
        
        for (i in 0 ..end) {
            var nodesProduct = 1.0
            for (j in 0 .. end) {
                if (j == i) {
                    continue
                }
                nodesProduct *= 1.0 / (_nodes[i] - _nodes[j])
            }
            dividedDiff += _funValues[i] * nodesProduct
        }
        
        return dividedDiff
    }
}