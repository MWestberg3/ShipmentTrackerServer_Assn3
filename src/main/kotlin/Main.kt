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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
@Preview
fun App() {
    val trackerViewHelper = remember { TrackerViewHelper() }
    val coroutineScope = rememberCoroutineScope()
    val shipments = loadShipmentData()
    val trackingSimulator = TrackingSimulator(shipments)
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

fun loadShipmentData(): MutableList<Shipment> {
    val shipment1 = Shipment(
        "In transit",
        "1",
        mutableListOf("Note 1", "Note 2"),
        mutableListOf(ShippingUpdate("In transit", "Location 1", 1652712855468)),
        1626345600,
        "Location 1"
    )
    val shipment2 = Shipment(
        "Delivered",
        "2",
        mutableListOf("Note 1", "Note 2"),
        mutableListOf(ShippingUpdate("In transit", "Location 1", 1652712855468)),
        1626345600,
        "Location 1"
    )
    return mutableListOf(shipment1, shipment2)
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
