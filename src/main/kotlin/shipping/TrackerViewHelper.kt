package shipping

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import api.ShipmentObserver

class TrackerViewHelper(private var simulator: TrackingSimulator) : ShipmentObserver {
    var shipmentId by mutableStateOf("")
        private set
    var shipmentNotes by mutableStateOf(mutableListOf<String>())
        private set
    var shipmentUpdateHistory by mutableStateOf(mutableListOf<ShippingUpdate>())
        private set
    var expectedShipmentDeliveryDate by mutableStateOf(mutableListOf<String>())
        private set
    var shipmentStatus by mutableStateOf("")
        private set
    var currentLocation by mutableStateOf("")
        private set

    fun trackShipment(id: String) {
        val shipment = simulator.findShipmentById(id)
        shipment?.let {
            it.registerObserver(this)
            this.shipmentId = it.id
            this.shipmentNotes = it.notes
            this.shipmentStatus = it.status.toString()
            this.shipmentUpdateHistory = it.shippingUpdateHistory.toMutableList()
            this.expectedShipmentDeliveryDate = mutableListOf(it.expectedDeliveryDate.toString())
            this.currentLocation = it.currentLocation
        }
    }

    fun stopTracking() {
        val shipment = simulator.findShipmentById(shipmentId)
        shipment?.removeObserver(this)
    }

    override fun notify(shipment: Shipment) {
        this.shipmentId = shipment.id
        this.shipmentNotes = shipment.notes.toMutableList()
        this.shipmentStatus = shipment.status.toString()
        this.shipmentUpdateHistory = shipment.shippingUpdateHistory.toMutableList()
        this.expectedShipmentDeliveryDate = mutableListOf(shipment.expectedDeliveryDate.toString())
        this.currentLocation = shipment.currentLocation
    }
}