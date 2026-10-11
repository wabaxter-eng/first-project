package com.wabaxter.firstproject.scoring

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BowlingScorerTest {

    @Test
    fun gutterGame() {
        val rolls = List(20) { 0 }
        assertTrue(BowlingScorer.isGameOver(rolls))
        assertEquals(0, BowlingScorer.currentScore(rolls))
    }

    @Test
    fun perfectGame() {
        val rolls = List(12) { 10 }
        assertTrue(BowlingScorer.isGameOver(rolls))
        assertEquals(300, BowlingScorer.currentScore(rolls))
        assertEquals(listOf("X", "X", "X"), BowlingScorer.frames(rolls).last().marks)
    }

    @Test
    fun allSpares() {
        val rolls = List(21) { 5 }
        assertTrue(BowlingScorer.isGameOver(rolls))
        assertEquals(150, BowlingScorer.currentScore(rolls))
    }

    @Test
    fun openFrames() {
        val rolls = List(10) { listOf(9, 0) }.flatten()
        assertTrue(BowlingScorer.isGameOver(rolls))
        assertEquals(90, BowlingScorer.currentScore(rolls))
        assertEquals(listOf("9", "-"), BowlingScorer.frames(rolls).first().marks)
    }

    @Test
    fun strikeWaitsForBonusRolls() {
        val frames = BowlingScorer.frames(listOf(10, 3))
        assertNull(frames[0].cumulativeScore)
        assertEquals(listOf("", "X"), frames[0].marks)

        assertEquals(17, BowlingScorer.frames(listOf(10, 3, 4))[0].cumulativeScore)
        assertEquals(24, BowlingScorer.currentScore(listOf(10, 3, 4)))
    }

    @Test
    fun maxNextRollLimitsSecondBall() {
        assertEquals(10, BowlingScorer.maxNextRoll(emptyList()))
        assertEquals(3, BowlingScorer.maxNextRoll(listOf(7)))
        assertEquals(10, BowlingScorer.maxNextRoll(listOf(7, 3)))
        assertEquals(10, BowlingScorer.maxNextRoll(listOf(10)))
    }

    @Test
    fun tenthFrameBonusBalls() {
        val nineOpen = List(9) { listOf(0, 0) }.flatten()
        assertEquals(10, BowlingScorer.maxNextRoll(nineOpen + listOf(10)))
        assertEquals(6, BowlingScorer.maxNextRoll(nineOpen + listOf(10, 4)))
        assertEquals(10, BowlingScorer.maxNextRoll(nineOpen + listOf(6, 4)))
        assertFalse(BowlingScorer.isGameOver(nineOpen + listOf(6, 4)))
        assertTrue(BowlingScorer.isGameOver(nineOpen + listOf(6, 3)))
        assertEquals(0, BowlingScorer.maxNextRoll(nineOpen + listOf(6, 3)))
        assertEquals(listOf("X", "4", "/"), BowlingScorer.frames(nineOpen + listOf(10, 4, 6)).last().marks)
        assertEquals(listOf("6", "/", "X"), BowlingScorer.frames(nineOpen + listOf(6, 4, 10)).last().marks)
    }
}
