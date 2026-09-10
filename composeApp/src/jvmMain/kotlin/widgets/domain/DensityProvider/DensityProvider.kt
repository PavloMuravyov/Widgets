package widgets.domain.DensityProvider

import androidx.compose.ui.unit.Density
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.awt.Toolkit

class DensityProvider() {
    private val _density = MutableStateFlow(computeInitialDensity())
    val density: StateFlow<Density> = _density.asStateFlow()


    fun update(newDensity: Density) {
        _density.value = newDensity
    }
    fun computeInitialDensity(): Density {
        val dpi = Toolkit.getDefaultToolkit().screenResolution
        return Density(dpi / 96f)
    }
}



