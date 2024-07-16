import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TrackerViewHelper() : ShipmentObserver {
    var shipmentId by mutableStateOf("")
        private set
    var shipmentTotes by mutableStateOf(mutableListOf<String>())
        private set
    var shipmentUpdateHistory by mutableStateOf(mutableListOf<String>())
        private set
    var expectedShipmentDeliveryDate by mutableStateOf(mutableListOf<String>())
        private set
    var shipmentStatus by mutableStateOf("")
        private set

    fun trackShipment(id: String) {

    }

    fun stopTracking() {

    }

    override fun notify(shipment: Shipment) {
        TODO("Not yet implemented")
    }
}