package Strategies

import Shipment
import ShippingEvents.ShippingEvent
import ShippingUpdate
import UpdateStrategy

class CreatedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        val update = ShippingUpdate(null, event.type, event.timestamp)
        shipment.status = update.newStatus
        shipment.addUpdate(update)
    }
}
