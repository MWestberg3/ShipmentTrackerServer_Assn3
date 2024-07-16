import ShippingEvents.ShippingEvent
import ShippingEvents.ShippingEventType
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch
import java.io.File

@Composable
@Preview
fun App(trackingSimulator: TrackingSimulator) {
    val coroutineScope = rememberCoroutineScope()
    val trackerViewHelper = remember { TrackerViewHelper(trackingSimulator) }
    var shipmentIdInput by remember { mutableStateOf(TextFieldValue("")) }
    var trackingInfo by remember { mutableStateOf<String?>(null)}
    var updateInfo by remember { mutableStateOf<String?>(null)}
    var notesInfo by remember { mutableStateOf<String?>(null)}
    var shipmentNotFound by remember { mutableStateOf<String?>(null)}

    val trackerProperties = listOf(
        trackerViewHelper.shipmentId,
        trackerViewHelper.expectedShipmentDeliveryDate,
        trackerViewHelper.shipmentStatus,
        trackerViewHelper.currentLocation
    )

    LaunchedEffect(trackerProperties) {
        trackingInfo = formatTrackingInfo(trackerViewHelper)
    }

    LaunchedEffect(trackerViewHelper.shipmentUpdateHistory) {
        updateInfo = formatStatusUpdates(trackerViewHelper)
    }

    LaunchedEffect(trackerViewHelper.shipmentNotes) {
        notesInfo = formatNotesUpdates(trackerViewHelper)
    }

    MaterialTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White),
            ) {
                TextField(value = shipmentIdInput,
                    onValueChange = { shipmentIdInput = it },
                    label = { Text("Enter shipment ID") })
                Button(onClick = {
                    coroutineScope.launch {
                        val found = trackingSimulator.findShipmentById(shipmentIdInput.text)
                        if (found != null) {
                            trackerViewHelper.trackShipment(shipmentIdInput.text)
                        } else {
                            shipmentNotFound = "Shipment ID not found: ${shipmentIdInput.text}"
                        }
                    }
                }) {
                    Text("Look up")
                }
            }
            Text(text = trackingInfo ?: "", color = Color.Black)
            Text(text = updateInfo ?: "", color = Color.Black)
            Text(text = notesInfo ?: "", color = Color.Black)
        }
    }
}

fun formatTrackingInfo(trackerViewHelper: TrackerViewHelper): String {
    return buildString {
        append("Tracking Shipment: ${trackerViewHelper.shipmentId}\n")
        append("Status: ${trackerViewHelper.shipmentStatus}\n")
        append("Location: ${trackerViewHelper.currentLocation}\n")
        append("Expected Delivery Date: ")
        if (trackerViewHelper.expectedShipmentDeliveryDate.isEmpty() || trackerViewHelper.expectedShipmentDeliveryDate.last() == "null") {
            append("--")
        } else {
            append(trackerViewHelper.expectedShipmentDeliveryDate.last())
        }
    }
}

fun formatStatusUpdates(trackerViewHelper: TrackerViewHelper): String {
    return buildString {
        append("\nStatus Updates:\n")
        if (trackerViewHelper.shipmentUpdateHistory.size < 2) {
            append("No updates yet\n")
        } else {
            for (i in 1 until trackerViewHelper.shipmentUpdateHistory.size) {
                val previousStatus = trackerViewHelper.shipmentUpdateHistory[i - 1]
                val currentStatus = trackerViewHelper.shipmentUpdateHistory[i]
                if (previousStatus != currentStatus)
                    append("Shipment went from $previousStatus to $currentStatus\n")
        }
        }
    }
}

fun formatNotesUpdates(trackerViewHelper: TrackerViewHelper): String {
    return buildString {
        append("Notes:\n")
        if (trackerViewHelper.shipmentNotes.size < 1) {
            append("No notes yet\n")
        } else {
            for (note in trackerViewHelper.shipmentNotes) {
                append("$note\n")
            }
        }
    }
}


fun loadShipmentData(): MutableList<ShippingEvent> {
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

    return shippingEvents
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        val trackingSimulator = TrackingSimulator()
        val coroutineScope = rememberCoroutineScope()

        coroutineScope.launch {
            trackingSimulator.setEvents(loadShipmentData())
            trackingSimulator.runSimulation()
        }
        App(trackingSimulator)
    }
}
