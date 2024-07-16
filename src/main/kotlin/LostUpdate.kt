class LostUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment) {
        val update = ShippingUpdate(shipment.shippingUpdateHistory.lastOrNull()?.newStatus, shipment.status, shipment.timestamp)
        shipment.addUpdate(update)
    }
}