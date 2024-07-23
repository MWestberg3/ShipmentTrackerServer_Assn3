package shipping

import ShippingEvents.ShippingEvent
import ShippingEvents.ShippingEventType
import Strategies.*
import api.UpdateStrategy
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import shipping.factory.BasicShippingType
import shipping.factory.ShipmentFactory
import shipping.shipment.Shipment
import shipping.shipment.StandardShipment
import java.io.File
import java.util.*

class TrackingSimulator private constructor(){
    private val shipmentsMutex = Mutex()
    private val eventsMutex = Mutex()
    private var currentIndex = 0
    private var pause = false

    private var shipments: MutableList<Shipment> = Collections.synchronizedList(mutableListOf())
    private var events: MutableList<ShippingEvent> = Collections.synchronizedList(mutableListOf())

    companion object {
        @Volatile private var instance: TrackingSimulator? = null

        fun getInstance(): TrackingSimulator = instance ?: synchronized(this) {
            instance ?: TrackingSimulator().also { instance = it }
        }
    }

    suspend fun findShipmentById(id: String): Shipment? {
        shipmentsMutex.withLock {
            return shipments.find { it.id == id }
        }
    }
    suspend fun addShipment(shipment: Shipment) {
        shipmentsMutex.withLock {
            shipments.add(shipment)
        }
    }
    private fun setEvents(events: List<ShippingEvent>) {
        this.events = Collections.synchronizedList(events.toMutableList())
    }
    private fun strategyPicker5000(eventType: ShippingEventType): UpdateStrategy {
        return when (eventType) {
            ShippingEventType.CANCELLED -> CancelledStrategy()
            ShippingEventType.CREATED -> CreatedStrategy()
            ShippingEventType.DELAYED -> DelayedStrategy()
            ShippingEventType.DELIVERED -> DeliveredStrategy()
            ShippingEventType.LOCATION -> LocationStrategy()
            ShippingEventType.LOST -> LostStrategy()
            ShippingEventType.NOTE_ADDED -> NoteAddedStrategy()
            ShippingEventType.SHIPPED -> ShippedStrategy()
        }
    }
    private fun loadShipmentData() {
        val filepath = "/Users/mwestberg/IdeaProjects/test.txt"
        val shippingEvents = mutableListOf<ShippingEvent>()

        File(filepath).forEachLine { line ->
            val parts = line.split(",")
            val statusString = parts[0]
            val status = ShippingEventType.from(statusString)
            val id = parts[1]
            val timestamp = parts[2].toLong()
            val otherInfo = if (parts.size > 3) parts[3] else null

            shippingEvents.add(ShippingEvent(status, id, timestamp, otherInfo))
        }

        setEvents(shippingEvents)
    }

    suspend fun readInShipmentData(data: String) {
        pause = true
        eventsMutex.withLock {
            val parts = data.split(",")
            val statusString = parts[0]
            val status = ShippingEventType.from(statusString)
            val id = parts[1]
            val timestamp = parts[2].toLong()
            val otherInfo = if (parts.size > 3) parts[3] else null

            val newEvent = ShippingEvent(status, id, timestamp, otherInfo)
            if (currentIndex >= events.size) {
                events.add(newEvent)
            } else {
                events.add(currentIndex, newEvent)
            }
            pause = false
        }
    }

    suspend fun runSimulation() {
        loadShipmentData()
        if (eventsMutex.tryLock()) {
            try {
                while (true) {
                    if (pause) {
                        eventsMutex.unlock()
                        delay(500)
                        while (!eventsMutex.tryLock()) {
                            delay(500)
                        }
                        if (currentIndex >= events.size) break
                    }
                    val event = events[currentIndex]
                    var shipment = findShipmentById(event.shipmentID)
                    var isNewShipment = false
                    val strategy = strategyPicker5000(event.type)
                    if (shipment == null) {
                        if (event.type != ShippingEventType.CREATED) {
                            continue
                        }
                        shipment = if (event.otherInfo == null) {
                            BasicShippingType().createShipment("standard shipment", event.shipmentID)
                        } else {
                            BasicShippingType().createShipment(event.otherInfo, event.shipmentID)
                        }
                        addShipment(shipment)
                        isNewShipment = true
                    }
                    if (!isNewShipment) {
                        delay(1000L)
                    }
                    strategy.processUpdate(shipment, event)
                    if (currentIndex < events.size - 1) {
                        currentIndex++
                    }
                }
            } finally {
                if (eventsMutex.isLocked) {
                    eventsMutex.unlock()
                }

            }
        }
    }
}