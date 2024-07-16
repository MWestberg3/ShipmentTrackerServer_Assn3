class LocationUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.currentLocation = event.otherInfo!!
    }
}