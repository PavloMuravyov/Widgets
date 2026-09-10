package widgets.widgetsModule.widgets.celsius.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocalizedLocation(
    val name: String? = null,
    val country: String? = null,
)
