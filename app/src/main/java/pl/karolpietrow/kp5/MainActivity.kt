package pl.karolpietrow.kp5

import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.karolpietrow.kp5.ui.theme.KP5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KP5Theme {
                val viewModel: MyViewModel = viewModel()
                MainApp(viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MyViewModel) {
    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        val screenCount by viewModel.screenCount.collectAsState()
        val currentPage by viewModel.currentScreen.collectAsState()
        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT


        MainTopAppBar (
            screenCount = screenCount,
            onScreenCountChange = { newCount -> viewModel.setScreenCount(newCount) }
        )

        if (isPortrait) { // Orientacja pionowa
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(currentPage) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                if (dragAmount < -50 && currentPage < screenCount) {
                                    viewModel.setCurrentScreen(currentPage + 1)
                                } else if (dragAmount > 50 && currentPage > 1) {
                                    viewModel.setCurrentScreen(currentPage - 1)
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                when (currentPage) {
                    1 -> Screen1(viewModel)
                    2 -> Screen2(viewModel)
                    3 -> Screen3(viewModel, activity = LocalContext.current as ComponentActivity)
                }
            }
        } else { // Orientacja pozioma
            Row(modifier = Modifier.fillMaxSize()) {
                if (screenCount >= 1) {
                    Screen1(viewModel, modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(8.dp)
                    )
                }
                if (screenCount >= 2) { Screen2(viewModel, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp)
                ) }
                if (screenCount >= 3) { Screen3(viewModel, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp),
                    activity = LocalContext.current as ComponentActivity
                ) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(screenCount: Int, onScreenCountChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("Aplikacja SMS (Aktywne ekrany: $screenCount)")
        },
        actions = {
            IconButton(onClick = { expanded = true } ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Select number of screens"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                (1..3).forEach { count ->
                    DropdownMenuItem(
                        text = { Text("$count") },
                        onClick = {
                            onScreenCountChange(count)
                            expanded = false
                        }
                    )
                }
            }
        },
    )

}

@Composable
fun Screen1(viewModel: MyViewModel, modifier: Modifier = Modifier) {
    val place1 = viewModel.place1.collectAsState().value
    val place2 = viewModel.place2.collectAsState().value

    Column(
        modifier = modifier
//            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Ekran 1",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
//            modifier = modifier.padding(5.dp),
            text = "Wprowadź numer telefonu oraz treść wiadomości SMS",
            fontSize = 15.sp
        )
        TextField(
            value = place1,
            onValueChange = { viewModel.updatePlace1(it) },
            label = { Text("Numer telefonu")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(text=place1)
        TextField(
            value = place2,
            onValueChange = { viewModel.updatePlace2(it) },
            label = { Text("Treść wiadomości")}
        )
        Text(text=place2)
    }
}

@Composable
fun Screen2(viewModel: MyViewModel, modifier: Modifier = Modifier) {
    val place1 = viewModel.place1.collectAsState().value
    val place2 = viewModel.place2.collectAsState().value


    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Ekran 2",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = modifier.padding(5.dp),
            text = "Numer telefonu:",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(
            modifier = modifier.padding(5.dp),
            text = place1,
            fontSize = 15.sp
        )
        Text(
            modifier = modifier.padding(5.dp),
            text = "Treść wiadomości SMS:",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Text(
            modifier = modifier.padding(5.dp),
            text = place2,
            fontSize = 15.sp
        )
    }
}

@Composable
fun Screen3(viewModel: MyViewModel, modifier: Modifier = Modifier, activity: ComponentActivity) {
    val phoneNumber = viewModel.place1.collectAsState().value
    val message = viewModel.place2.collectAsState().value

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Ekran 3",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Button(
            onClick = {
                sendSms(activity, phoneNumber, message)
            },
            modifier = Modifier
                .height(75.dp)
                .width(150.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send message",
                modifier = Modifier
                    .padding(end=8.dp)

            )
            Text("Wyślij SMS")
        }
    }
}

fun sendSms(activity: ComponentActivity, phoneNumber: String, message: String) {
    if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.SEND_SMS), 1)
        return
    }

    try {
        val smsManager: SmsManager = activity.getSystemService(SmsManager::class.java)
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(activity, "Wiadomość wysłana!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        println("Błąd podczas wysyłania wiadomości SMS")
        Toast.makeText(activity, "Błąd podczas wysyłania wiadomości SMS!", Toast.LENGTH_SHORT).show()
    }
}