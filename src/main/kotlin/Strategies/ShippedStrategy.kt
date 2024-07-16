package Strategies

import Shipment
import ShippingEvents.ShippingEvent
import ShippingUpdate
import UpdateStrategy

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.expectedDeliveryDate = event.otherInfo!!.toLong()
        val update = ShippingUpdate(shipment.status, event.type, event.timestamp)
        shipment.addUpdate(update)
    }
}