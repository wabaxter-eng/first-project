package com.wabaxter.firstproject

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wabaxter.firstproject.scoring.BowlingScorer
import com.wabaxter.firstproject.scoring.FRAME_COUNT
import com.wabaxter.firstproject.scoring.Frame
import com.wabaxter.firstproject.scoring.PIN_COUNT
import com.wabaxter.firstproject.ui.theme.FirstProjectTheme

@Composable
fun HomeScreen() {
    var rolls by rememberSaveable { mutableStateOf(emptyList<Int>()) }

    val frames = BowlingScorer.frames(rolls)
    val maxNext = BowlingScorer.maxNextRoll(rolls)
    val gameOver = BowlingScorer.isGameOver(rolls)
    val score = BowlingScorer.currentScore(rolls)
    val currentFrame = when {
        gameOver -> -1
        frames.isEmpty() -> 0
        frames.last().isComplete -> frames.size
        else -> frames.size - 1
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.score_tracker_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            Row(Modifier.horizontalScroll(rememberScrollState())) {
                for (i in 0 until FRAME_COUNT) {
                    FrameCell(number = i + 1, frame = frames.getOrNull(i), highlighted = i == currentFrame)
                }
            }

            Text(
                text = if (gameOver) {
                    stringResource(R.string.final_score, score)
                } else {
                    stringResource(R.string.frame_status, currentFrame + 1, score)
                },
                style = MaterialTheme.typography.titleLarge,
            )

            if (!gameOver) {
                Text(stringResource(R.string.pins_prompt), style = MaterialTheme.typography.bodyMedium)
                (0..PIN_COUNT).chunked(6).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { pins ->
                            Button(
                                onClick = { rolls = rolls + pins },
                                enabled = pins <= maxNext,
                                modifier = Modifier.weight(1f),
                            ) { Text(pins.toString()) }
                        }
                        repeat(6 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { rolls = rolls.dropLast(1) },
                    enabled = rolls.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                ) { Text(stringResource(R.string.undo)) }
                Button(
                    onClick = { rolls = emptyList() },
                    enabled = rolls.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                ) { Text(stringResource(R.string.new_game)) }
            }
        }
    }
}

@Composable
private fun FrameCell(number: Int, frame: Frame?, highlighted: Boolean) {
    val isTenth = number == FRAME_COUNT
    val marks = frame?.marks ?: List(if (isTenth) 3 else 2) { "" }
    val borderColor =
        if (highlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Column(
        modifier = Modifier
            .width(BOX_SIZE * marks.size)
            .border(if (highlighted) 2.dp else 1.dp, borderColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(number.toString(), style = MaterialTheme.typography.labelSmall)
        Row {
            marks.forEach { mark ->
                Box(Modifier.size(BOX_SIZE), contentAlignment = Alignment.Center) {
                    Text(mark, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        Box(Modifier.height(32.dp), contentAlignment = Alignment.Center) {
            Text(
                text = frame?.cumulativeScore?.toString() ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private val BOX_SIZE = 28.dp

@Preview
@Composable
private fun HomeScreenPreview() {
    FirstProjectTheme { HomeScreen() }
}
