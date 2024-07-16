package strategies

import Shipment
import shippingEvents.ShippingEvent
import ShippingUpdate
import UpdateStrategy

class DeliveredStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        val update = ShippingUpdate(shipment.status, event.type, event.timestamp)
        shipment.status = update.newStatus
        shipment.addUpdate(update)
    }
}