package Strategies

import Shipment
import ShippingEvents.ShippingEvent
import UpdateStrategy

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.currentLocation = event.otherInfo!!
    }
}