package api

import Shipment
import ShippingEvents.ShippingEvent

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, event: ShippingEvent)
}