import org.testng.AssertJUnit.assertEquals
import org.testng.annotations.Test

class ShipmentTest {
    @Test
    fun testShipment() {
        val shipment = Shipment(
            "In transit",
            "1", mutableListOf("Note 1", "Note 2"),
            1626345600,
            "San Diego, CA")
        assert(shipment.status == "In transit")
        assert(shipment.id == "1")
        assert(shipment.notes == mutableListOf("Note 1", "Note 2"))
        assertEquals(shipment.expectedDeliveryDate, 1626345600)

    }
}