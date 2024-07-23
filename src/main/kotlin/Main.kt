import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import shipping.TrackerViewHelper
import shipping.TrackingSimulator
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.ZoneId

@Composable
@Preview
fun App(trackingSimulator: TrackingSimulator) {
    val coroutineScope = rememberCoroutineScope()
    var trackerViewHelpers by remember { mutableStateOf(listOf<TrackerViewHelper>()) }
    var shipmentIdInput by remember { mutableStateOf(TextFieldValue("")) }
    var shipmentNotFound by remember { mutableStateOf<String?>(null)}
    val scrollState = rememberScrollState()

    MaterialTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(16.dp)
                .verticalScroll(scrollState),
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
                            val newTrackerViewHelper = TrackerViewHelper(trackingSimulator).apply {
                                trackShipment(shipmentIdInput.text)
                            }
                            trackerViewHelpers = trackerViewHelpers + newTrackerViewHelper
                        } else {
                            shipmentNotFound = "Shipment ID not yet created: ${shipmentIdInput.text}"
                        }
                    }
                }) {
                    Text("Look up")
                }
            }
            if (shipmentNotFound != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(2.dp, Color.Red)
                        .background(Color.White)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = shipmentNotFound!!, color = Color.Red)
                        Button(onClick = { shipmentNotFound = null }) {
                            Text("X")
                        }
                    }
                }
            }
            trackerViewHelpers.forEachIndexed { index, trackerViewHelper ->
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .border(2.dp, Color.Black)
                            .background(Color.White)
                            .padding(8.dp),
                    ) {
                        Text(text = formatTrackingInfo(trackerViewHelper) ?: "", color = Color.Black)
                        Text(text = formatStatusUpdates(trackerViewHelper) ?: "", color = Color.Black)
                        Text(text = formatNotesUpdates(trackerViewHelper) ?: "", color = Color.Black)
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                trackerViewHelper.stopTracking() // stop tracking
                                trackerViewHelpers = trackerViewHelpers.toMutableList().also { it.removeAt(index) }
                            }
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text("X")
                        }
                }
            }
        }
    }
}

fun formatTrackingInfo(trackerViewHelper: TrackerViewHelper): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return buildString {
        append("Tracking Shipment: ${trackerViewHelper.shipmentId}\n")
        append("Status: ${trackerViewHelper.shipmentStatus}\n")
        append("Location: ${trackerViewHelper.currentLocation}\n")
        append("Expected Delivery Date: ")
        if (trackerViewHelper.expectedShipmentDeliveryDate.isEmpty() || trackerViewHelper.expectedShipmentDeliveryDate.last() == "null") {
            append("--")
        } else {
            val timestamp = trackerViewHelper.expectedShipmentDeliveryDate.last().toLong()
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
            append(formatter.format(dateTime))
        }
    }
}

fun formatStatusUpdates(trackerViewHelper: TrackerViewHelper): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return buildString {
        append("\nStatus Updates:\n")
        if (trackerViewHelper.shipmentUpdateHistory.size < 2) {
            append("No updates yet\n")
        } else {
            for (i in 1 until trackerViewHelper.shipmentUpdateHistory.size) {
                val previousStatus = trackerViewHelper.shipmentUpdateHistory[i].previousStatus
                val currentStatus = trackerViewHelper.shipmentUpdateHistory[i].newStatus
                val timestamp = trackerViewHelper.shipmentUpdateHistory[i].timestamp
                val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                if (previousStatus != currentStatus)
                    append("Shipment went from $previousStatus to $currentStatus at $dateTime\n")
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

fun main() = application {
    val trackingSimulator = TrackingSimulator.getInstance()

    Window(onCloseRequest = ::exitApplication) {
        App(trackingSimulator)
    }

    CoroutineScope(Dispatchers.Default).launch {
        trackingSimulator.runSimulation()
    }

    CoroutineScope(Dispatchers.IO).launch {
        embeddedServer(Netty, 8080) {
            routing {
                get("/") {
                    call.respondText(File("index.html").readText(), ContentType.Text.Html)
                }

                post("/data") {
                    val data = call.receiveText()
                    print(data)
                    trackingSimulator.readInShipmentData(data)
                    call.respondText { "Shipment Tracked" }
                }
            }
        }.start(wait = true)
    }

}
