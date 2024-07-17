package shipping

import ShippingEvents.ShippingEventType
import api.ShipmentObserver
import org.junit.jupiter.api.Assertions.*

class ShipmentTest {
    @org.junit.jupiter.api.Test
    fun testShipmentCreation() {
        val shipment = Shipment("123")
        assertEquals("123", shipment.id)
        assertEquals(ShippingEventType.CREATED, shipment.status)
        assertEquals("unknown", shipment.currentLocation)
        assertNull(shipment.expectedDeliveryDate)
    }

    @org.junit.jupiter.api.Test
    fun testAddNote() {
        val shipment = Shipment("123")
        shipment.addNote("note1")
        shipment.addNote("note2")
        shipment.addNote("note3")
        assertEquals(3, shipment.notes.size)
        assertEquals("note3", shipment.notes[2])
    }

    @org.junit.jupiter.api.Test
    fun testAddUpdate() {
        val shipment = Shipment("123")
        val update = ShippingUpdate(ShippingEventType.CREATED, ShippingEventType.CANCELLED, 123456789)
        shipment.addUpdate(update)
        assertEquals(1, shipment.shippingUpdateHistory.size)
        assertEquals(ShippingEventType.CANCELLED, shipment.shippingUpdateHistory[0].newStatus)
    }

    @org.junit.jupiter.api.Test
    fun testRegisterObserver() {
        val shipment = Shipment("123")
        val observer = FalseObserver()
        shipment.registerObserver(observer)
        assertEquals(1, shipment.observers.size)
        assertEquals(observer, shipment.observers[0])
    }

    @org.junit.jupiter.api.Test
    fun testRemoveObserver() {
        val shipment = Shipment("123")
        val observer = FalseObserver()
        shipment.registerObserver(observer)
        assertEquals(1, shipment.observers.size)
        shipment.removeObserver(observer)
        assertEquals(0, shipment.observers.size)
    }
}

class FalseObserver: ShipmentObserver {
    override fun notify(shipment: Shipment) {
        shipment.addNote("note")
    }
}