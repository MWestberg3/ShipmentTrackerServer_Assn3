interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, event: ShippingEvent)
}