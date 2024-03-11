package presentation.meals

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import presentation.base.ErrorState
import presentation.resturantDetails.MealUiState


data class MealsUiState(
    val cuisineName: String = "",
    val meals: Flow<PagingData<MealUiState>> = emptyFlow(),
    val showWarningCartIsFull : Boolean = false,
    val showToastClearCart : Boolean = false,
    val showMealSheet: Boolean = false,
    val selectedMeal: MealUiState = MealUiState(),

    val isLogin: Boolean = false,
    val showLoginSheet: Boolean = false,
    val isAddToCartLoading: Boolean = false,
    val showToast: Boolean = false,

    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val errorAddToCart: ErrorState? = null,
)
