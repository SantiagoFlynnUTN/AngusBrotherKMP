package com.angus.common.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.angus.common.presentation.overview.OverviewScreen
import com.angus.common.presentation.resources.Resources
import com.angus.common.presentation.restaurant.RestaurantScreen
import com.angus.common.presentation.taxi.TaxiScreen
import com.angus.common.presentation.users.UserScreen

object OverviewTab : Tab {

    override val options: TabOptions
        @Composable get() {
            val title = Resources.Strings.overview
            return remember { TabOptions(index = 0u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = OverviewScreen)
    }

}

object TaxisTab : Tab {

    override val options: TabOptions
        @Composable get() {
            val title = Resources.Strings.taxis
            return remember { TabOptions(index = 1u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = TaxiScreen())
    }
}

object RestaurantsTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Resources.Strings.restaurants
            return remember { TabOptions(index = 2u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = RestaurantScreen())
    }
}

object UsersTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val title = Resources.Strings.users
            return remember { TabOptions(index = 3u, title = title) }
        }

    @Composable
    override fun Content() {
        Navigator(screen = UserScreen())
    }
}
