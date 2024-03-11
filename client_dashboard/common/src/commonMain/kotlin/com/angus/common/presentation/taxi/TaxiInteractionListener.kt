package com.angus.common.presentation.taxi

import com.angus.common.domain.entity.CarColor
import com.angus.common.domain.util.TaxiStatus
import com.angus.common.presentation.base.BaseInteractionListener

interface TaxiInteractionListener : BaseInteractionListener, FilterMenuListener,
    TaxiMenuListener, PageListener, TaxiDialogListener {

    fun onExportReportClicked()

    fun onDismissExportReportSnackBar()

    fun onSearchInputChange(searchQuery: String)

    fun onAddNewTaxiClicked()

    fun onRetry()
}

interface TaxiDialogListener {

    fun onTaxiPlateNumberChange(number: String)

    fun onDriverUserNamChange(name: String)

    fun onCarModelChanged(model: String)

    fun onCarColorSelected(color: CarColor)

    fun onSeatSelected(seats: Int)

    fun onSaveClicked()

    fun onCancelClicked()

    fun onCreateTaxiClicked()
}

interface TaxiMenuListener {

    fun onShowTaxiMenu(id : String)

    fun onHideTaxiMenu(id : String)

    fun onDeleteTaxiClicked(taxi: String)

    fun onEditTaxiClicked(taxiId: String)

}

interface FilterMenuListener {

    fun onCancelFilterClicked()

    fun onSaveFilterClicked()

    fun onFilterMenuDismiss()

    fun onFilterMenuClicked()

    fun onSelectedCarColor(color: CarColor)

    fun onSelectedSeat(seats: Int)

    fun onSelectedStatus(status: TaxiStatus)

    fun onClearAllClicked()

}

interface PageListener {

    fun onItemsIndicatorChange(itemPerPage: Int)

    fun onPageClick(pageNumber: Int)

}