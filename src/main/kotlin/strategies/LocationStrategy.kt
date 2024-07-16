package strategies

import Shipment
import shippingEvents.ShippingEvent
import ShippingUpdate
import UpdateStrategy

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.currentLocation = event.otherInfo!!
        val update = ShippingUpdate(shipment.status, shipment.status, event.timestamp)
        shipment.addUpdate(update)
    }
}