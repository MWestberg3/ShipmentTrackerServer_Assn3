import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.Assert.assertFalse
import shipping.Shipment
import shipping.ShippingUpdate
import ShippingEvents.ShippingEventType
import api.ShipmentObserver

class ShipmentTest {

    private lateinit var shipment: Shipment
    private lateinit var testObserver: TestObserver

    @BeforeMethod
    fun setUp() {
        shipment = Shipment("123")
        testObserver = TestObserver()
        shipment.registerObserver(testObserver)
    }

    @Test
    fun testInitialization() {
        assertEquals("123", shipment.id)
        assertEquals(ShippingEventType.CREATED, shipment.status)
        assertTrue(shipment.shippingUpdateHistory.isEmpty())
        assertTrue(shipment.notes.isEmpty())
        assertEquals("unknown", shipment.currentLocation)
    }

    @Test
    fun testAddNote() {
        shipment.addNote("Test note")
        assertTrue(shipment.notes.contains("Test note"))
        assertTrue(testObserver.isNotified)
    }

    @Test
    fun testAddUpdate() {
        val update = ShippingUpdate(ShippingEventType.DELIVERED, ShippingEventType.DELIVERED, System.currentTimeMillis())
        shipment.addUpdate(update)
        assertTrue(shipment.shippingUpdateHistory.contains(update))
        assertTrue(testObserver.isNotified)
    }

    @Test
    fun testObserverRegistrationAndNotification() {
        val newObserver = TestObserver()
        shipment.registerObserver(newObserver)
        shipment.addNote("Another test note")
        assertTrue(newObserver.isNotified)

        shipment.removeObserver(newObserver)
        newObserver.isNotified = false // Reset for next test
        shipment.addNote("Note after removal")
        assertFalse(newObserver.isNotified)
    }

    class TestObserver : ShipmentObserver {
        var isNotified: Boolean = false

        override fun notify(shipment: Shipment) {
            isNotified = true
        }
    }
}