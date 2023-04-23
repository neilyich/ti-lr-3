package neilyich

data class Situation(
    val win: List<Int>,
    var optimalByPareto: OptimalByPareto = OptimalByPareto.UNKNOWN,
    var nashEquilibrium: NashEquilibrium = NashEquilibrium.UNKNOWN,
) {
    constructor(vararg win: Int) : this(win.toList())

    enum class OptimalByPareto {
        IS, NOT, UNKNOWN
    }

    val isOptimalByPareto get() = optimalByPareto == OptimalByPareto.IS

    enum class NashEquilibrium {
        IS, NOT, UNKNOWN
    }

    val isNashEquilibrium get() = nashEquilibrium == NashEquilibrium.IS

    operator fun get(i: Int): Int {
        return win[i]
    }

    override fun hashCode(): Int {
        return win.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is Situation && win == other.win)
    }

    override fun toString(): String {
        return win.joinToString(prefix = "(", postfix = ")", separator = ",") { it.toString() }
    }
}
