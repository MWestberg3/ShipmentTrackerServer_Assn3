import ShippingEvents.ShippingEvent
import ShippingEvents.ShippingEventType
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
fun App() {
    val trackerViewHelper = remember { TrackerViewHelper() }
    val coroutineScope = rememberCoroutineScope()
    val trackingSimulator = TrackingSimulator()
    trackingSimulator.setEvents(loadShipmentData())
    trackingSimulator.runSimulation()
    var shipmentIdInput by remember { mutableStateOf(TextFieldValue("")) }
    var searchResult by remember { mutableStateOf<String?>(null)}

    MaterialTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White),
            ) {
                TextField(value = shipmentIdInput,
                    onValueChange = { shipmentIdInput = it },
                    label = { Text("Enter shipment ID") })
                Button(onClick = {
                    coroutineScope.launch {
                        val found = trackingSimulator.findShipmentById(shipmentIdInput.text)
                        searchResult = if (found != null) "Shipment found!" else "Shipment not found!"
                    }
                }) {
                    Text("Look up Shipment ID")
                }
                if (searchResult != null) {
                    Text(searchResult!!)
                }
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
        App()
    }
}
