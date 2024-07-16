class ShippedUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment) {
        shipment.expectedDeliveryDate = shipment.otherInfo as Long
        val update = ShippingUpdate(shipment.shippingUpdateHistory.lastOrNull()?.newStatus, shipment.status, shipment.timestamp)
        shipment.addUpdate(update)
    }
}