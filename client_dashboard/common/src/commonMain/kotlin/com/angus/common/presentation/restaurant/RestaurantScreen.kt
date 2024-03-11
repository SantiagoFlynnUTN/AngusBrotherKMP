package com.angus.common.presentation.restaurant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import cafe.adriel.voyager.navigator.Navigator
import com.angus.designSystem.ui.composable.*
import com.angus.designSystem.ui.composable.modifier.shimmerEffect
import com.angus.designSystem.ui.theme.Theme
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.seiko.imageloader.rememberImagePainter
import com.angus.common.presentation.base.BaseScreen
import com.angus.common.presentation.composables.*
import com.angus.common.presentation.composables.modifier.cursorHoverIconHand
import com.angus.common.presentation.composables.modifier.noRipple
import com.angus.common.presentation.composables.table.BpPager
import com.angus.common.presentation.composables.table.BpTable
import com.angus.common.presentation.composables.table.TotalItemsIndicator
import com.angus.common.presentation.resources.Resources
import com.angus.common.presentation.util.kms
import com.angus.common.presentation.util.toImageBitmap
import java.awt.Dimension
import kotlin.reflect.KFunction1

class RestaurantScreen :
    BaseScreen<RestaurantScreenModel, RestaurantUIEffect, RestaurantUiState, RestaurantInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getScreenModel())
    }

    override fun onEffect(effect: RestaurantUIEffect, navigator: Navigator) {

    }

    @Composable
    override fun OnRender(state: RestaurantUiState, listener: RestaurantInteractionListener) {
        AnimatedVisibility(visible = state.isNewRestaurantInfoDialogVisible) {
            NewRestaurantInfoDialog(
                state = state,
                listener = listener,
            )
        }

        CuisineDialog(
            listener = listener,
            state = state.newCuisineDialogUiState
        )
        OfferDialog(
            listener = listener,
            state = state.newOfferDialogUiState
        )

        Column(
            Modifier.background(Theme.colors.surface).padding(40.kms).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.kms),
        ) {
            RestaurantScreenTopRow(state = state, listener = listener)
            RestaurantTable(state = state, listener = listener)
            BpNoInternetConnection(!state.hasConnection) {
                listener.onRetry()
            }
            RestaurantPagingRow(state = state, listener = listener)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun RestaurantScreenTopRow(
        state: RestaurantUiState,
        listener: RestaurantInteractionListener,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.kms),
            verticalAlignment = Alignment.Top
        ) {
            BpSimpleTextField(
                modifier = Modifier.widthIn(min = 340.kms, max = 440.kms),
                hint = Resources.Strings.searchForRestaurants,
                onValueChange = listener::onSearchChange,
                text = state.searchQuery,
                keyboardType = KeyboardType.Text,
                trailingPainter = painterResource(Resources.Drawable.search),
                outlinedTextFieldDefaults = OutlinedTextFieldDefaults.colors(
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
                    content = {
                        Text(
                            text = Resources.Strings.filter,
                            style = Theme.typography.titleMedium
                                .copy(color = Theme.colors.contentTertiary),
                        )
                    },
                    onClick = listener::onClickDropDownMenu,
                    painter = painterResource(Resources.Drawable.filter),
                    modifier = Modifier.cursorHoverIconHand()
                )
                FilterRestaurantDropdownMenu(
                    onClickRating = listener::onClickFilterRatingBar,
                    onClickPrice = listener::onClickFilterPriceBar,
                    onDismissRequest = listener::onDismissDropDownMenu,
                    onClickCancel = listener::onCancelFilterRestaurantsClicked,
                    onClickSave = {
                        listener.onSaveFilterRestaurantsClicked(
                            state.restaurantFilterDropdownMenuUiState.filterRating,
                            state.restaurantFilterDropdownMenuUiState.filterPriceLevel.toString()
                        )
                    },
                    expanded = state.restaurantFilterDropdownMenuUiState.isFilterDropdownMenuExpanded,
                    rating = state.restaurantFilterDropdownMenuUiState.filterRating,
                    priceLevel = state.restaurantFilterDropdownMenuUiState.filterPriceLevel,
                    onFilterClearAllClicked = listener::onFilterClearAllClicked,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            BpOutlinedButton(
                title = Resources.Strings.addOffer,
                onClick = listener::onAddOfferClicked,
                textPadding = PaddingValues(horizontal = 24.kms),
                modifier = Modifier.cursorHoverIconHand()
            )
            BpOutlinedButton(
                title = Resources.Strings.addCuisine,
                onClick = listener::onClickAddCuisine,
                textPadding = PaddingValues(horizontal = 24.kms),
                modifier = Modifier.cursorHoverIconHand()
            )
            BpButton(
                title = Resources.Strings.newRestaurant,
                onClick = listener::onAddNewRestaurantClicked,
                textPadding = PaddingValues(horizontal = 24.kms),
                modifier = Modifier.cursorHoverIconHand()
            )
        }
    }

    @Composable
    private fun ColumnScope.RestaurantTable(
        state: RestaurantUiState,
        listener: RestaurantInteractionListener,
    ) {
        BpTable(
            data = state.restaurants,
            key = { it.id },
            isLoading = state.isLoading,
            headers = state.tableHeader,
            modifier = Modifier.fillMaxWidth(),
            isVisible = state.hasConnection,
            rowContent = { restaurant ->
                RestaurantTableRow(
                    onClickEditRestaurant = listener::onShowRestaurantMenu,
                    onEditRestaurantDismiss = listener::onHideRestaurantMenu,
                    onClickDeleteRestaurantMenuItem = listener::onClickDeleteRestaurantMenuItem,
                    onClickEditRestaurantMenuItem = listener::onClickEditRestaurantMenuItem,
                    position = state.restaurants.indexOf(restaurant) + 1,
                    restaurant = restaurant,
                )
            },
        )
    }

    @Composable
    private fun RestaurantPagingRow(
        state: RestaurantUiState,
        listener: RestaurantInteractionListener,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(color = Theme.colors.surface),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TotalItemsIndicator(
                numberItemInPage = state.numberOfRestaurantsInPage,
                totalItems = state.numberOfRestaurants,
                itemType = Resources.Strings.restaurant,
                onItemPerPageChange = listener::onItemPerPageChange
            )
            BpPager(
                maxPages = state.maxPageCount,
                currentPage = state.selectedPageNumber,
                onPageClicked = listener::onPageClicked,
            )
        }
    }

    @Composable
    private fun RowScope.RestaurantTableRow(
        onClickEditRestaurant: (restaurantId: String) -> Unit,
        onEditRestaurantDismiss: (String) -> Unit,
        onClickEditRestaurantMenuItem: (restaurantId: String) -> Unit,
        onClickDeleteRestaurantMenuItem: (id: String) -> Unit,
        position: Int,
        restaurant: RestaurantUiState.RestaurantDetailsUiState,
        firstAndLastColumnWeight: Float = 1f,
        otherColumnsWeight: Float = 3f,
    ) {
        Text(
            position.toString(),
            style = Theme.typography.titleMedium.copy(color = Theme.colors.contentTertiary),
            modifier = Modifier.weight(firstAndLastColumnWeight),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            restaurant.name,
            style = Theme.typography.titleMedium.copy(color = Theme.colors.contentPrimary),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(otherColumnsWeight),
            maxLines = 1,
        )

        Text(
            restaurant.ownerUsername,
            style = Theme.typography.titleMedium.copy(color = Theme.colors.contentPrimary),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(otherColumnsWeight),
            maxLines = 1,
        )
        Text(
            restaurant.phone,
            style = Theme.typography.titleMedium.copy(color = Theme.colors.contentPrimary),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(otherColumnsWeight),
            maxLines = 1,
        )
        RatingBar(
            rating = restaurant.rate,
            selectedIcon = painterResource(Resources.Drawable.starFilled),
            halfSelectedIcon = painterResource(Resources.Drawable.starHalfFilled),
            modifier = Modifier.weight(otherColumnsWeight),
            iconsSize = 16.kms
        )
        PriceBar(
            priceLevel = restaurant.priceLevel,
            icon = painterResource(Resources.Drawable.dollarSign),
            iconColor = Theme.colors.success,
            modifier = Modifier.weight(otherColumnsWeight),
            iconsSize = 16.kms
        )

        Text(
            text = "${restaurant.openingTime} - ${restaurant.closingTime}",
            style = Theme.typography.titleMedium.copy(color = Theme.colors.contentPrimary),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(otherColumnsWeight),
            maxLines = 1,
        )
        Box(modifier = Modifier.weight(firstAndLastColumnWeight)) {
            Image(
                painter = painterResource(Resources.Drawable.dots),
                contentDescription = null,
                modifier = Modifier.noRipple { onClickEditRestaurant(restaurant.id) },
                colorFilter = ColorFilter.tint(color = Theme.colors.contentPrimary)
            )
            EditRestaurantDropdownMenu(
                restaurant = restaurant,
                onClickEdit = onClickEditRestaurantMenuItem,
                onClickDelete = onClickDeleteRestaurantMenuItem,
                onDismissRequest = onEditRestaurantDismiss,
            )
        }
    }

    @Composable
    private fun FilterRestaurantDropdownMenu(
        onClickRating: (Double) -> Unit,
        onClickPrice: KFunction1<Int, Unit>,
        onDismissRequest: () -> Unit,
        onClickCancel: () -> Unit,
        onClickSave: () -> Unit,
        expanded: Boolean,
        rating: Double,
        priceLevel: Int,
        onFilterClearAllClicked: () -> Unit,
    ) {
        BpDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            offset = DpOffset.Zero.copy(y = 16.kms),
            shape = RoundedCornerShape(Theme.radius.medium),
        ) {
            FilterBox(
                title = Resources.Strings.filter,
                onSaveClicked = onClickSave,
                onCancelClicked = onClickCancel,
                onClearAllClicked = onFilterClearAllClicked,
            ) {
                Column {
                    Text(
                        text = Resources.Strings.rating,
                        style = Theme.typography.title,
                        color = Theme.colors.contentPrimary,
                        modifier = Modifier.padding(start = 24.kms, top = 16.kms)
                    )
                    EditableRatingBar(
                        rating = rating,
                        count = 5,
                        selectedIcon = painterResource(Resources.Drawable.starFilled),
                        halfSelectedIcon = painterResource(Resources.Drawable.starHalfFilled),
                        notSelectedIcon = painterResource(Resources.Drawable.starOutlined),
                        iconsSize = 24.kms,
                        iconsPadding = PaddingValues(horizontal = 8.kms),
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 16.kms)
                            .background(color = Theme.colors.background)
                            .padding(horizontal = 24.kms, vertical = 16.kms),
                        onClick = onClickRating
                    )
                    Text(
                        text = Resources.Strings.priceLevel,
                        style = Theme.typography.title,
                        color = Theme.colors.contentPrimary,
                        modifier = Modifier.padding(start = 24.kms, top = 32.kms)
                    )
                    EditablePriceBar(
                        priceLevel = priceLevel,
                        count = 3,
                        icon = painterResource(Resources.Drawable.dollarSign),
                        enabledIconsColor = Theme.colors.success,
                        disabledIconsColor = Theme.colors.disable,
                        iconsPadding = PaddingValues(horizontal = 8.kms),
                        iconsSize = 16.kms,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 16.kms)
                            .background(color = Theme.colors.background)
                            .padding(horizontal = 24.kms, vertical = 16.kms),
                        onClick = onClickPrice
                    )
                }
            }
        }
    }

    @Composable
    private fun EditRestaurantDropdownMenu(
        restaurant: RestaurantUiState.RestaurantDetailsUiState,
        onClickEdit: (restaurantId: String) -> Unit,
        onClickDelete: (id: String) -> Unit,
        onDismissRequest: (String) -> Unit,
    ) {
        BpDropdownMenu(
            expanded = restaurant.isExpanded,
            onDismissRequest = { onDismissRequest(restaurant.id) },
            shape = RoundedCornerShape(Theme.radius.medium).copy(topEnd = CornerSize(0.dp)),
            offset = DpOffset.Zero.copy(x = (-178).kms)
        ) {
            Column {
                BpDropdownMenuItem(
                    onClick = {
                        onClickEdit(restaurant.id)
                    },
                    text = Resources.Strings.edit,
                    leadingIconPath = Resources.Drawable.permission,
                    isSecondary = false,
                    showBottomDivider = true
                )
                BpDropdownMenuItem(
                    onClick = {
                        onClickDelete(restaurant.id)
                    },
                    text = Resources.Strings.delete,
                    leadingIconPath = Resources.Drawable.delete,
                    isSecondary = true,
                    showBottomDivider = false
                )
            }
        }
    }


    @Composable
    private fun CuisineDialog(
        listener: AddCuisineInteractionListener,
        state: RestaurantAddCuisineDialogUiState
    ) {
        DialogWindow(
            onCloseRequest = listener::onCloseAddCuisineDialog,
            visible = state.isVisible,
            undecorated = true,
            transparent = true,
            resizable = false
        ) {
            window.minimumSize = Dimension(400, 420)
            Column(
                modifier = Modifier
                    .background(Theme.colors.surface, RoundedCornerShape(8.kms))
                    .border(
                        1.kms,
                        Theme.colors.divider,
                        RoundedCornerShape(Theme.radius.medium)
                    )
            ) {
                Text(
                    text = Resources.Strings.cuisines,
                    style = Theme.typography.headline,
                    color = Theme.colors.contentPrimary,
                    modifier = Modifier.padding(top = 24.kms, start = 24.kms)
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    BpSimpleTextField(
                        text = state.cuisineName,
                        hint = Resources.Strings.enterCuisineName,
                        onValueChange = listener::onChangeCuisineName,
                        modifier = Modifier.padding(top = 24.kms, start = 24.kms, end = 16.kms)
                            .weight(2f),
                        isError = state.cuisineNameError.isError,
                        errorMessage = state.cuisineNameError.errorMessage,
                    )
                    Box(
                        modifier = Modifier.padding(top = 24.kms, end = 24.kms)
                            .heightIn(min = 56.dp, max = 160.dp)
                            .wrapContentWidth()
                            .border(
                                1.dp,
                                Theme.colors.divider,
                                RoundedCornerShape(Theme.radius.medium)
                            ).padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Resources.Drawable.addImage),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).align(Alignment.Center)
                                .noRipple(listener::onClickCuisineImage),
                            colorFilter = ColorFilter.tint(
                                color =
                                if (state.selectedCuisineImage.isNotEmpty()) Theme.colors.primary else Theme.colors.divider
                            )
                        )
                        FilePicker(
                            state.isImagePickerVisible,
                            fileExtensions = listOf("jpg", "png", "jpeg"),
                            onFileSelected = { type -> listener.onSelectedCuisineImage(type?.platformFile) }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.padding(top = 16.kms)
                        .background(Theme.colors.background)
                        .fillMaxWidth().heightIn(min = 64.kms, max = 256.kms)
                ) {
                    items(state.cuisines) { cuisine ->
                        Row(
                            modifier = Modifier.padding(horizontal = 24.kms, vertical = 16.kms),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = cuisine.name,
                                style = Theme.typography.caption,
                                color = Theme.colors.contentPrimary,
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                painter = painterResource(Resources.Drawable.trashBin),
                                contentDescription = null,
                                tint = Theme.colors.primary,
                                modifier = Modifier
                                    .noRipple { listener.onClickDeleteCuisine(cuisine.id) }
                            )
                        }
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(24.kms),
                    horizontalArrangement = Arrangement.Center
                ) {

                    BpTransparentButton(
                        title = Resources.Strings.cancel,
                        onClick = listener::onCloseAddCuisineDialog,
                        modifier = Modifier.padding(end = 16.kms)
                            .height(32.dp)
                            .weight(1f)
                    )
                    BpOutlinedButton(
                        title = Resources.Strings.add,
                        onClick = listener::onClickCreateCuisine,
                        modifier = Modifier.height(32.dp).weight(3f),
                        textPadding = PaddingValues(0.dp),
                        enabled = state.isAddCuisineEnabled
                    )
                }
            }
        }
    }

    @Composable
    private fun OfferDialog(
        listener: AddOfferInteractionListener,
        state: NewOfferDialogUiState
    ) {

        DialogWindow(
            onCloseRequest = listener::onCloseAddOfferDialog,
            visible = state.isVisible,
            undecorated = true,
            transparent = true,
            resizable = false
        ) {
            window.minimumSize = Dimension(1000, 500)
            Row(
                modifier = Modifier
                    .background(Theme.colors.surface, RoundedCornerShape(8.kms))
                    .border(
                        1.kms,
                        Theme.colors.divider,
                        RoundedCornerShape(Theme.radius.medium)
                    )
            ) {
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Text(
                        text = Resources.Strings.offers,
                        style = Theme.typography.headline,
                        color = Theme.colors.contentPrimary,
                        modifier = Modifier.padding(top = 24.kms, start = 24.kms)
                    )
                    BpSimpleTextField(
                        text = state.offerName,
                        hint = Resources.Strings.enterOfferName,
                        onValueChange = listener::onChangeOfferName,
                        modifier = Modifier.padding(top = 24.kms, start = 24.kms, end = 16.kms),
                        isError = state.offerNameError.isError,
                        errorMessage = state.offerNameError.errorMessage,
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .cursorHoverIconHand()
                            .padding(top = 24.kms, start = 24.kms, end = 16.kms).weight(1f)
                            .border(
                                1.dp,
                                Theme.colors.divider,
                                RoundedCornerShape(Theme.radius.medium)
                            )
                            .clickable(
                                onClick = listener::onClickOfferImagePicker,
                                role = Role.Image
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        FilePicker(
                            state.isImagePickerVisible,
                            fileExtensions = listOf("jpg", "png", "jpeg"),
                            onFileSelected = { type -> listener.onSelectedOfferImage(type?.platformFile) }
                        )
                        state.selectedOfferImage.toImageBitmap()?.let {
                            Image(
                                contentDescription = null,
                                bitmap = it,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                                    .clip(RoundedCornerShape(Theme.radius.medium)),

                                )
                            Box(
                                modifier = Modifier.matchParentSize()
                                    .background(Theme.colors.surface.copy(alpha = 0.5f))
                            ) {
                                Image(
                                    contentDescription = null,
                                    painter = painterResource(Resources.Drawable.editImage),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(56.dp).align(Alignment.Center),
                                    colorFilter = ColorFilter.tint(color = Theme.colors.disable)
                                )
                            }

                        } ?: Image(
                            contentDescription = null,
                            painter = painterResource(Resources.Drawable.addImage),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(56.dp),
                            colorFilter = ColorFilter.tint(color = Theme.colors.disable)
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth().padding(24.kms),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        BpTransparentButton(
                            title = Resources.Strings.cancel,
                            onClick = listener::onCloseAddOfferDialog,
                            modifier = Modifier.padding(end = 16.kms)
                                .height(32.dp)
                                .weight(1f)
                        )
                        BpOutlinedButton(
                            title = Resources.Strings.add,
                            onClick = listener::onClickCreateOffer,
                            modifier = Modifier.height(32.dp).weight(3f),
                            textPadding = PaddingValues(0.dp),
                            enabled = state.isAddOfferEnabled
                        )
                    }
                }
                LazyVerticalGrid(
                    modifier = Modifier.weight(1f)
                        .fillMaxHeight()
                        .padding(top = 24.dp, bottom = 24.dp, end = 16.dp)
                        .background(Theme.colors.background)
                        .border(
                            width = 1.dp,
                            color = Theme.colors.divider,
                            shape = RoundedCornerShape(Theme.radius.medium)
                        ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    columns = GridCells.Fixed(2),
                ) {
                    if (state.offers.isNotEmpty()) {
                        items(state.offers) { offer ->
                            AnimatedVisibility(!state.isLoading) {
                                OfferItem(offer = offer)
                            }
                        }
                    }

                    items(10) {
                        AnimatedVisibility(
                            state.isLoading,
                            enter = fadeIn(animationSpec = tween(1000)),
                            exit = fadeOut(animationSpec = tween(1000))
                        ) {
                            OfferItemLoading()
                        }
                    }

                }

            }
        }
    }

    @Composable
    fun OfferItem(
        offer: OfferUiState,
    ) {
        Column(
            modifier = Modifier
                .size(200.dp)
                .border(1.dp, Theme.colors.divider, RoundedCornerShape(Theme.radius.medium)),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                contentDescription = null,
                painter = rememberImagePainter(offer.image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(RoundedCornerShape(
                        topStart = Theme.radius.medium,
                        topEnd = Theme.radius.medium
                    )
                ).weight(1f)
            )

            Text(
                text = offer.name,
                style = Theme.typography.body,
                color = Theme.colors.contentPrimary,
                modifier = Modifier.padding(16.kms)
            )
        }
    }

    @Preview
    @Composable
    fun OfferItemLoading() {
        val randomFloat = remember { (50..100).random().dp }
        Column(
            modifier = Modifier.clip(
                RoundedCornerShape(
                    topStart = Theme.radius.medium,
                    topEnd = Theme.radius.medium
                )
            ).shimmerEffect(durationMillis = 1500).size(200.dp)
                .border(1.dp, Theme.colors.divider,
                    RoundedCornerShape(Theme.radius.medium)
                ),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Box(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.fillMaxWidth().background(color = Theme.colors.background)
                .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(Theme.radius.medium))
                        .shimmerEffect(durationMillis = 1500).height(22.dp)
                        .width(randomFloat).align(Alignment.Center)
                )
            }
        }
    }
}