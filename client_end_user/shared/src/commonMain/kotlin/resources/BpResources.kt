package resources

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.angus.designSystem.ui.theme.BpTheme
import com.seiko.imageloader.LocalImageLoader
import resources.strings.IStringResources
import util.LanguageCode
import util.LocalizationManager
import util.generateImageLoader
import util.getPlatformContext
import util.setInsetsController

private val localDrawableResources = staticCompositionLocalOf { DrawableResources() }
private val localStringResources = staticCompositionLocalOf<IStringResources> {
    error("CompositionLocal IStringResources not present")
}

@Composable
fun AngusTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    languageCode: LanguageCode,
    content: @Composable () -> Unit,
) {
    val drawableResources = if (useDarkTheme) BpDrawableDarkResources else DrawableResources()
    val context = getPlatformContext()

    CompositionLocalProvider(
        localDrawableResources provides drawableResources,
        localStringResources provides LocalizationManager.getStringResources(languageCode),
        LocalImageLoader provides remember { generateImageLoader(context) }
    ) {
        BpTheme(useDarkTheme = useDarkTheme) {
            setInsetsController(useDarkTheme)
            content()
        }
    }
}

object Resources {
    val images: DrawableResources
        @Composable
        @ReadOnlyComposable
        get() = localDrawableResources.current

    val strings: IStringResources
        @Composable
        @ReadOnlyComposable
        get() = localStringResources.current
}
