package math.polynomial

class Newton constructor(nodesAndFunValuePairs: MutableMap<Double, Double>) : Polynomial() {
    fun addNode(node: Double, funVal: Double): Polynomial {
        if (_nodes.contains(node)) {
            throw Exception("Can't add existing node")
        }
        _nodes.add(node)
        _funValues.add(funVal)
        _valueOfW *= Polynomial(-1.0 * _nodes[_nodes.size - 2], 1.0)
        return Polynomial(*_coeffs.toDoubleArray()) + (_valueOfW * getDevidedDifference(0, _nodes.size - 1))
    }

    private val _nodes: ArrayList<Double> = ArrayList(nodesAndFunValuePairs.size)
    private val _funValues: ArrayList<Double> = ArrayList(nodesAndFunValuePairs.size)
    private val _valueOfW: Polynomial = Polynomial(1.0)
    private val _dividedDifferences: MutableMap<Pair<Int, Int>, Double> = HashMap()

    init {
        var i = 0
        nodesAndFunValuePairs.forEach { (node, funVal) ->
            _nodes.add(node)
            _funValues.add(funVal)
            i++
        }

        val newton = Polynomial()
        newton += _valueOfW * getDevidedDifference(0,0)
        for (j in 1 until _nodes.size) {
            _valueOfW *= Polynomial(-1.0 * _nodes[j - 1], 1.0)
            newton += _valueOfW * getDevidedDifference(0, j)
        }
        _coeffs = newton.coeffs.toMutableList()
    }

    private fun getDevidedDifference(beg: Int, end: Int): Double {
        val key = Pair(beg, end)
        if (beg == end) {
            _dividedDifferences[key] = _funValues[beg]
            return _dividedDifferences[key]!!
        } else if (end == beg + 1) {
            _dividedDifferences[key] = (_funValues[end] - _funValues[beg]) / (_nodes[end] - _nodes[beg])
            return _dividedDifferences[key]!!
        }
        else {
            if (_dividedDifferences.containsKey(key)) {
                return _dividedDifferences[key]!!
            }
            _dividedDifferences[key] = (getDevidedDifference(beg + 1, end) - getDevidedDifference(beg, end - 1)) / (_nodes[end] - _nodes[beg])
            return _dividedDifferences[key]!!
        }
    }
}