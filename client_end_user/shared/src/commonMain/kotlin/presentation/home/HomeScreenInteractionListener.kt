package presentation.home

import presentation.base.BaseInteractionListener
import presentation.resturantDetails.MealInteractionListener
import presentation.resturantDetails.MealUiState

interface HomeScreenInteractionListener : BaseInteractionListener, MealInteractionListener {
    fun onDismissSnackBar()
    fun onDismissSheet()
    fun onClickOrderFood()
    fun onMealSelected(meal: MealUiState)
    fun onClickOffersSlider(position: Int)
    fun onClickOrderAgain(orderId: String)
    fun onLoginClicked()
    fun onClickCartCard()
    fun onClickRestaurantCard(restaurantId: String)
    fun onClickActiveFoodOrder(orderId: String, tripId: String)
    fun onClickActiveTaxiRide(tripId: String)
}
