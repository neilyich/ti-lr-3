package neilyich

data class Lr3Task(
    val randomGame: RandomGame,
    val definedGames: DefinedGames,
) {
    data class DefinedGames(
        val crossroad: Game2D,
        val familyArgue: Game2D,
        val prisonerDilemma: Game2D,
        val personalGame: Game2D,
    )
    data class RandomGame(
        val seed: Int,
        val firstPlayerStrategiesCount: Int,
        val secondPlayerStrategiesCount: Int,
        val minValue: Int,
        val maxValue: Int,
    )
}