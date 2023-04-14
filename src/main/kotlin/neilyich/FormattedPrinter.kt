package neilyich

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max

object FormattedPrinter {
    var NUMBER_WIDTH = 3

    private const val SEP_HOR = "─"
    private const val SEP_VER = "│"
    private const val CROSS = "┼"
    private const val CROSS_UP = "┬"
    private const val CROSS_DOWN = "┴"
    private const val CROSS_LEFT = "├"
    private const val CROSS_RIGHT = "┤"
    private const val CORNER_UP_LEFT = "┌"
    private const val CORNER_UP_RIGHT = "┐"
    private const val CORNER_DOWN_LEFT = "└"
    private const val CORNER_DOWN_RIGHT = "┘"

    fun printMixedStrategy(label: String?, strategy: List<Double>) {
        label?.let { print("$label: ") }
        println(formatMixedStrategy(strategy))
    }

    fun printDouble(label: String?, n: Double) {
        label?.let { print("$label: ") }
        println(formatDouble(n))
    }

    fun withBlue(printBlock: FormattedPrinter.() -> Unit) {
        printWithColor(34, printBlock)
    }

    fun withRed(printBlock: FormattedPrinter.() -> Unit) {
        printWithColor(31, printBlock)
    }

    fun withPurple(printBlock: FormattedPrinter.() -> Unit) {
        printWithColor(35, printBlock)
    }

    fun withGray(printBlock: FormattedPrinter.() -> Unit) {
        printWithColor(37, printBlock)
    }

    private fun printWithColor(color: Int, printBlock: FormattedPrinter.() -> Unit) {
        setPrintColor(color)
        this.printBlock()
        resetPrintColor()
    }

    private fun setPrintColor(color: Int) {
        print("\u001B[${color}m")
    }

    private fun resetPrintColor() {
        setPrintColor(0)
    }

    fun printGame2D(label: String? = null, game2D: Game2D) {
        label?.let { println("$it:") }
        for (r in 0 until game2D.rows) {
            withGray {
                printHorizontalSeparator(r, game2D)
                print(SEP_VER)
            }
            for (c in 0 until game2D.cols) {
                val situation = game2D.situation(r, c)
                val usingFormatting: (printBlock: FormattedPrinter.() -> Unit) -> Unit = if (situation.isNashEquilibrium) {
                    if (situation.isOptimalByPareto) {
                        this::withPurple
                    } else {
                        this::withRed
                    }
                } else {
                    if (situation.isOptimalByPareto) {
                        this::withBlue
                    } else {
                        this::withGray
                    }
                }
                usingFormatting {
                    printSituation(game2D.situation(r, c))
                }
                withGray { print(SEP_VER) }
            }
            println()
        }
        withGray {
            printHorizontalSeparator(game2D.rows, game2D)
        }
    }

    private fun printHorizontalSeparator(r: Int, game2D: Game2D) {
        val start = when (r) {
            0 -> CORNER_UP_LEFT
            game2D.rows -> CORNER_DOWN_LEFT
            else -> CROSS_LEFT
        }
        val end = when (r) {
            0 -> CORNER_UP_RIGHT
            game2D.rows -> CORNER_DOWN_RIGHT
            else -> CROSS_RIGHT
        }
        val sep = when (r) {
            0 -> CROSS_UP
            game2D.rows -> CROSS_DOWN
            else -> CROSS
        }
        val cellWidth = NUMBER_WIDTH * 2 + 4
        val totalWidth = game2D.cols * cellWidth
        print(start)
        for (i in 1 until totalWidth) {
            if (i % cellWidth == 0) {
                print(sep)
            } else {
                print(SEP_HOR)
            }
        }
        println(end)
    }

    private fun printSituation(situation: Situation) {
        print(formatSituation(situation))
    }

    private fun formatSituation(situation: Situation): String {
        val str = situation.win.joinToString(separator = ",", prefix = "(", postfix = ")") { formatInt(it) }
        val maxSize = NUMBER_WIDTH * situation.win.size + situation.win.size + 1
        val dif = max(maxSize - str.length, 0)
        val leftPad = (dif + 1) / 2
        val rightPad = dif - leftPad
        return " ".repeat(leftPad) + str + " ".repeat(rightPad)
    }

    private fun formatInt(n: Int): String {
        return n.toString()
    }

    private fun formatMixedStrategy(strategy: List<Double>): String {
        return strategy.joinToString(separator = ",") { formatDouble(it) }
    }

    private fun formatDouble(n: Double): String {
        return BigDecimal.valueOf(n).setScale(3, RoundingMode.HALF_EVEN).toString()
    }
}
