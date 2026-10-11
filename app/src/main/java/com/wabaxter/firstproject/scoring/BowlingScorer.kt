package com.wabaxter.firstproject.scoring

const val PIN_COUNT = 10
const val FRAME_COUNT = 10

data class Frame(
    val rolls: List<Int>,
    val isTenth: Boolean,
    /** Running game total through this frame, or null until its bonus rolls are known. */
    val cumulativeScore: Int?,
) {
    val isComplete: Boolean
        get() = if (isTenth) isTenthComplete(rolls) else rolls.size == 2 || rolls.firstOrNull() == PIN_COUNT

    /** Scorecard marks for each box: "X" strike, "/" spare, "-" gutter. */
    val marks: List<String>
        get() {
            val marks = MutableList(if (isTenth) 3 else 2) { "" }
            if (!isTenth && rolls.firstOrNull() == PIN_COUNT) {
                marks[1] = "X"
                return marks
            }
            rolls.forEachIndexed { j, pins ->
                val prev = rolls.getOrNull(j - 1)
                val freshRack = j == 0 || prev == PIN_COUNT || marks[j - 1] == "/"
                marks[j] = when {
                    freshRack && pins == PIN_COUNT -> "X"
                    !freshRack && prev != null && prev + pins == PIN_COUNT -> "/"
                    pins == 0 -> "-"
                    else -> pins.toString()
                }
            }
            return marks
        }
}

private fun isTenthComplete(rolls: List<Int>): Boolean =
    rolls.size == 3 || (rolls.size == 2 && rolls[0] + rolls[1] < PIN_COUNT)

object BowlingScorer {

    fun frames(rolls: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        var i = 0
        var total: Int? = 0
        while (i < rolls.size && frames.size < FRAME_COUNT) {
            if (frames.size == FRAME_COUNT - 1) {
                val tenth = rolls.subList(i, minOf(i + 3, rolls.size)).toList()
                total = if (isTenthComplete(tenth)) total?.plus(tenth.sum()) else null
                frames += Frame(tenth, isTenth = true, cumulativeScore = total)
                break
            }
            val first = rolls[i]
            val frameRolls =
                if (first == PIN_COUNT) listOf(first) else rolls.subList(i, minOf(i + 2, rolls.size)).toList()
            val complete = first == PIN_COUNT || frameRolls.size == 2
            val bonusCount = when {
                first == PIN_COUNT -> 2
                complete && frameRolls.sum() == PIN_COUNT -> 1
                else -> 0
            }
            val end = i + frameRolls.size
            val bonus = rolls.subList(end, minOf(end + bonusCount, rolls.size))
            total = if (complete && bonus.size == bonusCount) total?.plus(frameRolls.sum() + bonus.sum()) else null
            frames += Frame(frameRolls, isTenth = false, cumulativeScore = total)
            i = end
        }
        return frames
    }

    /** Most pins the next roll can knock down; 0 once the game is over. */
    fun maxNextRoll(rolls: List<Int>): Int {
        val last = frames(rolls).lastOrNull() ?: return PIN_COUNT
        val r = last.rolls
        if (!last.isTenth) return if (last.isComplete) PIN_COUNT else PIN_COUNT - r[0]
        return when (r.size) {
            1 -> if (r[0] == PIN_COUNT) PIN_COUNT else PIN_COUNT - r[0]
            2 -> when {
                r[0] == PIN_COUNT && r[1] < PIN_COUNT -> PIN_COUNT - r[1]
                r[0] + r[1] >= PIN_COUNT -> PIN_COUNT
                else -> 0
            }
            else -> 0
        }
    }

    fun isGameOver(rolls: List<Int>): Boolean {
        val frames = frames(rolls)
        return frames.size == FRAME_COUNT && frames.last().isComplete
    }

    /** Highest fully-scored running total so far. */
    fun currentScore(rolls: List<Int>): Int =
        frames(rolls).lastOrNull { it.cumulativeScore != null }?.cumulativeScore ?: 0
}
