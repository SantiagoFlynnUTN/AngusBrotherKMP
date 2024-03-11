package com.angus.common.presentation.taxi


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.angus.designSystem.ui.theme.Theme
import com.angus.common.domain.entity.*
import com.angus.common.domain.util.TaxiStatus
import com.angus.common.domain.util.TaxiStatus.ONLINE
import com.angus.common.presentation.composables.table.Header
import com.angus.common.presentation.resources.Resources
import com.angus.common.presentation.restaurant.ErrorWrapper

data class TaxiUiState(
    val isLoading: Boolean = true,
    val hasConnection: Boolean = true,
    val isAddNewTaxiDialogVisible: Boolean = false,
    val newTaxiInfo: TaxiInfoUiState = TaxiInfoUiState(),
    val taxiFilterUiState: TaxiFilterUiState = TaxiFilterUiState(),
    val taxis: List<TaxiDetailsUiState> = emptyList(),
    val searchQuery: String = "",
    val isReportExportedSuccessfully: Boolean = false,
    val pageInfo: TaxiPageInfoUiState = TaxiPageInfoUiState(),
    val specifiedTaxis: Int = 10,
    val currentPage: Int = 1,
    val isFilterDropdownMenuExpanded: Boolean = false,
    val isEditMode: Boolean = false,

    ) {
    val tabHeader
       @Composable get() = listOf(
        Header(Resources.Strings.number, 1f),
        Header(Resources.Strings.plateNumber, 3f),
        Header(Resources.Strings.driverUsername, 3f),
        Header(Resources.Strings.status, 3f),
        Header(Resources.Strings.carModel, 3f),
        Header(Resources.Strings.carColor, 3f),
        Header(Resources.Strings.seats, 3f),
        Header(Resources.Strings.trips, 3f),
        Header("", 1f),
    )
}

data class TaxiDetailsUiState(
    val id: String = "",
    val plateNumber: String = "",
    val color: CarColor = CarColor.WHITE,
    val type: String = "",
    val seats: Int = 4,
    val username: String = "",
    val driverId: String? = null,
    val status: TaxiStatus = ONLINE,
    val trips: String = "1",
    val isTaxiMenuExpanded: Boolean = false,
) {
    val statusColor: Color
        @Composable get() = when (status) {
            TaxiStatus.OFFLINE -> Theme.colors.primary
            TaxiStatus.ONLINE -> Theme.colors.success
        }
}

data class TaxiFilterUiState(
    val carColor: CarColor? = null,
    val seats: Int? = null,
    val status: TaxiStatus? = null
) {
    fun toEntity(): TaxiFiltration {
        return TaxiFiltration(
            carColor = carColor,
            seats = seats,
            status = status
        )
    }
}

@Composable
fun TaxiStatus.getStatusName(): String {
    return when (this) {
        TaxiStatus.OFFLINE -> Resources.Strings.offline
        TaxiStatus.ONLINE -> Resources.Strings.online
    }
}

data class TaxiPageInfoUiState(
    val data: List<TaxiDetailsUiState> = emptyList(),
    val numberOfTaxis: Int = 0,
    val totalPages: Int = 0,
)

data class TaxiInfoUiState(
    val id: String = "",
    val plateNumber: String = "",
    val driverUserName: String = "",
    val driverId: String = "",
    val carModel: String = "",
    val selectedCarColor: CarColor = CarColor.WHITE,
    val seats: Int = 0,
    val plateNumberError: ErrorWrapper? = null,
    val carModelError: ErrorWrapper? = null,
    val driverUserNameError: ErrorWrapper? = null,
    val isFormValid: Boolean = false,
)

fun DataWrapper<Taxi>.toDetailsUiState(): TaxiPageInfoUiState {
    return TaxiPageInfoUiState(
        data = result.toDetailsUiState(),
        totalPages = totalPages,
        numberOfTaxis = numberOfResult
    )
}

fun Taxi.toDetailsUiState(): TaxiDetailsUiState = TaxiDetailsUiState(
    id = id,
    plateNumber = plateNumber,
    color = color,
    type = type,
    seats = seats,
    username = username,
    status = status,
    trips = trips,
    driverId = driverId
)

fun List<Taxi>.toDetailsUiState() = map { it.toDetailsUiState() }

fun TaxiInfoUiState.toEntity() = NewTaxiInfo(
    plateNumber = plateNumber,
    driverUserName = driverUserName,
    driverId = driverId,
    carModel = carModel,
    selectedCarColor = selectedCarColor,
    seats = seats
)

fun TaxiDetailsUiState.toEntity(): Taxi = Taxi(
    id = id,
    plateNumber = plateNumber,
    color = color,
    type = type,
    seats = seats,
    username = username,
    status = status,
    trips = trips,
    driverId = driverId
)

fun List<TaxiDetailsUiState>.toEntity() = map { it.toEntity() }

fun Taxi.toUiState(): TaxiInfoUiState = TaxiInfoUiState(
    id = id,
    plateNumber = plateNumber,
    driverUserName = username,
    carModel = type,
    selectedCarColor = color,
    seats = seats,
    driverId = driverId ?: ""
)