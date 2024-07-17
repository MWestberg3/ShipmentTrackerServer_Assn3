package api

import shipping.Shipment
import ShippingEvents.ShippingEvent

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, event: ShippingEvent)
}