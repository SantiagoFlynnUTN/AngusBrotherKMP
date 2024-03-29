package presentation.preferredRide

import cafe.adriel.voyager.core.model.screenModelScope
import domain.entity.Cost
import domain.entity.PreferredRide
import domain.entity.RideQuality
import domain.usecase.IManageSettingUseCase
import kotlinx.coroutines.CoroutineScope
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class PreferredRideScreenModel(
    private val userPreferences: IManageSettingUseCase,
) : BaseScreenModel<PreferredRideUiState, PreferredRideUiEffect>
    (PreferredRideUiState()), PreferredRideInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    override fun onClickPreferredRide(quality: RideQuality) {
        when (quality) {
            RideQuality.LOW -> updateState { it.copy(cost = Cost.LOW, quality = RideQuality.LOW) }

            RideQuality.HIGH -> updateState {
                it.copy(
                    cost = Cost.HIGH,
                    quality = RideQuality.HIGH
                )
            }
        }
        savePreferredRide(state.value.toPreferredRide())
    }

    private fun savePreferredRide(preferredRide: PreferredRide) {
        tryToExecute(
            { userPreferences.savePreferredRide(preferredRide) },
            ::onSuccess,
            ::onError
        )
    }

    private fun onSuccess(unit: Unit) {
        sendNewEffect(PreferredRideUiEffect.NavigateToPreferredMeal)
    }

    private fun onError(errorState: ErrorState) {
        println("$errorState")
    }
}
