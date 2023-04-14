package neilyich

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import neilyich.FormattedPrinter.withBlue
import neilyich.FormattedPrinter.withPurple
import neilyich.FormattedPrinter.withRed
import java.io.File
import kotlin.math.max
import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
    val task = configMapper().readValue(File("lr3.json"), Lr3Task::class.java)
    FormattedPrinter.NUMBER_WIDTH = max(
        task.randomGame.minValue.toString().length,
        task.randomGame.maxValue.toString().length
    )

    val familyArgue = task.definedGames.familyArgue
    val crossroad = task.definedGames.crossroad
    val prisonerDilemma = task.definedGames.prisonerDilemma

    NashEquilibrium(familyArgue).find()
    NashEquilibrium(crossroad).find()
    NashEquilibrium(prisonerDilemma).find()

    ParetoOptimality(familyArgue).find()
    ParetoOptimality(crossroad).find()
    ParetoOptimality(prisonerDilemma).find()

    withRed { print("(*,*)") }
    println(" - ситуации, равновесные по Нэшу")
    withBlue { print("(*,*)") }
    println(" - ситуации, оптимальные по Парето")
    withPurple { print("(*,*)") }
    println(" - ситуации, равновесные по Нэшу и оптимальные по Парето")

    println()
    FormattedPrinter.printGame2D("Перекресток", crossroad)
    println()
    FormattedPrinter.printGame2D("Семейный спор", familyArgue)
    println()
    FormattedPrinter.printGame2D("Дилемма заключенного", prisonerDilemma)

    val game = randomGame2D(task.randomGame)
    NashEquilibrium(game).find()
    ParetoOptimality(game).find()
    println()
    FormattedPrinter.printGame2D("Случайная игра 10х10", game)

    val personalGame = task.definedGames.personalGame
    NashEquilibrium(personalGame).find()
    ParetoOptimality(personalGame).find()
    println()
    FormattedPrinter.printGame2D("Игра по варианту (17)", personalGame)
    val mixedEquilibrium = MixedEquilibrium(personalGame).find() ?: return
    (0..1).forEach {
        FormattedPrinter.printMixedStrategy("Вполне смешанная ситуация равновесия для ${it+1}-го игрока",
            mixedEquilibrium.mixedStrategies[it]
        )
    }
    (0..1).forEach {
        FormattedPrinter.printDouble("Выигрыш ${it+1}-го игрока",
            mixedEquilibrium.wins[it]
        )
    }
}

private fun configMapper(): ObjectMapper {
    val mapper = ObjectMapper()
    mapper.enable(JsonParser.Feature.ALLOW_COMMENTS)
    val situationDeserializer = object : StdDeserializer<Situation?>(Situation::class.java) {
        override fun deserialize(jsonParser: JsonParser, p1: DeserializationContext): Situation {
            val win = jsonParser.readValueAs(IntArray::class.java)
            return Situation(win.toList())
        }
    }
    val game2dDeserializer = object : StdDeserializer<Game2D?>(Game2D::class.java) {
        override fun deserialize(jsonParser: JsonParser, p1: DeserializationContext): Game2D {
            val situations = jsonParser.readValueAs(Array<Array<Situation>>::class.java)
            return Game2D(situations.map { it.toList() })
        }
    }
    mapper.registerModules(SimpleModule().apply {
        addDeserializer(Situation::class.java, situationDeserializer)
        addDeserializer(Game2D::class.java, game2dDeserializer)
    }, kotlinModule())
    return mapper
}

private fun randomGame2D(config: Lr3Task.RandomGame): Game2D {
    val range = config.minValue..config.maxValue
    val rand = Random(config.seed)
    val g = mutableListOf<List<Situation>>()
    for (i in 0 until config.firstPlayerStrategiesCount) {
        val row = mutableListOf<Situation>()
        for (j in 0 until config.secondPlayerStrategiesCount) {
            val first = rand.nextInt(range)
            val second = rand.nextInt(range)
            row.add(Situation(first, second))
        }
        g.add(row)
    }
    return Game2D(g)
}