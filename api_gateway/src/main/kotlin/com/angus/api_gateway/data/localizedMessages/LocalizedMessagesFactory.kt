package com.angus.api_gateway.data.localizedMessages

import org.koin.core.annotation.Single
import com.angus.api_gateway.data.localizedMessages.languages.*

@Single
class LocalizedMessagesFactory {
    fun createLocalizedMessages(languageCode: String): LocalizedMessages {
        return map[languageCode.uppercase()] ?: EnglishLocalizedMessages()
    }
}

private val map = mapOf(
    Language.ENGLISH.code to EnglishLocalizedMessages(),
)

enum class Language(val code: String) {
    ENGLISH("EN"),
}
