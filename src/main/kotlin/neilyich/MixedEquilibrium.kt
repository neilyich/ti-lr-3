package neilyich

import kotlin.math.abs

class MixedEquilibrium(
    private val game2D: Game2D,
) {
    fun find(): MixedEquilibriumSituation? {
        if (game2D.rows != 2 || game2D.cols != 2) {
            throw IllegalArgumentException("only games 2x2 supported")
        }
        val nashEquilibriumCount = game2D.situations().count { it.isNashEquilibrium }
        for (player in game2D.players()) {
            val dominantStrategy = dominantStrategy(player)
            if (dominantStrategy != null) {
                println("Игрок ${player.id+1} имеет доминирующую стратегию ${dominantStrategy+1}, " +
                        "поэтому игра имеет единственную ситуацию равновесия по Нэшу")
                if (nashEquilibriumCount == 1) {
                    println("Единственная ситуация равновесия по Нэшу уже найдена в чистых стратегиях")
                    return null
                }
            }
        }
        when (nashEquilibriumCount) {
            0 -> {
                println("Игра не имеет ситуации равновесия по Нэшу в чистых стратегиях, " +
                        "поэтому существует вполне смешанная ситуация равновесия")
            }
            2 -> {
                println("Игра имеет 2 ситуации равновесия по Нэшу в чистых стратегиях, " +
                        "поэтому еще существует вполне смешанная ситуация равновесия")
            }
            else -> {
                println("Условие для существования смешанной ситуации не выполнено")
                return null
            }
        }
        val a = getPlayerWinMatrix(game2D.players()[0])
        val b = getPlayerWinMatrix(game2D.players()[1])
        val notA = reverse(a)
        //println("A: ${det(a)}")
        //a.forEach { println(it) }
        //println()
        //notA.forEach { println(it) }
        //println()
        //mult(a, notA).forEach { println(it) }
        val notB = reverse(b)
        //println()
        //println("B: ${det(b)}")
        //b.forEach { println(it) }
        //println()
        //notB.forEach { println(it) }
        //println()
        //mult(b, notB).forEach { println(it) }
        val vA = 1.0 / (multURight(multULeft(notA)))
        val vB = 1.0 / (multURight(multULeft(notB)))
        val x = multULeft(notB).map { it * vB }
        val y = multURight(notA).map { it * vA }
        return MixedEquilibriumSituation(listOf(x, y), listOf(vA, vB))
    }

    private fun getPlayerWinMatrix(player: Player): List<List<Int>> {
        return (0 until game2D.rows).map { r ->
            (0 until game2D.cols).map { c -> game2D.situation(r, c)[player.id] }
        }
    }

    private fun reverse(matrix2x2: List<List<Int>>): List<List<Double>> {
        val det = abs(det(matrix2x2))
        if (det == 0) {
            throw IllegalArgumentException("mixed equilibrium works only for invertible matrix: $matrix2x2")
        }
        return listOf(
            listOf(matrix2x2[1][1], -matrix2x2[0][1]),
            listOf(-matrix2x2[1][0], matrix2x2[0][0])
        ).map { row -> row.map { el -> el.toDouble() / det } }
    }

    private fun det(matrix2x2: List<List<Int>>): Int {
        return matrix2x2[0][0]*matrix2x2[1][1] - matrix2x2[0][1]*matrix2x2[1][0]
    }

    private fun multULeft(matrix: List<List<Double>>): List<Double> {
        val result = ArrayList<Double>(matrix[0].size)
        for (c in matrix[0].indices) {
            var sum = 0.0
            for (r in matrix.indices) {
                sum += matrix[r][c]
            }
            result.add(sum)
        }
        return result
    }

    private fun multURight(matrix: List<List<Double>>): List<Double> {
        return matrix.map { it.sum() }
    }

    private fun multURight(vector: List<Double>): Double {
        return vector.sum()
    }

    private fun mult(a: List<List<Int>>, b: List<List<Double>>): List<List<Double>> {
        val result = mutableListOf<List<Double>>()
        for (r in a.indices) {
            val row = mutableListOf<Double>()
            for (c in b[0].indices) {
                var sum = 0.0
                for (k in a[0].indices) {
                    sum += a[r][k] * b[k][c]
                }
                row.add(sum)
            }
            result.add(row)
        }
        return result
    }

    private fun dominantStrategy(player: Player): Int? {
        val wins = getPlayerWinMatrix(player)
        for (i in wins.indices) {
            val dominantCandidate = wins[i]
            val isDominant = wins.all { dominantCandidate.dominates(it) }
            if (isDominant) {
                return i
            }
        }
        return null
    }
}

private fun List<Int>.dominates(other: List<Int>): Boolean {
    for (i in indices) {
        if (this[i] < other[i]) {
            return false
        }
    }
    return true
}