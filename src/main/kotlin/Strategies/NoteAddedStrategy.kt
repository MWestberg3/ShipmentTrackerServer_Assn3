package Strategies

import shipping.Shipment
import ShippingEvents.ShippingEvent
import api.UpdateStrategy

class NoteAddedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.addNote(event.otherInfo!!)
    }
}