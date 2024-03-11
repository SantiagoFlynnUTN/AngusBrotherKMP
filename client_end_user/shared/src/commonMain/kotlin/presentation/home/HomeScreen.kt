package presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.angus.designSystem.ui.composable.BpAppBar
import com.angus.designSystem.ui.composable.BpButton
import com.angus.designSystem.ui.composable.BpImageLoader
import com.angus.designSystem.ui.composable.BpToastMessage
import com.angus.designSystem.ui.theme.Theme
import domain.entity.FoodOrder
import domain.entity.TripStatus
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import presentation.auth.login.LoginScreen
import presentation.base.BaseScreen
import presentation.cart.CartScreen
import presentation.chatSupport.ChatSupportScreen
import presentation.composable.BottomSheet
import presentation.composable.ImageSlider
import presentation.composable.MealBottomSheet
import presentation.composable.MealCard
import presentation.composable.modifier.noRippleEffect
import presentation.composable.modifier.roundedBorderShape
import presentation.cuisines.CuisinesScreen
import presentation.home.composable.CartCard
import presentation.home.composable.Circle
import presentation.meals.MealsScreen
import presentation.orderFoodTracking.OrderFoodTrackingScreen
import presentation.restaurants.RestaurantsScreen
import presentation.resturantDetails.Composable.NeedToLoginSheet
import presentation.resturantDetails.RestaurantCuisineUiState
import presentation.resturantDetails.RestaurantScreen
import presentation.taxi.TaxiOrderScreen
import resources.Resources
import util.getNavigationBarPadding
import util.root

class HomeScreen : BaseScreen<
        HomeScreenModel, HomeScreenUiState, HomeScreenUiEffect, HomeScreenInteractionListener,>() {

    @Composable
    override fun Content() {
        initScreen(getScreenModel())
    }

    override fun onEffect(effect: HomeScreenUiEffect, navigator: Navigator) {
        when (effect) {
            is HomeScreenUiEffect.NavigateToMeals -> {
                navigator.root?.push(MealsScreen(effect.cuisineId, effect.cuisineName))
            }

            is HomeScreenUiEffect.NavigateToCuisines -> navigator.root?.push(CuisinesScreen())
            is HomeScreenUiEffect.NavigateToChatSupport -> navigator.root?.push(ChatSupportScreen())
            is HomeScreenUiEffect.NavigateToOrderTaxi -> navigator.root?.push(TaxiOrderScreen())
            is HomeScreenUiEffect.ScrollDownToRecommendedRestaurants -> navigator.root?.push(
                RestaurantsScreen(),
            )

            is HomeScreenUiEffect.NavigateToOfferItem -> println("Navigate to offer item details ${effect.offerId}")
            is HomeScreenUiEffect.NavigateToOrderDetails -> println("Navigate to order details ${effect.orderId}")
            is HomeScreenUiEffect.NavigateToCart -> navigator.root?.push(CartScreen())
            is HomeScreenUiEffect.NavigateLoginScreen -> navigator.root?.push(LoginScreen())
            is HomeScreenUiEffect.NavigateToRestaurantDetails -> navigator.root?.push(
                RestaurantScreen(effect.restaurantId),
            )

            is HomeScreenUiEffect.NavigateToTrackOrder -> navigator.root?.push(
                OrderFoodTrackingScreen(effect.orderId, effect.tripId),
            )

            is HomeScreenUiEffect.NavigateToTrackTaxiRide -> println("navigate to track taxi ride ${effect.tripId}")
        }
    }

    @Composable
    override fun onRender(state: HomeScreenUiState, listener: HomeScreenInteractionListener) {
        println("onRender " +
                "showCart ${state.showCart} " +
                "loggedIn> ${state.isLoggedIn}")
        BottomSheet(
            sheetContent = {
                if (state.showMealSheet) {
                    MealBottomSheet(
                        meal = state.selectedMeal,
                        isLoading = state.isAddToCartLoading,
                        onAddToCart = listener::onAddToCart,
                        onDismissSheet = listener::onDismissSheet,
                        onIncreaseQuantity = listener::onIncreaseMealQuantity,
                        onDecreaseQuantity = listener::onDecreaseMealQuantity,
                    )
                }
                if (state.showLoginSheet) {
                    NeedToLoginSheet(
                        text = Resources.strings.loginToAddToFavourite,
                        onClick = listener::onLoginClicked,
                    )
                }
            },
            sheetBackgroundColor = Theme.colors.background,
            onBackGroundClicked = listener::onDismissSheet,
            sheetState = state.sheetState,
            content = { content(state, listener) },
        )
    }

    @OptIn(
        ExperimentalFoundationApi::class,
        ExperimentalResourceApi::class,
    )
    @Composable
    private fun content(
        state: HomeScreenUiState,
        listener: HomeScreenInteractionListener,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(Theme.colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            stickyHeader {
                GetUserAppBar(state, listener)
            }

            item {
                ImageSlider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onItemClickListener = { listener.onClickOffersSlider(it) },
                    images = state.getOfferImages(),
                )
            }

            item {
                AnimatedVisibility(state.showCart) {
                    CartCard(onClick = { listener.onClickCartCard() })
                }
            }

            item {
                AnimatedVisibility(state.hasLiveOrders && state.isLoggedIn) {
                    InProgressSection(state, listener)
                }
            }

            item {
                AnimatedVisibility(state.isThereLastOrder) {
                    LastOrder(state.lastOrder, listener)
                }
            }

            item {
                LaunchedEffect(state.showSnackBar) {
                    if (state.showSnackBar) {
                        delay(4000)
                        listener.onDismissSnackBar()
                    }
                }
                BpToastMessage(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = getNavigationBarPadding().calculateBottomPadding()),
                    state = state.showSnackBar,
                    message = Resources.strings.accessDeniedMessage,
                    isError = true,
                    successIcon = painterResource(Resources.images.unread),
                    warningIcon = painterResource(Resources.images.warningIcon),
                )
            }

            item {
                state.cuisines.forEach { cuisine ->
                    addCuisineToHome(cuisine, listener)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun GetUserAppBar(state: HomeScreenUiState, listener: HomeScreenInteractionListener) {
        BpAppBar(
            title = if (!state.isLoadingUser && state.isLoggedIn) {
                Resources.strings.welcome + " ${state.user.fullName}"
            } else if (!state.isLoadingUser && !state.isLoggedIn) {
                Resources.strings.loginWelcomeMessage
            } else {
                Resources.strings.welcome
            },
            actions = {
                if (state.isLoggedIn || state.isLoadingUser) {
                    Box(Modifier.heightIn(max = 32.dp).padding(end = 16.dp))
                } else {
                    BpButton(
                        modifier = Modifier.heightIn(max = 32.dp).padding(end = 16.dp),
                        textPadding = PaddingValues(horizontal = 16.dp),
                        title = Resources.strings.login,
                        onClick = listener::onLoginClicked,
                    )
                }
            },
        )
    }

    @Composable
    private fun addCuisineToHome(cuisine: RestaurantCuisineUiState, listener: HomeScreenInteractionListener) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                modifier = Modifier.padding(start = 16.dp, bottom = 12.dp),
                text = cuisine.name,
                style = Theme.typography.titleLarge,
                color = Theme.colors.contentPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            cuisine.meals.forEach {
                MealCard(
                    meal = it,
                    modifier = Modifier.noRippleEffect { listener.onMealSelected(it) }
                        .padding(bottom = 8.dp),
                )
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun InProgressSection(
        state: HomeScreenUiState,
        listener: HomeScreenInteractionListener,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = Resources.strings.inProgress,
                style = Theme.typography.titleLarge.copy(Theme.colors.contentPrimary),
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            // taxi rides
            state.liveOrders.taxiRides.forEach { taxiRideUiState ->
                InProgressCard(
                    painter = painterResource(Resources.images.taxiOnTheWay),
                    titleText = if (taxiRideUiState.rideStatus == TripStatus.APPROVED.statusCode) {
                        Resources.strings.taxiOnTheWay
                    } else {
                        Resources.strings.enjoyYourRide
                    },
                    id = taxiRideUiState.tripId,
                    onClick = listener::onClickActiveTaxiRide,
                    titleTextColor = if (taxiRideUiState.rideStatus == TripStatus.APPROVED.statusCode) {
                        Theme.colors.primary
                    } else {
                        Theme.colors.contentSecondary
                    },
                ) { textStyle ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (taxiRideUiState.rideStatus == TripStatus.APPROVED.statusCode) {
                            Text(text = taxiRideUiState.taxiColor.name, style = textStyle)
                            Circle()
                            Text(text = taxiRideUiState.taxiPlateNumber, style = textStyle)
                            Circle()
                        }
                        Text(
                            text = "${taxiRideUiState.rideEstimatedTime} min to arrive",
                            style = textStyle,
                        )
                    }
                }
            }

            // delivery rides
            state.liveOrders.deliveryOrders.forEach { deliveryOrder ->
                InProgressCard(
                    painter = painterResource(Resources.images.orderOnTheWay),
                    titleText = Resources.strings.orderOnTheWay,
                    titleTextColor = Theme.colors.primary,
                    id = deliveryOrder.tripId,
                    onClick = {
                        listener.onClickActiveFoodOrder(
                            orderId = "",
                            tripId = deliveryOrder.tripId,
                        )
                    },
                ) { textStyle ->
                    Text(
                        text = "From ${deliveryOrder.restaurantName}",
                        style = textStyle,
                    )
                }
            }

            // food orders
            state.liveOrders.foodOrders.forEach { foodOrder ->
                InProgressCard(
                    painter =
                    if (foodOrder.orderStatus == FoodOrder.OrderStatusInRestaurant.PENDING.statusCode) {
                        painterResource(Resources.images.orderImage)
                    } else {
                        painterResource(Resources.images.orderImage)
                    },
                    titleText = if (foodOrder.orderStatus == FoodOrder.OrderStatusInRestaurant.PENDING.statusCode) {
                        Resources.strings.orderPlaced
                    } else {
                        Resources.strings.orderInCooking
                    },
                    id = foodOrder.orderId,
                    onClick = {
                        listener.onClickActiveFoodOrder(
                            orderId = foodOrder.orderId,
                            tripId = "",
                        )
                    },
                ) { textStyle ->
                    val str = foodOrder.meals.joinToString(", ") { it.name }
                    Text(
                        text = str,
                        style = textStyle,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    private fun LastOrder(order: OrderUiState, listener: HomeScreenInteractionListener) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Text(
                Resources.strings.lastOrder,
                style = Theme.typography.titleLarge.copy(color = Theme.colors.contentPrimary),
            )
            Row(modifier = Modifier.fillMaxWidth().height(80.dp).padding(top = 8.dp)) {
                BpImageLoader(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(104.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    imageUrl = order.image,
                    errorPlaceholderImageUrl = Resources.images.restaurantErrorPlaceholder,
                )
                Column(
                    modifier = Modifier.padding(8.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        order.restaurantName,
                        style = Theme.typography.title.copy(color = Theme.colors.contentPrimary),
                    )
                    Text(
                        order.date,
                        style = Theme.typography.body.copy(color = Theme.colors.contentSecondary),
                    )
                    Row(
                        modifier = Modifier.clickable { listener.onClickOrderAgain(order.id) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = Resources.strings.orderAgain,
                            style = Theme.typography.body,
                            color = Theme.colors.primary,
                        )
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(Resources.images.arrowRight),
                            contentDescription = Resources.strings.seeAllDescription,
                            tint = Theme.colors.primary,
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun InProgressCard(
        painter: Painter,
        titleText: String,
        id: String,
        onClick: (id: String) -> Unit,
        modifier: Modifier = Modifier,
        titleTextColor: Color = Theme.colors.primary,
        titleTextStyle: TextStyle = Theme.typography.title.copy(color = titleTextColor),
        captionText: @Composable (TextStyle) -> Unit,
    ) {
        Row(
            modifier = modifier
                .heightIn(min = 72.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .noRippleEffect { onClick(id) }
                .roundedBorderShape()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = titleText,
                    style = titleTextStyle,
                    color = titleTextColor,
                )
                captionText(Theme.typography.caption.copy(color = Theme.colors.contentSecondary))
            }
        }
    }
}
