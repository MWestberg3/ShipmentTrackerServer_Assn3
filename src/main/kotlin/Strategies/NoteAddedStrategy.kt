package Strategies

import Shipment
import ShippingEvents.ShippingEvent
import UpdateStrategy

class NoteAddedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, event: ShippingEvent) {
        shipment.addNote(event.otherInfo!!)
    }
}