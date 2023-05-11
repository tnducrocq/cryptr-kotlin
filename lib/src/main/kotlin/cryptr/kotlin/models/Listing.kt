package cryptr.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Listing")
data class Listing<T : CryptrResource>(
    @SerialName("__type__") override val cryptrType: String = "List",
    @SerialName("data") val data: Set<T> = setOf(),
    @SerialName("pagination") val pagination: Pagination,
    val total: Int = 0
) : CryptrResource()