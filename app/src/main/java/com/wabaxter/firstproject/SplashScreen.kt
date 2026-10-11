package com.wabaxter.firstproject

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wabaxter.firstproject.ui.theme.FirstProjectTheme
import kotlinx.coroutines.delay

private const val SPLASH_HOLD_MS = 1500L

@Composable
fun BowlingSplashScreen(onFinished: () -> Unit) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(durationMillis = 800))
        delay(SPLASH_HOLD_MS)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_bowling_ball),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .scale(0.6f + 0.4f * progress.value),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.splash_title),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.alpha(progress.value),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.splash_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.alpha(progress.value),
        )
    }
}

@Preview
@Composable
private fun BowlingSplashScreenPreview() {
    FirstProjectTheme { BowlingSplashScreen(onFinished = {}) }
}
