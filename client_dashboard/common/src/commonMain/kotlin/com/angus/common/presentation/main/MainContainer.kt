package com.angus.common.presentation.main

import LoginScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.angus.designSystem.ui.theme.Theme
import com.angus.common.presentation.base.BaseScreen
import com.angus.common.presentation.composables.scaffold.BpSideBarItem
import com.angus.common.presentation.composables.scaffold.DashBoardScaffold
import com.angus.common.presentation.composables.scaffold.DashboardAppbar
import com.angus.common.presentation.composables.scaffold.DashboardSideBar
import com.angus.common.presentation.resources.Resources

object MainContainer :
    BaseScreen<MainScreenModel, MainUiEffect, MainUiState, MainInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getScreenModel())
    }

    override fun onEffect(effect: MainUiEffect, navigator: Navigator) {
        when (effect) {
            MainUiEffect.Logout -> {
                navigator.replaceAll(LoginScreen())
            }
        }
    }

    @Composable
    override fun OnRender(state: MainUiState, listener: MainInteractionListener) {

        TabNavigator(OverviewTab) {
            val tabNavigator = LocalTabNavigator.current
            DashBoardScaffold(
                    appbar = {
                        DashboardAppbar(
                                title = tabNavigator.current.options.title,
                                username = state.username,
                                firstUsernameLetter = state.firstUsernameLetter,
                                onLogOut = listener::onClickLogout,
                                isDropMenuExpanded = state.isDropMenuExpanded,
                                onClickDropDownMenu = listener::onClickDropDownMenu,
                                onDismissDropDownMenu = listener::onDismissDropDownMenu
                        )
                    },
                    sideBar = {
                        DashboardSideBar(
                                currentItem = tabNavigator.current.options.index.toInt(),
                                onSwitchTheme = listener::onSwitchTheme,
                                darkTheme = state.isDarkMode
                        ) { sideBarUnexpandedWidthInKms, mainMenuIsExpanded, itemHeight ->
                            SideBarItems(
                                    mainMenuIsExpanded = mainMenuIsExpanded,
                                    sideBarUnexpandedWidthInKms = sideBarUnexpandedWidthInKms,
                                    itemHeight = itemHeight
                            )
                        }
                    },
                    content = {
                        Box(Modifier.background(Theme.colors.surface)) {
                            CurrentTab()
                        }
                    },
            )
        }
    }

    @Composable
    private fun ColumnScope.SideBarItems(
        mainMenuIsExpanded: Boolean,
        sideBarUnexpandedWidthInKms: Dp,
        itemHeight: (itemHeight: Float) -> Unit
    ) {
        TabNavigationItem(
                tab = OverviewTab,
                selectedIconResource = Resources.Drawable.overviewFilled,
                unSelectedIconResource = Resources.Drawable.overviewOutlined,
                mainMenuIsExpanded = mainMenuIsExpanded,
                sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
                modifier = Modifier.onGloballyPositioned {
                    itemHeight(it.boundsInParent().height)
                }
        )
        /*TabNavigationItem(
                tab = TaxisTab,
                selectedIconResource = Resources.Drawable.taxiFilled,
                unSelectedIconResource = Resources.Drawable.taxiOutlined,
                mainMenuIsExpanded = mainMenuIsExpanded,
                sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
                modifier = Modifier.onGloballyPositioned {
                    itemHeight(it.boundsInParent().height)
                }
        )*/
        TabNavigationItem(
                tab = RestaurantsTab,
                selectedIconResource = Resources.Drawable.restaurantFilled,
                unSelectedIconResource = Resources.Drawable.restaurantOutlined,
                mainMenuIsExpanded = mainMenuIsExpanded,
                sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
                modifier = Modifier.onGloballyPositioned {
                    itemHeight(it.boundsInParent().height)
                }
        )
        TabNavigationItem(
                tab = UsersTab,
                selectedIconResource = Resources.Drawable.usersFilled,
                unSelectedIconResource = Resources.Drawable.usersOutlined,
                mainMenuIsExpanded = mainMenuIsExpanded,
                sideBarUnexpandedWidth = sideBarUnexpandedWidthInKms,
                modifier = Modifier.onGloballyPositioned {
                    itemHeight(it.boundsInParent().height)
                }
        )

    }

    @Composable
    fun ColumnScope.TabNavigationItem(
        tab: Tab,
        selectedIconResource: String,
        unSelectedIconResource: String,
        mainMenuIsExpanded: Boolean,
        sideBarUnexpandedWidth: Dp,
        modifier: Modifier = Modifier,
    ) {
        val tabNavigator = LocalTabNavigator.current
        BpSideBarItem(
                onClick = { tabNavigator.current = tab },
                isSelected = tabNavigator.current == tab,
                label = tab.options.title,
                selectedIconResource = selectedIconResource,
                unSelectedIconResource = unSelectedIconResource,
                mainMenuIsExpanded = mainMenuIsExpanded,
                sideBarUnexpandedWidthInKms = sideBarUnexpandedWidth,
                modifier = modifier
        )
    }

}
