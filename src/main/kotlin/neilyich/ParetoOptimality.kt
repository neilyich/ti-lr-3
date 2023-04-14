package neilyich

class ParetoOptimality(
    private val game: Game
) {
    private val players = game.players()

    fun find(): Set<Situation> {
        return game.filter {
            !canBeImprovedForEveryone(it)
        }.map {
            game.situation(it).also { s ->
                s.optimalByPareto = Situation.OptimalByPareto.IS
            }
        }.toSet()
    }

    private fun canBeImprovedForEveryone(strategies: List<Int>): Boolean {
        val checkedSituation = game.situation(strategies)
        for (situation in game.situations()) {
            if (situation.isBetterForEveryone(checkedSituation, players)) {
                checkedSituation.optimalByPareto = Situation.OptimalByPareto.NOT
                return true
            }
        }
        return false
    }
}

private fun Situation.isBetterForEveryone(than: Situation, players: List<Player>): Boolean {
    var eq = true
    for (player in players) {
        if (this[player.id] < than[player.id]) {
            return false
        } else if (eq && than[player.id] != this[player.id]) {
            eq = false
        }
    }
    return !eq
}
