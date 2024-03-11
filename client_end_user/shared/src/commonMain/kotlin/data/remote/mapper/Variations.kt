package data.remote.mapper

import data.remote.model.VariationsDto
import domain.entity.Variations

fun VariationsDto.toEntity() = Variations(variations = variations)

fun List<VariationsDto>.toEntity() = map { it.toEntity() }