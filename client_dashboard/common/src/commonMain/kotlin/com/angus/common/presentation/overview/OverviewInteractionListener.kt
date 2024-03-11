package com.angus.common.presentation.overview

import com.angus.common.presentation.base.BaseInteractionListener

interface OverviewInteractionListener : BaseInteractionListener {

    fun onMenuItemDropDownClicked()

    fun onMenuItemClicked(index: Int)

    fun onDismissDropDownMenu()

    fun onViewMoreUsersClicked()

    fun onViewMoreRestaurantClicked()

    fun onViewMoreTaxiClicked()

    fun onRetry()

}