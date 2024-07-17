package Strategies

import shipping.Shipment
import ShippingEvents.ShippingEvent
import shipping.ShippingUpdate
import api.UpdateStrategy

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.currentLocation = event.otherInfo!!
        val update = ShippingUpdate(shipment.status, shipment.status, event.timestamp)
        shipment.addUpdate(update)
    }
}