package presentation.cart

import cafe.adriel.voyager.core.model.screenModelScope
import data.gateway.local.LocalConfigurationGateway
import domain.entity.Cart
import domain.entity.FoodOrder
import domain.usecase.ManageCartUseCase
import domain.usecase.TrackOrdersUseCase
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import presentation.base.BaseScreenModel
import presentation.base.ErrorState
import kotlin.time.Duration

class CartScreenModel(private val cartManagement: ManageCartUseCase, private val trackOrdersUseCase: TrackOrdersUseCase, private val localConfigurationGateway: LocalConfigurationGateway) :
    BaseScreenModel<CartUiState, CartUiEffect>(CartUiState()),
    CartInteractionListener {
    override val viewModelScope = screenModelScope

    init {
        getCartMeals()
    }

    // region getting cart meals
    private fun getCartMeals() {
        tryToExecute(
            cartManagement::getCart,
            ::onGetCartMealsSuccess,
            ::onError,
        )
    }

    private fun onGetCartMealsSuccess(cart: Cart) {
        val cartUiState = cart.toUiState()
        updateState {
            it.copy(
                meals = cartUiState.meals,
                currency = cartUiState.currency,
            )
        }
    }
    // endregion

    // region saving cart
    override fun onDispose() {
        tryToExecute(
            { cartManagement.updateCart(state.value.toEntity()) },
            {},
            ::onError,
        )
        super.onDispose()
    }

    private fun orderNow() {
        updateState { it.copy(isOrderNowLoading = true, orderError = null) }
        tryToExecute(cartManagement::orderNow, ::onOrderNowSuccess, ::onOrderError)
    }

    private fun onOrderNowSuccess(success: Boolean) {
        updateState { it.copy(isOrderNowLoading = false, orderError = null) }

        sendNewEffect(CartUiEffect.NavigateUp)
    }
    // endregion

    // region interactions
    override fun onClickPlus(index: Int, count: Int) {
        val updatedCount = if (count < 99) count + 1 else count
        val meal = state.value.meals[index].copy(count = updatedCount)
        updateState {
            it.copy(
                meals = it.meals
                    .mapIndexed { mealIndex, cartMealUiState ->
                        if (mealIndex == index) meal else cartMealUiState
                    },
            )
        }
    }

    override fun onClickMinus(index: Int, count: Int) {
        val updatedCount = if (count > 1) count - 1 else count
        val meal = state.value.meals[index].copy(count = updatedCount)
        updateState {
            it.copy(
                meals = it.meals
                    .mapIndexed { mealIndex, cartMealUiState ->
                        if (mealIndex == index) meal else cartMealUiState
                    },
            )
        }
    }

    override fun onClickOrderNow() {
        orderNow()
    }

    override fun onDismissSnackBar() {
        updateState { it.copy(showToast = false) }
    }

    override fun onClickBack() {
        sendNewEffect(CartUiEffect.NavigateUp)
    }
    // endregion

    private fun onError(error: ErrorState) {
    }

    private fun onOrderError(error: ErrorState) {
        updateState { it.copy(isOrderNowLoading = false, orderError = error) }
        when (error) {
            is ErrorState.RestaurantClosed -> {
                println("RestaurantClosed")
                updateState { it.copy(showToast = true) }
            }
            is ErrorState.NoInternet -> {
            }

            else -> {}
        }
    }
}
