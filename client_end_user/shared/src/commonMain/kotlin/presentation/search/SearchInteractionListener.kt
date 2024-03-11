package presentation.search

import presentation.base.BaseInteractionListener
import presentation.resturantDetails.MealInteractionListener
import presentation.resturantDetails.MealUiState

interface SearchInteractionListener : BaseInteractionListener , MealInteractionListener{
    fun onSearchInputChanged(keyword: String)
    fun onRestaurantClicked(restaurantId: String)
    fun onMealClicked(meal: MealUiState)
    fun onDismissSheet()
    fun onLoginClicked()
    fun onGoToCart()
    fun onDismissDialog()
    fun onClearCart()
    fun onDismissSnackBar()
}
