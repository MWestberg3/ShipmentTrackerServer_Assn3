class DelayedUpdate : UpdateStrategy {
    override fun processUpdate(shipment: Shipment) {
        shipment.expectedDeliveryDate = shipment.otherInfo!!.toLong()
        val update = ShippingUpdate(shipment.shippingUpdateHistory.lastOrNull()?.newStatus, shipment.status, shipment.timestamp)
        shipment.addUpdate(update)
    }
}