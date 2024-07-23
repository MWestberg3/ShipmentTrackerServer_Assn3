package api

import shipping.shipment.Shipment
import ShippingEvents.ShippingEvent

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, event: ShippingEvent)
}