package presentation.meals

import presentation.base.BaseInteractionListener
import presentation.resturantDetails.MealInteractionListener
import presentation.resturantDetails.MealUiState

interface MealsInteractionListener : BaseInteractionListener, MealInteractionListener {
    fun onMealClicked(meal: MealUiState)
    fun onDismissSheet()
    fun onBackClicked()
    fun onLoginClicked()
    fun onGoToCart()
    fun onDismissDialog()
    fun onClearCart()
    fun onDismissSnackBar()
}
