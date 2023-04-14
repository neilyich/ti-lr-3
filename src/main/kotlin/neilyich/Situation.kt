package neilyich

data class Situation(
    val win: List<Int>,
    var optimalByPareto: OptimalByPareto = OptimalByPareto.UNKNOWN,
    var nashEquilibrium: NashEquilibrium = NashEquilibrium.UNKNOWN,
) {
    constructor(vararg win: Int) : this(win.toList())

//
//    var optimalByPareto = OptimalByPareto.UNKNOWN
//    var nashEquilibrium = NashEquilibrium.UNKNOWN
//
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
        return win.toString()
    }
//
//
//    fun canAllIncreaseWin(other: Situation): Boolean {
//        var hasLess = false
//        var hasBigger = false
//        for (i in win.indices) {
//            if (this[i] < win[i]) {
//                hasBigger = true
//            } else if (this[i] > win[i]) {
//                hasLess = true
//            }
//            if (hasBigger && hasLess) {
//                break
//            }
//        }
//        if (hasBigger) {
//            if (hasLess) {
//                return true
//            } else {
//                optimalByPareto = OptimalByPareto.NOT
//                return false
//            }
//        } else {
//            if (hasLess) {
//                return false
//            } else {
//                return true
//            }
//        }
//    }
}
