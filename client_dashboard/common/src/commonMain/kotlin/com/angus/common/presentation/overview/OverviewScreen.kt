package com.angus.common.presentation.overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import com.angus.designSystem.ui.composable.BpOutlinedButton
import com.angus.designSystem.ui.composable.modifier.shimmerEffect
import com.angus.designSystem.ui.theme.Theme
import com.angus.common.presentation.base.BaseScreen
import com.angus.common.presentation.composables.BpNoInternetConnection
import com.angus.common.presentation.composables.OverviewDropDown
import com.angus.common.presentation.main.RestaurantsTab
import com.angus.common.presentation.main.TaxisTab
import com.angus.common.presentation.main.UsersTab
import com.angus.common.presentation.resources.Resources
import com.angus.common.presentation.util.kms

object OverviewScreen :
    BaseScreen<OverviewScreenModel, OverviewUiEffect, OverviewUiState, OverviewInteractionListener>() {

    override fun onEffect(effect: OverviewUiEffect, navigator: Navigator) {

    }

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OverviewScreenModel>()
        Init(screenModel)
        LaunchNavigationEffect(screenModel)
    }

    @Composable
    override fun OnRender(state: OverviewUiState, listener: OverviewInteractionListener) {

        BpNoInternetConnection(hasConnection = !state.hasInternetConnection) {
            listener.onRetry()
        }

        AnimatedContent(targetState = state.isLoading) { isLoading ->
            if (isLoading) OverviewScreenLoading()
            else if (state.hasInternetConnection) OverviewScreenContent(state, listener)
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun OverviewScreenContent(
        state: OverviewUiState,
        listener: OverviewInteractionListener,
        modifier: Modifier = Modifier
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = modifier
                .background(Theme.colors.surface)
                .padding(paddingValues = PaddingValues(horizontal = 40.kms))
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.kms),
        ) {

            RevenueCard(
                listener = listener,
                state = state,
                modifier = Modifier.padding(paddingValues = PaddingValues(top = 40.kms))
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 40.kms)
                    .height(380.dp),
                horizontalArrangement = Arrangement.spacedBy(24.kms)
            ) {
                OverviewCard(
                    modifier = Modifier.fillMaxHeight().background(Theme.colors.surface)
                        .weight(1f)
                        .border(
                            1.kms,
                            Theme.colors.divider,
                            RoundedCornerShape(Theme.radius.medium)
                        ),
                    title = Resources.Strings.taxiLabel,
                    onLeadingButtonClicked = listener::onViewMoreTaxiClicked,
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    content = {
                        TaxiChart(
                            tripsRevenueShare = listOf(
                                PieChartData(
                                    partName = Resources.Strings.accepted,
                                    data = state.tripsRevenueShare.acceptedTrips,
                                    color = Theme.colors.primary,
                                ),
                                PieChartData(
                                    partName = Resources.Strings.pending,
                                    data = state.tripsRevenueShare.pendingTrips,
                                    color = Theme.colors.primary.copy(alpha = 0.5f),
                                ),
                                PieChartData(
                                    partName = Resources.Strings.rejected,
                                    data = state.tripsRevenueShare.rejectedTrips,
                                    color = Theme.colors.primary.copy(alpha = 0.3f),
                                ),
                                PieChartData(
                                    partName = Resources.Strings.canceled,
                                    data = state.tripsRevenueShare.canceledTrips,
                                    color = Theme.colors.primary.copy(alpha = 0.1f),
                                ),
                            )
                        )
                    }
                )
                OverviewCard(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .background(Theme.colors.surface)
                        .border(
                            1.kms,
                            Theme.colors.divider,
                            RoundedCornerShape(Theme.radius.medium)
                        ),
                    title = Resources.Strings.restaurantLabel,
                    onLeadingButtonClicked = listener::onViewMoreRestaurantClicked,
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    content = {
                        RestaurantsChart(
                            ordersRevenueShare = listOf(
                                PieChartData(
                                    partName = Resources.Strings.completed,
                                    data = state.ordersRevenueShare.completedOrders,
                                    color = Theme.colors.primary,
                                ),
                                PieChartData(
                                    partName = Resources.Strings.inTheWay,
                                    data = state.ordersRevenueShare.inTheWayOrders,
                                    color = Theme.colors.primary.copy(alpha = 0.5f),
                                ),
                                PieChartData(
                                    partName = Resources.Strings.canceled,
                                    data = state.ordersRevenueShare.canceledOrders,
                                    color = Theme.colors.primary.copy(alpha = 0.1f),
                                ),
                            )
                        )
                    }
                )
                OverviewCard(
                    modifier = Modifier.weight(1f)
                        .background(Theme.colors.surface)
                        .border(
                            1.kms,
                            Theme.colors.divider,
                            RoundedCornerShape(Theme.radius.medium)
                        ),
                    title = Resources.Strings.users,
                    onLeadingButtonClicked = listener::onViewMoreUsersClicked,
                    content = {
                        Column(
                            modifier = Modifier.weight(1f).fillMaxWidth()
                                .verticalScroll(scrollState)
                        ) {
                            state.users.forEachIndexed { index, user ->
                                Row(
                                    modifier = Modifier.padding(16.kms),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Image(
                                        modifier = Modifier.size(40.kms),
                                        painter = painterResource(user.image),
                                        contentDescription = null
                                    )
                                    Text(
                                        text = user.name,
                                        style = Theme.typography.body.copy(color = Theme.colors.contentPrimary),
                                        modifier = Modifier.padding(start = 16.kms)
                                            .weight(1f),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    FlowRow(
                                        modifier = Modifier,
                                        horizontalArrangement = Arrangement.spacedBy(8.kms)
                                    ) {
                                        user.permission.forEach {
                                            Icon(
                                                painter = painterResource(
                                                    getPermissionIcon(it)
                                                ),
                                                contentDescription = null,
                                                tint = Theme.colors.contentPrimary.copy(
                                                    alpha = 0.87f
                                                ),
                                                modifier = Modifier.size(24.kms)
                                            )
                                        }
                                    }
                                }
                                if (index != state.users.lastIndex) {
                                    Divider(
                                        modifier = Modifier,
                                        color = Theme.colors.divider
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun LaunchNavigationEffect(screenModel: OverviewScreenModel) {
        val tabNavigator = LocalTabNavigator.current
        LaunchedEffect(Unit) {
            collectNavigationEffect(screenModel, tabNavigator)
        }
    }

    private suspend fun collectNavigationEffect(
        screenModel: OverviewScreenModel,
        tabNavigator: TabNavigator
    ) {
        screenModel.effect.collect {
            it.let {
                when (it) {
                    OverviewUiEffect.ViewMoreUsers -> {
                        tabNavigator.current = UsersTab
                    }

                    OverviewUiEffect.ViewMoreRestaurant -> {
                        tabNavigator.current = RestaurantsTab
                    }

                    /*OverviewUiEffect.ViewMoreTaxis -> {
                        tabNavigator.current = TaxisTab
                    }*/
                }

            }
        }
    }

    @Composable
    private fun OverviewCard(
        title: String,
        onLeadingButtonClicked: () -> Unit,
        paddingValues: PaddingValues = PaddingValues(start = 24.kms, top = 24.kms, end = 24.kms),
        modifier: Modifier = Modifier,
        verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.kms),
        content: @Composable ColumnScope.() -> Unit,
    ) {
        Column(
            modifier = modifier
                .background(Theme.colors.surface, RoundedCornerShape(8.kms))
                .padding(paddingValues)
                .defaultMinSize(minHeight = 280.dp, minWidth = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = verticalArrangement
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = Theme.typography.headline,
                    color = Theme.colors.contentPrimary,
                    modifier = Modifier.weight(2f)
                )

                BpOutlinedButton(
                    shape = RoundedCornerShape(Theme.radius.small),
                    title = Resources.Strings.viewMore,
                    onClick = onLeadingButtonClicked,
                    textStyle = Theme.typography.body,
                    textPadding = PaddingValues(0.kms),
                    modifier = Modifier.weight(1f).height(32.dp),
                )
            }
            content()
        }
    }

    @Composable
    private fun RevenueCard(
        listener: OverviewInteractionListener,
        state: OverviewUiState,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .background(Theme.colors.surface)
                .border(1.kms, Theme.colors.divider, RoundedCornerShape(Theme.radius.medium))
                .padding(24.kms),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.kms)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Resources.Strings.revenueShare,
                    style = Theme.typography.headline,
                    color = Theme.colors.contentPrimary,
                    modifier = Modifier
                )
                OverviewDropDown(
                    listener::onMenuItemDropDownClicked,
                    listener::onDismissDropDownMenu,
                    listener::onMenuItemClicked,
                    items = state.dropdownMenuState.items,
                    selectedIndex = state.dropdownMenuState.selectedIndex,
                    isExpanded = state.dropdownMenuState.isExpanded,
                )
            }

            Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                BarChart(
                    chartParameters = listOf(
                        BarParameters(
                            dataName = Resources.Strings.revenue,
                            data = state.revenueData,
                            barColor = Theme.colors.primary,
                        ),
                        BarParameters(
                            dataName = Resources.Strings.earnings,
                            data = state.earningData,
                            barColor = Theme.colors.secondary,
                        ),
                    ),
                    gridColor = Theme.colors.divider,
                    xAxisData = state.revenueShare,
                    yAxisStyle = Theme.typography.caption.copy(color = Theme.colors.contentPrimary),
                    xAxisStyle = Theme.typography.body.copy(color = Theme.colors.contentPrimary),
                    yAxisRange = 4,
                    animateChart = true,
                    barWidth = 14.kms,
                    barCornerRadius = 4.kms,
                    backgroundLineWidth = 0.5f,
                    spaceBetweenBars = 8.kms,
                    spaceBetweenGroups = 100.kms,
                    descriptionStyle = Theme.typography.caption.copy(color = Theme.colors.contentPrimary),
                )
            }
        }
    }

    //endregion
    @Composable
    fun ColumnScope.TaxiChart(
        tripsRevenueShare: List<PieChartData>
    ) {
        DonutChart(
            pieChartData = tripsRevenueShare,
            centerTitle = Resources.Strings.trip,
            centerTitleStyle = Theme.typography.body.copy(color = Theme.colors.contentSecondary),
            outerCircularColor = Theme.colors.contentBorder,
            innerCircularColor = Theme.colors.contentBorder,
            ratioLineColor = Theme.colors.contentBorder,
            legendPosition = LegendPosition.BOTTOM,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textRatioStyle = Theme.typography.caption.copy(color = Theme.colors.contentPrimary),
            descriptionStyle = Theme.typography.caption.copy(color = Theme.colors.contentPrimary),
        )
    }

    @Composable
    fun ColumnScope.RestaurantsChart(
        ordersRevenueShare: List<PieChartData>
    ) {
        DonutChart(
            pieChartData = ordersRevenueShare,
            centerTitle = Resources.Strings.orders,
            centerTitleStyle = Theme.typography.body.copy(color = Theme.colors.contentSecondary),
            outerCircularColor = Theme.colors.contentBorder,
            innerCircularColor = Theme.colors.contentBorder,
            ratioLineColor = Theme.colors.contentBorder,
            legendPosition = LegendPosition.BOTTOM,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textRatioStyle = Theme.typography.caption.copy(color = Theme.colors.contentPrimary),
            descriptionStyle = Theme.typography.caption.copy(color = Theme.colors.contentPrimary),
        )
    }

    @Composable
    fun getPermissionIcon(permission: PermissionUiState): String {
        return when (permission) {
            PermissionUiState.RESTAURANT_OWNER -> Resources.Drawable.restaurantOutlined
            PermissionUiState.DRIVER -> Resources.Drawable.taxiOutlined
            PermissionUiState.END_USER -> Resources.Drawable.endUser
            PermissionUiState.SUPPORT -> Resources.Drawable.support
            PermissionUiState.DELIVERY -> Resources.Drawable.delivery
            PermissionUiState.ADMIN -> Resources.Drawable.dashboardAdmin
        }
    }

    // region loading
    @Composable
    private fun OverviewScreenLoading() {
        Column(
            modifier = Modifier
                .padding(horizontal = 40.kms)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.kms),
        ) {
            RevenueCardLoading(modifier = Modifier.padding(top = 40.kms))
            Row(
                modifier = Modifier
                    .padding(bottom = 40.kms)
                    .height(380.dp),
                horizontalArrangement = Arrangement.spacedBy(24.kms)
            ) {
                OverviewCardLoading(modifier = Modifier.weight(1f)) { PieChartLoading() }
                OverviewCardLoading(modifier = Modifier.weight(1f)) { PieChartLoading() }
                OverviewCardLoading(modifier = Modifier.weight(1f)) { UsersListLoading() }
            }
        }
    }

    @Composable
    private fun RevenueCardLoading(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .border(1.kms, Theme.colors.divider, RoundedCornerShape(Theme.radius.medium))
                .padding(24.kms),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.kms)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(140.kms, 24.kms)
                        .clip(RoundedCornerShape(Theme.radius.medium))
                        .shimmerEffect()
                )
                Row {
                    Box(
                        modifier = Modifier
                            .size(70.kms, 24.kms)
                            .clip(RoundedCornerShape(Theme.radius.medium))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 8.kms)
                            .size(24.kms)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(Theme.radius.medium))
                    .shimmerEffect()
            )
        }
    }

    @Composable
    private fun OverviewCardLoading(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    ) {
        Column(
            modifier = modifier
                .border(1.kms, Theme.colors.divider, RoundedCornerShape(Theme.radius.medium))
                .padding(start = 24.kms, top = 24.kms, end = 24.kms),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.kms)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(140.kms, 24.kms)
                        .clip(RoundedCornerShape(Theme.radius.medium))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .size(100.kms, 32.kms)
                        .clip(RoundedCornerShape(Theme.radius.medium))
                        .shimmerEffect()
                )
            }
            content()
        }
    }

    @Composable
    private fun PieChartLoading(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 40.kms)
                    .size(200.kms)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .padding(bottom = 24.kms, start = 80.kms, end = 80.kms)
                    .fillMaxWidth()
                    .height(16.kms)
                    .clip(RoundedCornerShape(Theme.radius.medium))
                    .shimmerEffect()
            )
        }
    }

    @Composable
    private fun UsersListLoading(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            repeat(10) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.kms),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.kms)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.kms)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )
                        Box(
                            modifier = Modifier
                                .size(140.kms, 24.kms)
                                .clip(RoundedCornerShape(Theme.radius.medium))
                                .shimmerEffect()
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.kms)
                    ) {
                        repeat(3) {
                            Box(
                                modifier = Modifier
                                    .size(24.kms)
                                    .clip(CircleShape)
                                    .shimmerEffect()
                            )
                        }
                    }
                }
            }
        }
    }

    // endregion
}