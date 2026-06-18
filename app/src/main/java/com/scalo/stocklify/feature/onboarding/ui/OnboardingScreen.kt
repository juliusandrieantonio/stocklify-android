package com.scalo.stocklify.feature.onboarding.ui

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.scalo.stocklify.core.ui.components.buttons.StocklifyPrimaryButton
import com.scalo.stocklify.core.ui.components.buttons.StocklifyTertiaryButton
import com.scalo.stocklify.core.ui.theme.StocklifySpacing
import com.scalo.stocklify.core.ui.theme.StocklifyStaticColors
import com.scalo.stocklify.core.ui.theme.StocklifyTextStyles
import com.scalo.stocklify.core.ui.theme.StocklifyTheme
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pages = state.pages

    if (pages.isEmpty()) return

    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    val goNext: () -> Unit = {
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    val isLastPage = pagerState.currentPage == pages.lastIndex


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(StocklifySpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(StocklifySpacing.m, alignment = Alignment.CenterVertically)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            userScrollEnabled = true
        ) { page ->
            val item = pages[page]
            OnboardingPageContent(item)
        }

        Spacer(
            modifier = Modifier.height(StocklifySpacing.l)
        )

        OnboardingIndicator(
            pages = pages,
            pagerState = pagerState
        )

        Spacer(
            modifier = Modifier.height(StocklifySpacing.l)
        )
        StocklifyPrimaryButton(
            fullWidth = true,
            text = if (isLastPage) "Get Started" else "Next",
            onClick = {
                if (isLastPage) {
                    viewModel.completeOnboarding()
                    onFinished()
                } else {
                    goNext()
                }
            }
        )

        if (!isLastPage) {
            StocklifyTertiaryButton(
                fullWidth = true,
                text = "Skip",
                onClick = {
                    viewModel.completeOnboarding()
                    onFinished()
                }
            )
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnBoardingPage) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = page.title,
            color = StocklifyTheme.colors.primary700,
            style = StocklifyTextStyles.heading3
        )
        Text(
            text = page.subTitle,
            color = StocklifyTheme.colors.typography400,
            style = StocklifyTextStyles.subtitleXs
        )

        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Text(
            text = page.contentText,
            color = StocklifyStaticColors.black,
            style = StocklifyTextStyles.bodyXs
        )
    }
}

@Composable
fun OnboardingIndicator(
    pages: List<OnBoardingPage>,
    pagerState: PagerState
) {
    val currentPage = pagerState.currentPage

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(pages.size) { index ->

            val isSelected = currentPage == index

            Box(
                modifier = Modifier
                    .height(6.dp)
                    .width(if (isSelected) 32.dp else 16.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isSelected)
                            StocklifyTheme.colors.primary600
                        else
                            StocklifyTheme.colors.transparentPrimary600
                    )
            )
        }
    }
}
