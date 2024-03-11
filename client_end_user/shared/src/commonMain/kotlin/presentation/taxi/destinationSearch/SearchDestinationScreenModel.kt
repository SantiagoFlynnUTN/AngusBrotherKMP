package presentation.taxi.destinationSearch

import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import presentation.base.BaseScreenModel
import presentation.base.ErrorState

class SearchDestinationScreenModel() : BaseScreenModel<SearchDestinationUiState, SearchDestinationUiEffect>(SearchDestinationUiState()), SearchDestinationInteractionListener {

    override val viewModelScope: CoroutineScope = screenModelScope
    private var searchJob: Job? = null

    init {

    }


    override fun onSearchInputChanged(keyword: String) {
        updateState { it.copy(query = keyword) }
        launchSearchJob()
    }


    private fun launchSearchJob() {
        searchJob?.cancel()
//        searchJob = launchDelayed(300L) { this@SearchDestinationScreenModel.getMealAndRestaurant() }
    }

    private fun onError(error: ErrorState) {
        updateState { it.copy(error = error) }
    }

}
