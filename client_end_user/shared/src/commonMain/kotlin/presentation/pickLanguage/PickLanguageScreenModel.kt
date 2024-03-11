package presentation.pickLanguage

import cafe.adriel.voyager.core.model.screenModelScope
import domain.usecase.IManageSettingUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class PickLanguageScreenModel(
    private val userPreferences: IManageSettingUseCase,
) : BaseScreenModel<PickLanguageUIState, PickLanguageUIEffect>(PickLanguageUIState()),
    PickLanguageInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope


    override fun onLanguageSelected(language: LanguageUIState) {
        tryToExecute(
            { userPreferences.saveLanguageCode(language.code) },
            ::onSavedLanguageSuccess,
            ::onError
        )
        tryToExecute(
            { userPreferences.saveIsFirstTimeUseApp(false) },
            {},
            ::onError
        )
    }

    private fun onSavedLanguageSuccess(unit: Unit) {
        sendNewEffect(PickLanguageUIEffect.onGoToPreferredLanguage)
    }

    private fun onError(errorState: ErrorState) {
        println(errorState)
    }
}
