import shippingEvents.ShippingEvent

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, event: ShippingEvent)
}