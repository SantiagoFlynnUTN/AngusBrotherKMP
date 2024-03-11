package presentation.home

import cafe.adriel.voyager.core.model.screenModelScope
import domain.entity.Cuisine
import domain.entity.DeliveryRide
import domain.entity.FoodOrder
import domain.entity.Offer
import domain.entity.Restaurant
import domain.entity.TaxiRide
import domain.entity.Trip
import domain.entity.TripStatus
import domain.entity.User
import domain.usecase.IExploreRestaurantUseCase
import domain.usecase.IGetOffersUseCase
import domain.usecase.IGetUserLocationUseCase
import domain.usecase.IManageAuthenticationUseCase
import domain.usecase.IManageCartUseCase
import domain.usecase.IManageFavouriteUseCase
import domain.usecase.IManageProfileUseCase
import domain.usecase.ITrackOrdersUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import presentation.base.BaseScreenModel
import presentation.base.ErrorState
import presentation.cuisines.CuisineUiState
import presentation.cuisines.toCuisineUiState
import presentation.resturantDetails.MealUiState
import presentation.resturantDetails.toRestaurantCuisineUiState

class HomeScreenModel(
    private val exploreRestaurant: IExploreRestaurantUseCase,
    private val offers: IGetOffersUseCase,
    private val trackOrders: ITrackOrdersUseCase,
    private val manageCart: IManageCartUseCase,
    private val manageFavorite: IManageFavouriteUseCase,
    private val manageProfile: IManageProfileUseCase,
    private val getUserLocation: IGetUserLocationUseCase,
    private val manageAuthentication: IManageAuthenticationUseCase,
) : BaseScreenModel<HomeScreenUiState, HomeScreenUiEffect>(HomeScreenUiState()),
    HomeScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        checkIfLoggedIn()
        getRecommendedCuisines()
        getNewOffers()
        getHomeMealsMenu()
    }

    private fun getLiveOrders() {
        screenModelScope.launch {
            val taxiTripsDeferred = async { getActiveTaxiTrips() }
            val deliveryTripsDeferred = async { getActiveDeliveryTrips() }
            val foodOrdersDeferred = async { getActiveFoodOrders() }

            // Handle each result independently as soon as it is available.
            launch { taxiTripsDeferred.await() }
            launch { deliveryTripsDeferred.await() }
            launch { foodOrdersDeferred.await() }
        }
    }

    private fun trackingAndUpdateTaxiRide(tripId: String) {
        tryToCollect(
            { trackOrders.trackTaxiRide(tripId) },
            ::onGetTaxiRidesSuccess,
            ::onTrackingError,
        )
    }

    private fun trackingAndUpdateDeliveryRide(tripId: String) {
        tryToCollect(
            { trackOrders.trackDeliveryRide(tripId) },
            ::onGetDeliveryRidesSuccess,
            ::onTrackingError,
        )
    }

    private fun trackingAndUpdateFoodOrderFromRestaurant(orderId: String) {
        tryToCollect(
            { trackOrders.trackFoodOrderInRestaurant(orderId) },
            ::onGetFoodOrdersSuccess,
            ::onTrackingError,
        )
    }

    private fun onGetTaxiRidesSuccess(ride: TaxiRide) {
        updateState { it.copy(isLoading = false) }
        updateState { homeScreenUiState ->
            val currentTaxiRides = homeScreenUiState.liveOrders.taxiRides
            val updatedTaxiRides = currentTaxiRides.mapNotNull { taxiRideUiState ->
                if (taxiRideUiState.tripId == ride.id) {
                    if (ride.tripStatus.statusCode == TripStatus.FINISHED.statusCode) {
                        currentTaxiRides - ride.toTaxiRideUiState()
                        null
                    } else {
                        ride.toTaxiRideUiState()
                    }
                } else {
                    taxiRideUiState
                }
            }
            homeScreenUiState.copy(liveOrders = homeScreenUiState.liveOrders.copy(taxiRides = updatedTaxiRides))
        }
    }

    private fun onGetDeliveryRidesSuccess(ride: DeliveryRide) {
        updateState { it.copy(isLoading = false) }
        updateState { homeScreenUiState ->
            val currentDeliveryRides = homeScreenUiState.liveOrders.deliveryOrders
            val updatedDeliveryRides = currentDeliveryRides.mapNotNull { deliveryRideUiState ->
                if (deliveryRideUiState.tripId == ride.id) {
                    if (ride.tripStatus.statusCode == TripStatus.FINISHED.statusCode) {
                        currentDeliveryRides - ride.toDeliveryOrderUiState()
                        null
                    } else {
                        ride.toDeliveryOrderUiState()
                    }
                } else {
                    deliveryRideUiState
                }
            }
            homeScreenUiState.copy(liveOrders = homeScreenUiState.liveOrders.copy(deliveryOrders = updatedDeliveryRides))
        }
    }

    private fun onGetFoodOrdersSuccess(order: FoodOrder) {
        println("onGetFoodOrdersSuccess: WS -> ${order.orderStatus}")
        updateState { it.copy(isLoading = false) }
        updateState { homeScreenUiState ->
            val currentFoodOrders = homeScreenUiState.liveOrders.foodOrders
            val updatedFoodOrders = currentFoodOrders.mapNotNull { foodOrderUiState ->
                if (foodOrderUiState.orderId == order.id) {
                    if (order.orderStatus.statusCode == TripStatus.FINISHED.statusCode) {
                        currentFoodOrders - order.toFoodOrderUiState()
                        null
                    } else {
                        order.toFoodOrderUiState()
                    }
                } else {
                    foodOrderUiState
                }
            }
            homeScreenUiState.copy(liveOrders = homeScreenUiState.liveOrders.copy(foodOrders = updatedFoodOrders))
        }
    }

    private fun getActiveTaxiTrips() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            { trackOrders.getActiveTaxiTrips() },
            ::onGetActiveTaxiTripsSuccess,
            ::onTrackingError,
        )
    }

    private fun getActiveDeliveryTrips() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            { trackOrders.getActiveDeliveryTrips() },
            ::onGetActiveDeliveryTripsSuccess,
            ::onTrackingError,
        )
    }

    private fun getActiveFoodOrders() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            { trackOrders.getActiveFoodOrders() },
            ::onGetActiveFoodOrdersSuccess,
            ::onTrackingError,
        )
    }

    private fun onGetActiveTaxiTripsSuccess(taxiTrips: List<Trip>) {
        updateState { homeScreenState ->
            homeScreenState.copy(
                liveOrders = homeScreenState.liveOrders.copy(
                    taxiRides = taxiTrips.map { trip -> trip.toTaxiRideUiState() },
                ),
            )
        }
        val currentTaxiRides = state.value.liveOrders.taxiRides
        if (currentTaxiRides.isNotEmpty()) {
            currentTaxiRides.forEach { taxiRide ->
                trackingAndUpdateTaxiRide(taxiRide.tripId)
            }
        }
    }

    private fun onGetActiveDeliveryTripsSuccess(deliveryTrips: List<DeliveryRide>) {
        updateState { homeScreenState ->
            homeScreenState.copy(
                liveOrders = homeScreenState.liveOrders.copy(
                    deliveryOrders = deliveryTrips.map { trip -> trip.toDeliveryOrderUiState() },
                ),
            )
        }

        val currentDeliveryRides = state.value.liveOrders.deliveryOrders
        if (currentDeliveryRides.isNotEmpty()) {
            currentDeliveryRides.forEach { deliveryRide ->
                trackingAndUpdateDeliveryRide(deliveryRide.tripId)
            }
        }
    }

    private fun onGetActiveFoodOrdersSuccess(foodOrders: List<FoodOrder>) {
        updateState { homeScreenState ->
            homeScreenState.copy(
                liveOrders = homeScreenState.liveOrders.copy(
                    foodOrders = foodOrders.map { order -> order.toFoodOrderUiState() },
                ),
            )
        }

        val currentFoodOrders = state.value.liveOrders.foodOrders
        if (currentFoodOrders.isNotEmpty()) {
            currentFoodOrders.forEach { order ->
                trackingAndUpdateFoodOrderFromRestaurant(order.orderId)
            }
        }
    }

    private fun checkIfLoggedIn() {
        tryToExecute(
            { manageAuthentication.getAccessToken() },
            ::onCheckIfLoggedInSuccess,
            ::onErrorLoadingUser,
        )
    }

    private fun onCheckIfLoggedInSuccess(accessToken: Flow<String>) {
        screenModelScope.launch {
            accessToken.distinctUntilChanged().collectLatest { token ->
                if (token.isNotEmpty()) {
                    updateState { it.copy(isLoggedIn = true, isLoadingUser = false) }
                    getUser()
                    getFavoriteRestaurants()
                    checkCartHasMeals()
                    getLiveOrders()
                } else {
                    println("onRender else")
                    updateState { it.copy(isLoggedIn = false, isLoadingUser = false, showCart = false) }
                }
            }
        }
    }

    private fun onErrorLoadingUser(errorState: ErrorState) {
        println("onRender onErrorLoadingUser")
        updateState { it.copy(isLoggedIn = false) }
        updateState { it.copy(isLoadingUser = false) }
    }

    private fun onTrackingError(errorState: ErrorState) {
        if (errorState is ErrorState.SocketClosed) {
            println("Socket Closed $errorState")
        }
        updateState { it.copy(isLoading = false) }
    }

    private fun checkCartHasMeals() {
        println("checkCartHasMeals")
        tryToExecute(
            { manageCart.isCartEmpty() },
            ::onGetCartSuccess,
            ::onGetCartError,
        )
    }

    private fun onGetCartSuccess(isCartEmpty: Flow<Boolean>) {
        println("checkCartHasMeals onGetCartSuccess")
        screenModelScope.launch {
            isCartEmpty.distinctUntilChanged().collectLatest { isEmpty ->
                println("checkCartHasMeals onGetCartSuccess distinctUntilChanged")
                if (isEmpty) {
                    getLiveOrders()
                }
                updateState { it.copy(showCart = !isEmpty) }
            }
        }
    }

    private fun onGetCartError(errorState: ErrorState) {
        updateState { it.copy(showCart = false) }
    }

    private fun getUser() {
        tryToExecute(
            { manageProfile.getUserProfile() },
            ::onGetUserSuccess,
            ::onGetUserError,
        )
    }

    private fun onGetUserError(errorState: ErrorState) {
        println("onRender onErrorLoadingUser")
        updateState { it.copy(isLoggedIn = false) }
    }

    private fun onGetUserSuccess(user: User) {
        updateState { it.copy(user = user.toUIState()) }
    }

    override fun onDismissSnackBar() {
        updateState { it.copy(showSnackBar = false) }
    }

    override fun onDismissSheet() {
        state.value.sheetState.dismiss()
        updateState { it.copy(showLoginSheet = false, showMealSheet = false) }
    }

    override fun onClickOrderFood() {
        sendNewEffect(HomeScreenUiEffect.ScrollDownToRecommendedRestaurants)
    }

    override fun onMealSelected(meal: MealUiState) {
        if (!state.value.showMealSheet) {
            state.value.sheetState.show()
            updateState { it.copy(showMealSheet = true, selectedMeal = meal) }
        }
    }

    override fun onClickOffersSlider(position: Int) {
        screenModelScope.launch {
            val newOffers = offers.getNewOffers()
            if (position in newOffers.indices) {
                val id = newOffers[position].id
                sendNewEffect(HomeScreenUiEffect.NavigateToOfferItem(id))
            }
        }
    }

    override fun onClickOrderAgain(orderId: String) {
        sendNewEffect(HomeScreenUiEffect.NavigateToOrderDetails(orderId))
    }

    override fun onLoginClicked() {
        sendNewEffect(HomeScreenUiEffect.NavigateLoginScreen)
    }

    override fun onClickCartCard() {
        sendNewEffect(HomeScreenUiEffect.NavigateToCart)
    }

    override fun onClickRestaurantCard(restaurantId: String) {
        sendNewEffect(HomeScreenUiEffect.NavigateToRestaurantDetails(restaurantId))
    }

    override fun onClickActiveFoodOrder(orderId: String, tripId: String) {
        sendNewEffect(HomeScreenUiEffect.NavigateToTrackOrder(orderId, tripId))
    }

    override fun onClickActiveTaxiRide(tripId: String) {
        startTrackUserLocation(tripId)
    }

    override fun onIncreaseMealQuantity() {
        val quantity = state.value.selectedMeal.quantity + 1
        updateState {
            it.copy(
                selectedMeal = state.value.selectedMeal.copy(
                    quantity = quantity,
                    totalPrice = state.value.selectedMeal.price * quantity,
                ),
            )
        }
    }

    override fun onDecreaseMealQuantity() {
        if (state.value.selectedMeal.quantity == 1) return
        updateState {
            val quantity = state.value.selectedMeal.quantity - 1
            it.copy(
                selectedMeal = state.value.selectedMeal.copy(
                    quantity = quantity,
                    totalPrice = state.value.selectedMeal.price * quantity,
                ),
            )
        }
    }

    override fun onAddToCart() {
        if (state.value.isLoggedIn) {
            updateState { it.copy(isAddToCartLoading = true) }
            tryToExecute(
                {
                    println("onAddToCart AAAAAAAA ${state.value.selectedMeal.restaurantId}")

                    manageCart.addMealTCart(
                        restaurantId = state.value.selectedMeal.restaurantId,
                        quantity = state.value.selectedMeal.quantity,
                        mealId = state.value.selectedMeal.id,
                    )
                },
                ::onAddToCartSuccess,
                ::onAddToCartError,
            )
        } else {
            updateState { it.copy(showMealSheet = false, showLoginSheet = true) }
        }
    }

    private fun onAddToCartSuccess(success: Boolean) {
        updateState { it.copy(isAddToCartLoading = false, errorAddToCart = null) }
        onDismissSheet()
        updateState { it.copy(showToast = true) }
    }

    private fun onAddToCartError(errorState: ErrorState) {
        when (errorState) {
            is ErrorState.CartIsFull -> {
                updateState { it.copy(isAddToCartLoading = false, showWarningCartIsFull = true, errorAddToCart = errorState) }
            }
            else -> {
                updateState { it.copy(error = errorState) }
            }
        }
    }

    private fun startTrackUserLocation(tripId: String) {
        tryToExecute(
            function = getUserLocation::startTracking,
            onSuccess = { onStartTrackUserLocationSuccess(tripId) },
            onError = ::onStartTrackUserLocationError,
        )
    }

    private fun onStartTrackUserLocationSuccess(tripId: String) {
        sendNewEffect(HomeScreenUiEffect.NavigateToTrackTaxiRide(tripId))
    }

    private fun onStartTrackUserLocationError(errorState: ErrorState) {
        when (errorState) {
            ErrorState.LocationPermissionDenied -> updateState { it.copy(showSnackBar = true) }
            else -> {}
        }
    }

    private fun getRecommendedCuisines() {
        tryToExecute(
            { exploreRestaurant.getPreferredCuisines().toCuisineUiState() },
            ::onGetCuisinesSuccess,
            ::onGetCuisinesError,
        )
    }

    private fun onGetCuisinesSuccess(cuisines: List<CuisineUiState>) {
        updateState { it.copy(recommendedCuisines = cuisines) }
    }

    private fun onGetCuisinesError(error: ErrorState) {
        println("error is $error")
    }

    private fun getFavoriteRestaurants() {
        tryToExecute(
            { manageFavorite.getFavoriteRestaurants() },
            ::onGetFavoriteRestaurantsSuccess,
            ::onGetFavoriteRestaurantsError,
        )
    }

    private fun onGetFavoriteRestaurantsSuccess(restaurants: List<Restaurant>) {
        updateState { it.copy(favoriteRestaurants = restaurants.toRestaurantUiState()) }
    }

    private fun onGetFavoriteRestaurantsError(error: ErrorState) {
        println("error is $error")
    }

    private fun getNewOffers() {
        tryToExecute(
            { offers.getNewOffers() },
            ::onGetNewOffersSuccess,
            ::onGetNewOffersError,
        )
    }

    private fun onGetNewOffersSuccess(offers: List<Offer>) {
        updateState { it.copy(offers = offers.toUiState()) }
    }

    private fun onGetNewOffersError(error: ErrorState) {
        println("error is $error")
    }

    private fun getHomeMealsMenu() {
        tryToExecute(
            { exploreRestaurant.getMealsHomePage() },
            ::onGetHomeMealsSuccess,
            ::onGetNewOffersError,
        )
    }

    private fun onGetHomeMealsSuccess(cuisines: List<Cuisine>) {
        updateState { it.copy(cuisines = cuisines.toRestaurantCuisineUiState()) }
    }
}
