package com.angus.common.presentation.overview

sealed interface OverviewUiEffect {

    object ViewMoreUsers : OverviewUiEffect

    //object ViewMoreTaxis : OverviewUiEffect

    object ViewMoreRestaurant : OverviewUiEffect

}