package Strategies

import shipping.Shipment
import ShippingEvents.ShippingEvent
import shipping.ShippingUpdate
import api.UpdateStrategy

class CreatedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        val update = ShippingUpdate(null, event.type, event.timestamp)
        shipment.status = update.newStatus
        shipment.addUpdate(update)
    }
}
