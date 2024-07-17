package Strategies

import Shipment
import ShippingEvents.ShippingEvent
import ShippingUpdate
import api.UpdateStrategy

class CancelledStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        val update = ShippingUpdate(shipment.status, event.type, event.timestamp)
        shipment.status = update.newStatus
        shipment.addUpdate(update)
    }
}