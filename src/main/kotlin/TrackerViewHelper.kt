import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TrackerViewHelper(private var simulator: TrackingSimulator) : ShipmentObserver {
    var shipmentId by mutableStateOf("")
        private set
    var shipmentNotes by mutableStateOf(mutableListOf<String>())
        private set
    var shipmentUpdateHistory by mutableStateOf(mutableListOf<String>())
        private set
    var expectedShipmentDeliveryDate by mutableStateOf(mutableListOf<String>())
        private set
    var shipmentStatus by mutableStateOf("Created")
        private set

    fun trackShipment(id: String) {
        val shipment = simulator.findShipmentById(id)
        shipment?.registerObserver(this)
    }

    fun stopTracking() {
        val shipment = TrackingSimulator().findShipmentById(shipmentId)
        shipment?.removeObserver(this)
    }

    override fun notify(shipment: Shipment) {
        this.shipmentId = shipment.id
        this.shipmentNotes = shipment.notes
        this.shipmentStatus = shipment.status.toString()
        this.shipmentUpdateHistory = shipment.shippingUpdateHistory.map { it.newStatus.toString() }.toMutableList()
        this.expectedShipmentDeliveryDate = mutableListOf(shipment.expectedDeliveryDate.toString())
    }
}