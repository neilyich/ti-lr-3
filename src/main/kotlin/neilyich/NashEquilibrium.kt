package neilyich

class NashEquilibrium(
    private val game: Game
) {
    private val players = game.players()

    fun find(): Set<Situation> {
        return game.filter {
            !someoneWantsToChange(it)
        }.map {
            game.situation(it).also { s ->
                s.nashEquilibrium = Situation.NashEquilibrium.IS
            }
        }.toSet()
    }

    private fun someoneWantsToChange(strategies: List<Int>): Boolean {
        val situation = game.situation(strategies)
        for (player in players) {
            for (newPlayerStrategy in player.strategies()) {
                if (newPlayerStrategy == strategies[player.id]) {
                    continue
                }
                val newStrategies = ArrayList(strategies).apply { set(player.id, newPlayerStrategy) }
                val newSituation = game.situation(newStrategies)
                if (player.wantsToChange(situation, newSituation)) {
                    situation.nashEquilibrium = Situation.NashEquilibrium.NOT
                    return true
                }
            }
        }
        return false
    }
}

private fun Player.wantsToChange(from: Situation, to: Situation): Boolean {
    return to[id] >= from[id]
}
