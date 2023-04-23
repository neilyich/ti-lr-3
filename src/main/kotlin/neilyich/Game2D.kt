package neilyich

class Game2D(
    private val situations: List<List<Situation>>
) : Game {
    val rows = situations.size
    val cols = situations.firstOrNull()?.size ?: 0

    override fun situation(strategies: List<Int>): Situation {
        if (strategies.size != 2) {
            throw IllegalArgumentException("unexpected strategies count for 2d game: ${strategies.size}")
        }
        return situation(strategies[0], strategies[1])
    }

    fun situation(firstStrategy: Int, secondStrategy: Int): Situation {
        return situations[firstStrategy][secondStrategy]
    }

    override fun players(): List<Player> {
        return listOf(Player(0, rows), Player(1, cols))
    }

    fun playerWinMatrix(player: Player): List<List<Int>> {
        if (player.id == 0) {
            return (0 until rows).map { r ->
                (0 until cols).map { c -> situation(r, c)[player.id] }
            }
        }
        return (0 until cols).map { c ->
            (0 until rows).map { r -> situation(r, c)[player.id] }
        }
    }

    override fun iterator(): Iterator<List<Int>> {
        return object : Iterator<List<Int>> {
            private var i = 0
            private var j = 0

            override fun hasNext(): Boolean {
                return i < rows && j < cols
            }

            override fun next(): List<Int> {
                val oldI = i
                val oldJ = j
                j++
                if (j == situations[i].size) {
                    j = 0
                    i++
                }
                return listOf(oldI, oldJ)
            }

        }
    }
}