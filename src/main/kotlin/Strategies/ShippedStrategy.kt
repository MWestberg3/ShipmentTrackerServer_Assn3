package Strategies

import shipping.Shipment
import ShippingEvents.ShippingEvent
import shipping.ShippingUpdate
import api.UpdateStrategy

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.expectedDeliveryDate = event.otherInfo!!.toLong()
        val update = ShippingUpdate(shipment.status, event.type, event.timestamp)
        shipment.status = update.newStatus
        shipment.addUpdate(update)
    }
}