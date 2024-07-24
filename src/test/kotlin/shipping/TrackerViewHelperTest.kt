package shipping

import ShippingEvents.ShippingEventType
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import shipping.shipment.Shipment
import shipping.shipment.StandardShipment

class TrackerViewHelperTest {
    private lateinit var trackerViewHelper: TrackerViewHelper
    private val simulator: TrackingSimulator = TrackingSimulator.getInstance()
    private val shipment: Shipment = StandardShipment("123").apply {
        addNote("Test Note")
        addUpdate(ShippingUpdate(ShippingEventType.CREATED, ShippingEventType.DELIVERED, 123456789))
        status = ShippingEventType.DELIVERED
        currentLocation = "Test Location"
    }

    @BeforeEach
    fun setUp() = runBlocking{
        trackerViewHelper = TrackerViewHelper(simulator)
        simulator.addShipment(shipment)
    }

    @Test
    fun trackShipmentTest() = runBlocking {
        val shipmentId = "123"
        trackerViewHelper.trackShipment(shipmentId)
        assertEquals(shipmentId, trackerViewHelper.shipmentId)
        assertEquals(shipment.notes, trackerViewHelper.shipmentNotes)
        assertEquals(shipment.shippingUpdateHistory, trackerViewHelper.shipmentUpdateHistory)
        assertEquals(shipment.currentLocation, trackerViewHelper.currentLocation)
        assertEquals(shipment.status.toString(), trackerViewHelper.shipmentStatus)
    }

    @Test
    fun trackShipmentNullCheck() = runBlocking {
        val shipmentId = "1"
        assertEquals(shipment.observers.size, 0)
        trackerViewHelper.trackShipment(shipmentId)
        assertEquals(shipment.observers.size, 0)
    }

    @Test
    fun stopTracking() = runBlocking {
        val shipmentId = "123"
        assertEquals(shipment.observers.size, 0)
        trackerViewHelper.trackShipment(shipmentId)
        assertEquals(shipmentId, trackerViewHelper.shipmentId)
        assertEquals(shipment.observers.size, 0)
        trackerViewHelper.stopTracking()
        assertEquals(shipment.observers.size, 0)
    }

    @Test
    fun stopTrackingNull() = runBlocking {
        val shipment: Shipment = StandardShipment("0")
        assertEquals(shipment.observers.size, 0)
        trackerViewHelper.stopTracking()
        assertEquals(shipment.observers.size, 0)
    }

    @Test
    fun testNotify() = runBlocking {
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