import ShippingEvents.ShippingEvent
import ShippingEvents.ShippingEventType
import Strategies.*
import kotlinx.coroutines.delay

class TrackingSimulator {
    private var shipments: MutableList<Shipment> = mutableListOf()
    private var events: MutableList<ShippingEvent> = mutableListOf()

    fun findShipmentById(id: String): Shipment? {
        return shipments.find { it.id == id }
    }
    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }
    fun setEvents(events: List<ShippingEvent>) {
        this.events = events.toMutableList()
    }
    private fun strategyPicker5000(eventType: ShippingEventType): UpdateStrategy {
        return when (eventType) {
            ShippingEventType.CANCELLED -> CancelledStrategy()
            ShippingEventType.CREATED -> CreatedStrategy()
            ShippingEventType.DELAYED -> DelayedStrategy()
            ShippingEventType.DELIVERED -> DeliveredStrategy()
            ShippingEventType.LOCATION -> LocationStrategy()
            ShippingEventType.LOST -> LostStrategy()
            ShippingEventType.NOTE_ADDED -> NoteAddedStrategy()
            ShippingEventType.SHIPPED -> ShippedStrategy()
        }
    }
    suspend fun runSimulation() {
        events.forEach { event ->
            var shipment = findShipmentById(event.shipmentID)
            var isNewShipment = false
            val strategy = strategyPicker5000(event.type)
            if (shipment == null) {
                // error check for if shipment hasn't been created but trying to modify?
                shipment = Shipment(event.shipmentID)
                addShipment(shipment)
                isNewShipment = true
            }
            strategy.processUpdate(shipment, event)
            if (!isNewShipment) {
                delay(1000L)
            }
        }
    }
}