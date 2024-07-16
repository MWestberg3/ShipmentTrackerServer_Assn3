package strategies

import Shipment
import shippingEvents.ShippingEvent
import ShippingUpdate
import UpdateStrategy

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.expectedDeliveryDate = event.otherInfo!!.toLong()
        val update = ShippingUpdate(shipment.status, event.type, event.timestamp)
        shipment.status = update.newStatus
        shipment.addUpdate(update)
    }
}