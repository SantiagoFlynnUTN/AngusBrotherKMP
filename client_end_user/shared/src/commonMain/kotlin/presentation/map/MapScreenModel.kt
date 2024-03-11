package presentation.map

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import presentation.base.BaseScreenModel

class MapScreenModel(

) : BaseScreenModel<MapUiState, MapUiEffect>(MapUiState()), MapInteractionListener {

    override val viewModelScope: CoroutineScope = screenModelScope
    private var searchJob: Job? = null

    init {

    }

    override fun onIncreaseMealQuantity() {
    }

    override fun onDecreaseMealQuantity() {
    }

    override fun onAddToCart() {
    }

}
