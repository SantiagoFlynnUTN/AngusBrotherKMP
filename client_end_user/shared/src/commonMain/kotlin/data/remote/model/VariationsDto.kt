package data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class VariationsDto(val variations: List<String>)