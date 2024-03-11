package presentation.taxi.destinationSearch

import presentation.base.ErrorState


data class SearchDestinationUiState(
    val query: String = "",
    val error: ErrorState? = null,
)




