import ShippingEvents.ShippingEvent

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, event: ShippingEvent)
}