package com.mambo.iris.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mambo.iris.R
import com.mambo.iris.ui.theme.*
import com.mambo.iris.ui.navigation.AppRoute
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val title: AnnotatedString,
    val subtitle: String,
    val text: String,
    val backgroundColor: Color,
    val accentColor: Color,
    val textColor: Color = Color.White,
    val logoInfo: LogoInfo,
    val verticalArrangement: Arrangement.Vertical = Arrangement.Center
)

private data class LogoInfo(
    val resId: Int,
    val alignment: Alignment,
    val scaleX: Float = 1f,
    val modifier: Modifier = Modifier,
    val subtitleSize: Int,
    val textSize: Int
)

private val pages = listOf(
    // --- PANTALLA 1
    OnboardingPage(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = workSansRegular, fontWeight = FontWeight.Normal, fontSize = 24.sp, color = NaranjaIris)) {
                append("Bienvenido a\n")
            }
            withStyle(style = SpanStyle(fontFamily = highTowerFontFamily, fontSize = 60.sp, color = Color.White)) {
                append("Iris")
            }
        },
        subtitle = "¿Qué somos?",
        text = "Somos una app que transforma el arte en una experiencia viva, emocional y accesible. Aquí, las obras no solo se miran: te escuchan, te hablan y te acompañan. Activa símbolos, trazos y memorias en tu entorno con realidad aumentada y descubre el arte como lenguaje que conecta, transforma y recuerda.\n\nEl arte no espera a ser visitado. Sale al encuentro.",
        backgroundColor = IrisFondoGris,
        accentColor = NaranjaIris,
        logoInfo = LogoInfo(
            resId = R.drawable.logo_fondo,
            alignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .offset(x = 30.dp, y = 30.dp),
            subtitleSize = 20,
            textSize = 16
        ),

        verticalArrangement = Arrangement.Top
    ),

    // --- PANTALLA 2 ---
    OnboardingPage(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = workSansRegular, fontSize = 24.sp, color = Color.White)) {
                append("¿Cómo funciona?")
            }
        },
        subtitle = "Usamos realidad aumentada e inteligencia artificial para activar obras en tu espacio.",
        text = "Solo necesitas tu cámara y tu sensibilidad. Escanea, escucha, conversa y guarda tu experiencia en una bitácora emocional.\n\nCada obra tiene algo que decir. Tú decides cómo responder.",
        backgroundColor = NaranjaIris,
        accentColor = Color.White,
        logoInfo = LogoInfo(
            resId = R.drawable.logo_fondo_espejo,
            alignment = Alignment.TopEnd,
            scaleX = -1f,
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .offset(x = 50.dp, y = (-50).dp),
            subtitleSize = 20,
            textSize = 16
        )
    ),

    // --- PANTALLA 3 ---
    OnboardingPage(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = workSansRegular, fontSize = 24.sp, color = Color.White)) {
                append("Experiencias\nque brindamos")
            }
        },
        subtitle = "Recorridos emocionales:\nobras que se adaptan a tu estado de ánimo.\n\nActivaciones simbólicas:\ntrazos y símbolos que emergen en tu entorno.",
        text = "Bitácoras sensibles:\nescribe, reflexiona y guarda tu diálogo con el arte.\n\nExploración libre:\ndescubre obras en tu comunidad, escuela o espacio cotidiano.",
        backgroundColor = IrisMaroon,
        accentColor = Color.White,
        logoInfo = LogoInfo(
            resId = R.drawable.logo_espiral,
            alignment = Alignment.TopEnd,
            modifier = Modifier.fillMaxWidth(0.7f),
            subtitleSize = 18,
            textSize = 14
        ),
        verticalArrangement = Arrangement.Bottom
    ),

    // --- PANTALLA 4 ---
    OnboardingPage(
        title = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = workSansRegular, fontSize = 24.sp, color = Color.Black)) {
                append("Crea tu espacio\nen IRIS")
            }
        },
        subtitle = "No necesitas saber de arte, solo estar dispuesto/a a sentir. Regístrate para guardar tus recorridos, tus emociones y tus símbolos activados.",
        text = "Tu historia también es arte.\nObraviva la quiere escuchar.",
        backgroundColor = IrisSky,
        accentColor = IrisSky,
        textColor = Color.Black,
        logoInfo = LogoInfo(
            resId = R.drawable.logo_lado,
            alignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth(0.7f),
            subtitleSize = 18,
            textSize = 14
        ),
        verticalArrangement = Arrangement.Top
    )
)

@Composable
fun OnboardingScreen(navController: NavController) {

    // CORRECCIÓN: Definir explícitamente initialPage para resolver el error de firma de función
    val pagerState = rememberPagerState(
        initialPage = 0, // Inicia siempre en la primera página
        pageCount = { pages.size }
    )

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                scope.launch {
                    val nextPage = pagerState.currentPage + 1
                    if (nextPage < pages.size) {
                        pagerState.animateScrollToPage(nextPage)
                    } else {
                        // Navegación al registro
                        navController.navigate(AppRoute.Login.route) {
                            popUpTo(AppRoute.Onboarding.route) { inclusive = true }
                        }
                    }
                }
            }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = false
        ) { pageIndex ->
            OnboardingPageContent(page = pages[pageIndex])
        }
        PageIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        )
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backgroundColor)
    ) {
        Image(
            painter = painterResource(id = page.logoInfo.resId),
            contentDescription = "Logo Iris",
            contentScale = ContentScale.Fit,
            modifier = page.logoInfo.modifier
                .align(page.logoInfo.alignment)
                .scale(scaleX = page.logoInfo.scaleX, scaleY = 1f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (page.verticalArrangement == Arrangement.Bottom) 150.dp else 0.dp)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = page.verticalArrangement
        ) {
            when (page.verticalArrangement) {
                Arrangement.Top -> Spacer(modifier = Modifier.height(100.dp))
                Arrangement.Bottom -> Spacer(modifier = Modifier.weight(1f))
                else -> { /* No hace nada para Center */ }
            }

            Text(
                text = page.title,
                textAlign = TextAlign.Start,
                lineHeight = 50.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = page.subtitle,
                fontFamily = workSansBold,
                fontSize = page.logoInfo.subtitleSize.sp,
                color = page.textColor,
                textAlign = TextAlign.Start,
                lineHeight = (page.logoInfo.subtitleSize + 6).sp
            )

            if (page.text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = page.text,
                    fontFamily = workSansRegular,
                    fontSize = page.logoInfo.textSize.sp,
                    color = page.textColor,
                    textAlign = TextAlign.Start,
                    lineHeight = (page.logoInfo.textSize + 6).sp
                )
            }
        }
    }
}

@Composable
private fun PageIndicator(pagerState: PagerState, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(32.dp))
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) pages[pagerState.currentPage].accentColor else Color.Gray.copy(alpha = 0.5f)
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Onboarding Page 1")
@Composable
fun OnboardingScreenP1Preview() {
    IrisAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState(pageCount = { pages.size })
            OnboardingPageContent(page = pages[0])
            PageIndicator(pagerState = pagerState, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp))
        }
    }
}

@Preview(showBackground = true, name = "Onboarding Page 4")
@Composable
fun OnboardingScreenP4Preview() {
    IrisAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState(pageCount = { pages.size }, initialPage = 3)
            OnboardingPageContent(page = pages[3])
            PageIndicator(pagerState = pagerState, modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp))
        }
    }
}
