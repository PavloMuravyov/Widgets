package widgets.widgetsModule.widgets.celsius.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HereResponseDTO(
    val items: List<HereItem> = emptyList()
)

@Serializable
data class HereItem(
    val title: String = "",       // ← додай
    val address: HereAddress
)

@Serializable
data class HereAddress(
    val city: String? = null,
    val county: String? = null,
    val countryName: String? = null,
    val countryCode: String? = null,
)