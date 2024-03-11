package com.angus.common.presentation.taxi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.Navigator
import com.angus.designSystem.ui.composable.*
import com.angus.designSystem.ui.theme.Theme
import kotlinx.coroutines.delay
import com.angus.common.domain.entity.CarColor
import com.angus.common.domain.util.TaxiStatus
import com.angus.common.presentation.base.BaseScreen
import com.angus.common.presentation.composables.*
import com.angus.common.presentation.composables.modifier.cursorHoverIconHand
import com.angus.common.presentation.composables.modifier.noRipple
import com.angus.common.presentation.composables.table.BpPager
import com.angus.common.presentation.composables.table.BpTable
import com.angus.common.presentation.composables.table.TotalItemsIndicator
import com.angus.common.presentation.resources.Resources
import com.angus.common.presentation.util.kms

class TaxiScreen :
    BaseScreen<TaxiScreenModel, TaxiUiEffect, TaxiUiState, TaxiInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getScreenModel())
    }

    override fun onEffect(effect: TaxiUiEffect, navigator: Navigator) {
        when (effect) {
            //TODO: effects
            else -> {}
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun OnRender(
        state: TaxiUiState,
        listener: TaxiInteractionListener
    ) {
        TaxiDialog(
            isVisible = state.isAddNewTaxiDialogVisible,
            listener = listener,
            state = state.newTaxiInfo,
            isEditMode = state.isEditMode
        )
        Box(
            modifier = Modifier.background(Theme.colors.surface).padding(40.kms).fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            Column(modifier = Modifier.fillMaxSize().align(Alignment.TopCenter)) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 16.kms),
                    horizontalArrangement = Arrangement.spacedBy(16.kms),
                    verticalAlignment = Alignment.Top
                ) {
                    BpSimpleTextField(
                        modifier = Modifier.widthIn(max = 440.kms),
                        hint = Resources.Strings.searchForTaxis,
                        onValueChange = listener::onSearchInputChange,
                        text = state.searchQuery,
                        keyboardType = KeyboardType.Text,
                        trailingPainter = painterResource(Resources.Drawable.search),
                        outlinedTextFieldDefaults =  OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Theme.colors.surface,
                            cursorColor = Theme.colors.contentTertiary,
                            errorCursorColor = Theme.colors.primary,
                            focusedBorderColor = Theme.colors.contentTertiary.copy(alpha = 0.2f),
                            unfocusedBorderColor = Theme.colors.contentBorder.copy(alpha = 0.1f),
                            errorBorderColor = Theme.colors.primary.copy(alpha = 0.5f),
                        )
                    )

                    Column {
                        BpIconButton(
                            onClick = listener::onFilterMenuClicked,
                            painter = painterResource(Resources.Drawable.filter),
                            modifier = Modifier.cursorHoverIconHand()
                        ) {
                            Text(
                                text = Resources.Strings.filter,
                                style = Theme.typography.titleMedium.copy(color = Theme.colors.contentTertiary),
                            )
                        }
                        TaxiFilterDropdownMenu(
                            onFilterMenuDismiss = listener::onFilterMenuDismiss,
                            isFilterDropdownMenuExpanded = state.isFilterDropdownMenuExpanded,
                            taxi = state.taxiFilterUiState,
                            onCarColorSelected = listener::onSelectedCarColor,
                            onSeatsSelected = listener::onSelectedSeat,
                            onStatusSelected = listener::onSelectedStatus,
                            onSaveFilterClicked = listener::onSaveFilterClicked,
                            onCancelFilterClicked = listener::onCancelFilterClicked,
                            onClearAllClicked = listener::onClearAllClicked,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    BpOutlinedButton(
                        title = Resources.Strings.export,
                        onClick = listener::onExportReportClicked,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand()
                    )
                    BpButton(
                        title = Resources.Strings.newTaxi,
                        onClick = listener::onAddNewTaxiClicked,
                        textPadding = PaddingValues(horizontal = 24.kms),
                        modifier = Modifier.cursorHoverIconHand()
                    )
                }
                BpNoInternetConnection(!state.hasConnection){
                    listener.onRetry()
                }
                BpTable(
                    hasConnection = state.hasConnection,
                    data = state.pageInfo.data,
                    key = { it.id },
                    isVisible = state.hasConnection,
                    headers = state.tabHeader,
                    limitItem=state.specifiedTaxis,
                    isLoading = state. isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    rowContent = { taxi ->
                        TaxiRow(
                            taxi = taxi,
                            position = state.pageInfo.data.indexOf(taxi) + 1,
                            onDropdownMenuClicked = listener::onShowTaxiMenu,
                            onDropdownMenuDismiss = listener::onHideTaxiMenu,
                            onEditTaxiClicked = listener::onEditTaxiClicked,
                            onDeleteTaxiClicked = listener::onDeleteTaxiClicked,
                        )
                    },
                )
                TaxisTableFooter(
                    selectedPage = state.currentPage,
                    numberItemInPage = state.specifiedTaxis,
                    numberOfTaxis = state.pageInfo.numberOfTaxis,
                    pageCount = state.pageInfo.totalPages,
                    onPageClicked = listener::onPageClick,
                    onItemPerPageChanged = listener::onItemsIndicatorChange,
                )
            }

            AnimatedVisibility(
                visible = state.isReportExportedSuccessfully,
                enter = fadeIn(initialAlpha = 0.3f),
                exit = fadeOut(targetAlpha = 0.3f)
            ) {
                SnackBar(
                    modifier = Modifier.zIndex(3f).align(Alignment.BottomCenter),
                    onDismiss = listener::onDismissExportReportSnackBar
                ) {
                    Image(
                        painter = painterResource(Resources.Drawable.downloadMark),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = Theme.colors.success),
                        modifier = Modifier.padding(16.kms)
                    )
                    Text(
                        text = Resources.Strings.downloadSuccessMessage,
                        style = Theme.typography.titleMedium,
                        color = Theme.colors.success,
                    )
                }
            }

            LaunchedEffect(state.isReportExportedSuccessfully) {
                if (state.isReportExportedSuccessfully) {
                    delay(1500)
                    listener.onDismissExportReportSnackBar()
                }
            }
        }
    }

    //region components

    @Composable
    private fun TaxiFilterDropdownMenu(
        isFilterDropdownMenuExpanded: Boolean = false,
        onFilterMenuDismiss: () -> Unit = {},
        taxi: TaxiFilterUiState,
        onSeatsSelected: (Int) -> Unit,
        onCarColorSelected: (CarColor) -> Unit,
        onStatusSelected: (TaxiStatus) -> Unit,
        onSaveFilterClicked: () -> Unit,
        onCancelFilterClicked: () -> Unit,
        onClearAllClicked: () -> Unit,
    ) {
        BpDropdownMenu(
            expanded = isFilterDropdownMenuExpanded,
            onDismissRequest = onFilterMenuDismiss,
            offset = DpOffset.Zero.copy(y = 16.kms),
            shape = RoundedCornerShape(Theme.radius.medium),
        ) {
            FilterBox(
                title = Resources.Strings.filter,
                onSaveClicked = onSaveFilterClicked,
                onCancelClicked = onCancelFilterClicked,
                onClearAllClicked = onClearAllClicked,
            ) {
                Column(Modifier.fillMaxSize()) {
                    Text(
                        Resources.Strings.status,
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.contentPrimary,
                        modifier = Modifier.padding(horizontal = 24.kms, vertical = 16.kms),
                    )
                    CarStatus(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Theme.colors.background)
                            .padding(24.kms),
                        status = TaxiStatus.values().toList(),
                        onSelectState = onStatusSelected,
                        selectedStatus = taxi.status,
                    )
                    Text(
                        Resources.Strings.carColor,
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.contentPrimary,
                        modifier = Modifier.padding(horizontal = 24.kms, vertical = 16.kms),
                    )
                    CarColors(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Theme.colors.background)
                            .padding(horizontal = 24.kms, vertical = 16.kms),
                        colors = CarColor.values().toList(),
                        onSelectColor = onCarColorSelected,
                        selectedCarColor = taxi.carColor
                    )
                    Text(
                        Resources.Strings.seats,
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.contentPrimary,
                        modifier = Modifier.padding(horizontal = 24.kms, vertical = 16.kms)
                    )
                    SeatsBar(
                        selectedSeatsCount = taxi.seats?: 0,
                        count = 6,
                        selectedIcon = painterResource(Resources.Drawable.seatFilled),
                        notSelectedIcon = painterResource(Resources.Drawable.seatOutlined),
                        iconsSize = 24.kms,
                        iconsPadding = PaddingValues(horizontal = 8.kms),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Theme.colors.background)
                            .padding(horizontal = 24.kms, vertical = 16.kms),
                        onClick = onSeatsSelected
                    )
                }
            }
        }
    }

    @Composable
    private fun TaxisTableFooter(
        numberItemInPage: Int,
        numberOfTaxis: Int,
        pageCount: Int,
        selectedPage: Int,
        onPageClicked: (Int) -> Unit,
        onItemPerPageChanged: (Int) -> Unit,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(color = Theme.colors.surface),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TotalItemsIndicator(
                numberItemInPage = numberItemInPage,
                totalItems = numberOfTaxis,
                itemType = Resources.Strings.taxi,
                onItemPerPageChange = onItemPerPageChanged
            )
            BpPager(
                maxPages = pageCount,
                currentPage = selectedPage,
                onPageClicked = onPageClicked,
            )
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun RowScope.TaxiRow(
        position: Int,
        taxi: TaxiDetailsUiState,
        firstColumnWeight: Float = 1f,
        otherColumnsWeight: Float = 3f,
        onDropdownMenuClicked: (String) -> Unit,
        onDropdownMenuDismiss: (String) -> Unit,
        onEditTaxiClicked: (String) -> Unit,
        onDeleteTaxiClicked: (String) -> Unit,
    ) {
        TitleField(
            text = position.toString(),
            color = Theme.colors.contentTertiary,
            weight = firstColumnWeight
        )
        TitleField(text = taxi.plateNumber)
        TitleField(text = taxi.username)
        TitleField(text = taxi.status.getStatusName(), color = taxi.statusColor)
        TitleField(text = taxi.type)
        SquareColorField(
            modifier = Modifier.weight(otherColumnsWeight),
            color = Color(taxi.color.hexadecimal)
        )
        FlowRow(
            modifier = Modifier.weight(otherColumnsWeight),
            horizontalArrangement = Arrangement.spacedBy(8.kms),
            maxItemsInEachRow = 3,
        ) {
            repeat(taxi.seats) {
                Box(modifier = Modifier.padding(top = 8.kms)) {
                    Icon(
                        painter = painterResource(Resources.Drawable.seatOutlined),
                        contentDescription = null,
                        tint = Theme.colors.contentPrimary.copy(alpha = 0.87f),
                        modifier = Modifier.size(24.kms)
                    )
                }
            }
        }
        TitleField(text = taxi.trips)
        Box(modifier = Modifier.weight(firstColumnWeight)) {
            Image(
                painter = painterResource(Resources.Drawable.dots),
                contentDescription = null,
                modifier = Modifier.noRipple { onDropdownMenuClicked(taxi.id) },
                colorFilter = ColorFilter.tint(color = Theme.colors.contentPrimary)
            )
            EditTaxiDropdownMenu(
                taxi = taxi,
                onDropdownMenuDismiss = { onDropdownMenuDismiss(taxi.id) },
                onEditTaxiClicked = onEditTaxiClicked,
                onDeleteTaxiClicked = onDeleteTaxiClicked,
            )
        }
    }

    @Composable
    private fun EditTaxiDropdownMenu(
        taxi: TaxiDetailsUiState,
        onDropdownMenuDismiss: () -> Unit,
        onEditTaxiClicked: (String) -> Unit,
        onDeleteTaxiClicked: (String) -> Unit,
    ) {
        BpDropdownMenu(
            expanded = taxi.isTaxiMenuExpanded,
            onDismissRequest = onDropdownMenuDismiss,
            shape = RoundedCornerShape(Theme.radius.medium),
            offset = DpOffset.Zero.copy(x = (-100).kms)
        ) {
            Column {
                BpDropdownMenuItem(
                    onClick = {
                        onEditTaxiClicked(taxi.id)
                    },
                    text = Resources.Strings.edit,
                    leadingIconPath = Resources.Drawable.edit,
                    isSecondary = false,
                    showBottomDivider = true
                )
                BpDropdownMenuItem(
                    onClick = {
                        onDeleteTaxiClicked(taxi.id)
                    },
                    text = Resources.Strings.delete,
                    leadingIconPath = Resources.Drawable.delete,
                    isSecondary = true,
                    showBottomDivider = false
                )
            }
        }
    }
}

@Composable
private fun RowScope.TitleField(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Theme.colors.contentPrimary,
    weight: Float = 3f
) {
    Text(
        text = text,
        style = Theme.typography.titleMedium,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.weight(weight),
        maxLines = 1,
        color = color
    )
}

@Composable
private fun SquareColorField(modifier: Modifier = Modifier, color: Color) {
    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier.size(24.kms)
                .background(
                    color = color,
                    shape = RoundedCornerShape(Theme.radius.small),
                )
                .border(
                    width = 1.kms,
                    color = Theme.colors.contentBorder,
                    shape = RoundedCornerShape(Theme.radius.small),
                )
        )
    }
}

@Composable
fun CarStatus(
    modifier: Modifier = Modifier,
    status: List<TaxiStatus>,
    onSelectState: (TaxiStatus) -> Unit,
    selectedStatus: TaxiStatus?,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.kms)
    ) {
        status.forEach { status ->
            BpChip(
                modifier = Modifier.height(32.kms),
                label = status.getStatusName(),
                onClick = { onSelectState(status) },
                isSelected = selectedStatus == status,
            )
        }
    }
}
//endregion

