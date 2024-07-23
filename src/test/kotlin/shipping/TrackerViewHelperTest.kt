package shipping

import ShippingEvents.ShippingEventType
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import shipping.shipment.Shipment

class TrackerViewHelperTest {
    private lateinit var trackerViewHelper: TrackerViewHelper
    private val simulator: TrackingSimulator = TrackingSimulator()
    private val shipment: Shipment = Shipment("123").apply {
        addNote("Test Note")
        addUpdate(ShippingUpdate(ShippingEventType.CREATED, ShippingEventType.DELIVERED, 123456789))
        status = ShippingEventType.DELIVERED
        currentLocation = "Test Location"
    }

    @BeforeEach
    fun setUp() {
        trackerViewHelper = TrackerViewHelper(simulator)
        simulator.addShipment(shipment)
    }

    @Test
    fun trackShipmentTest() {
        val shipmentId = "123"
        trackerViewHelper.trackShipment(shipmentId)
        assertEquals(shipmentId, trackerViewHelper.shipmentId)
        assertEquals(shipment.notes, trackerViewHelper.shipmentNotes)
        assertEquals(shipment.shippingUpdateHistory, trackerViewHelper.shipmentUpdateHistory)
        assertEquals(shipment.currentLocation, trackerViewHelper.currentLocation)
        assertEquals(shipment.status.toString(), trackerViewHelper.shipmentStatus)
    }

    @Test
    fun trackShipmentNullCheck() {
        val shipmentId = "1"
        assertEquals(shipment.observers.size, 0)
        trackerViewHelper.trackShipment(shipmentId)
        assertEquals(shipment.observers.size, 0)
    }

    @Test
    fun stopTracking() {
        val shipmentId = "123"
        assertEquals(shipment.observers.size, 0)
        trackerViewHelper.trackShipment(shipmentId)
        assertEquals(shipmentId, trackerViewHelper.shipmentId)
        assertEquals(shipment.observers.size, 1)
        trackerViewHelper.stopTracking()
        assertEquals(shipment.observers.size, 0)
    }

    @Test
    fun stopTrackingNull() {
        val shipment: Shipment = Shipment("0")
        assertEquals(shipment.observers.size, 0)
        trackerViewHelper.stopTracking()
        assertEquals(shipment.observers.size, 0)
    }

    @Test
    fun testNotify() {
        val shipmentId = "123"
        trackerViewHelper.trackShipment(shipmentId)
        assertEquals(shipmentId, trackerViewHelper.shipmentId)
        assertEquals(shipment.notes, trackerViewHelper.shipmentNotes)
        assertEquals(shipment.shippingUpdateHistory, trackerViewHelper.shipmentUpdateHistory)
        assertEquals(shipment.currentLocation, trackerViewHelper.currentLocation)
        assertEquals(shipment.status.toString(), trackerViewHelper.shipmentStatus)
        shipment.addNote("New Note")
        shipment.addUpdate(ShippingUpdate(ShippingEventType.DELIVERED, ShippingEventType.LOST, 123456789))
        shipment.status = ShippingEventType.LOST
        shipment.currentLocation = "New Location"
        trackerViewHelper.notify(shipment)
        assertEquals(shipmentId, trackerViewHelper.shipmentId)
        assertEquals(shipment.notes, trackerViewHelper.shipmentNotes)
        assertEquals(shipment.shippingUpdateHistory, trackerViewHelper.shipmentUpdateHistory)
        assertEquals(shipment.currentLocation, trackerViewHelper.currentLocation)
        assertEquals(shipment.status.toString(), trackerViewHelper.shipmentStatus)
    }
}