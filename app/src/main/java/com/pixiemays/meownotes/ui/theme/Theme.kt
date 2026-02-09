package com.pixiemays.meownotes.ui.theme


import android.app.Activity

import android.os.Build

import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.darkColorScheme

import androidx.compose.material3.dynamicDarkColorScheme

import androidx.compose.material3.dynamicLightColorScheme

import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable

import androidx.compose.runtime.SideEffect

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.toArgb

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalView

import androidx.core.view.WindowCompat


enum class AppTheme {

    PURPLE,

    BLUE,

    GREEN,

    ORANGE,

    PINK

}


private val PurpleLightColorScheme = lightColorScheme(

    primary = Purple40,

    onPrimary = White,

    primaryContainer = Purple80,

    onPrimaryContainer = Color(0xFF21005D),


    secondary = PurpleGrey40,

    onSecondary = White,


    tertiary = Pink40,

    onTertiary = White,


    background = Color(0xFFFFFBFE),

    surface = Color(0xFFFFFBFE)

)


private val PurpleDarkColorScheme = darkColorScheme(

    primary = Purple80,

    onPrimary = Color(0xFF381E72),

    primaryContainer = Purple40,

    onPrimaryContainer = Purple80,


    secondary = PurpleGrey80,

    onSecondary = Color(0xFF332D41),


    tertiary = Pink80,

    onTertiary = Color(0xFF492532),


    background = Color(0xFF1C1B1F),

    surface = Color(0xFF1C1B1F)

)


private val BlueLightColorScheme = lightColorScheme(

    primary = Blue40,

    onPrimary = White,

    primaryContainer = Blue80,

    onPrimaryContainer = Color(0xFF001D36),


    secondary = BlueGrey40,

    onSecondary = White,


    tertiary = Cyan40,

    onTertiary = White,


    background = Color(0xFFFCFCFF),

    surface = Color(0xFFFCFCFF)

)


private val BlueDarkColorScheme = darkColorScheme(

    primary = Blue80,

    onPrimary = Color(0xFF003258),

    primaryContainer = Blue40,

    onPrimaryContainer = Blue80,


    secondary = BlueGrey80,

    onSecondary = Color(0xFF2C3439),


    tertiary = Cyan80,

    onTertiary = Color(0xFF003640),


    background = Color(0xFF1A1C1E),

    surface = Color(0xFF1A1C1E)

)


private val GreenLightColorScheme = lightColorScheme(

    primary = Green40,

    onPrimary = White,

    primaryContainer = Green80,

    onPrimaryContainer = Color(0xFF00210A),


    secondary = GreenGrey40,

    onSecondary = White,


    tertiary = Lime40,

    onTertiary = White,


    background = Color(0xFFFBFDF8),

    surface = Color(0xFFFBFDF8)

)


private val GreenDarkColorScheme = darkColorScheme(

    primary = Green80,

    onPrimary = Color(0xFF00390F),

    primaryContainer = Green40,

    onPrimaryContainer = Green80,


    secondary = GreenGrey80,

    onSecondary = Color(0xFF003731),


    tertiary = Lime80,

    onTertiary = Color(0xFF3E3F00),


    background = Color(0xFF191C19),

    surface = Color(0xFF191C19)

)


private val OrangeLightColorScheme = lightColorScheme(

    primary = Orange40,

    onPrimary = White,

    primaryContainer = Orange80,

    onPrimaryContainer = Color(0xFF2A1500),


    secondary = OrangeGrey40,

    onSecondary = White,


    tertiary = Amber40,

    onTertiary = White,


    background = Color(0xFFFFFBFF),

    surface = Color(0xFFFFFBFF)

)


private val OrangeDarkColorScheme = darkColorScheme(

    primary = Orange80,

    onPrimary = Color(0xFF5F1600),

    primaryContainer = Orange40,

    onPrimaryContainer = Orange80,


    secondary = OrangeGrey80,

    onSecondary = Color(0xFF352F2B),


    tertiary = Amber80,

    onTertiary = Color(0xFF463C00),


    background = Color(0xFF1F1B16),

    surface = Color(0xFF1F1B16)

)


private val PinkLightColorScheme = lightColorScheme(

    primary = Pink40Soft,

    onPrimary = White,

    primaryContainer = Pink80,

    onPrimaryContainer = Color(0xFF3E001D),


    secondary = PinkGrey40,

    onSecondary = White,


    tertiary = Rose40,

    onTertiary = White,


    background = Color(0xFFFFFBFF),

    surface = Color(0xFFFFFBFF)

)


private val PinkDarkColorScheme = darkColorScheme(

    primary = Pink80Soft,

    onPrimary = Color(0xFF55001F),

    primaryContainer = Pink40,

    onPrimaryContainer = Pink80,


    secondary = PinkGrey80,

    onSecondary = Color(0xFF3E2B44),


    tertiary = Rose80,

    onTertiary = Color(0xFF5A0027),


    background = Color(0xFF1E1A1D),

    surface = Color(0xFF1E1A1D)

)


@Composable

fun MeowNotesTheme(

    darkTheme: Boolean = isSystemInDarkTheme(),

    appTheme: AppTheme = AppTheme.PURPLE,

    dynamicColor: Boolean = false,

    content: @Composable () -> Unit

) {

    val colorScheme = when {

        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

            val context = LocalContext.current

            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)

        }

        darkTheme -> when (appTheme) {

            AppTheme.PURPLE -> PurpleDarkColorScheme

            AppTheme.BLUE -> BlueDarkColorScheme

            AppTheme.GREEN -> GreenDarkColorScheme

            AppTheme.ORANGE -> OrangeDarkColorScheme

            AppTheme.PINK -> PinkDarkColorScheme

        }

        else -> when (appTheme) {

            AppTheme.PURPLE -> PurpleLightColorScheme

            AppTheme.BLUE -> BlueLightColorScheme

            AppTheme.GREEN -> GreenLightColorScheme

            AppTheme.ORANGE -> OrangeLightColorScheme

            AppTheme.PINK -> PinkLightColorScheme

        }

    }


    val view = LocalView.current

    if (!view.isInEditMode) {

        SideEffect {

            val window = (view.context as Activity).window

            window.statusBarColor = colorScheme.primary.toArgb()

            WindowCompat.getInsetsController(window, view)

                .isAppearanceLightStatusBars = !darkTheme

        }

    }


    MaterialTheme(

        colorScheme = colorScheme,

        typography = Typography,

        content = content

    )

} 