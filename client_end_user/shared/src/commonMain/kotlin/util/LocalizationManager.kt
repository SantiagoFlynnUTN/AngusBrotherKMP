package util

import resources.strings.IStringResources
import resources.strings.en.English
import resources.strings.en.Spanish


object LocalizationManager {

    fun getStringResources(languageCode: LanguageCode): IStringResources {
        return when (languageCode) {
            LanguageCode.EN -> English()
            LanguageCode.ES -> Spanish()
        }
    }
}
