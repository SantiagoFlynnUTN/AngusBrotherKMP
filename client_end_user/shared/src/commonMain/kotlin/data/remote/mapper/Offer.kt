package data.remote.mapper

import data.remote.model.OfferDto
import domain.entity.Offer

fun OfferDto.toEntity() = Offer(
    id = id ?: "",
    title = name ?: "",
    imageUrl = image ?: "",
    restaurants = restaurants?.map { it.toEntity() }?.toMutableList() ?: mutableListOf()
)

fun List<OfferDto>.toEntity() = map { it.toEntity() }
