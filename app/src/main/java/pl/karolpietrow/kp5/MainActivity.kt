package pl.karolpietrow.kp5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        val currentPage by remember { mutableIntStateOf(1) }
        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT


        MainTopAppBar (
            screenCount = screenCount,
            onScreenCountChange = { newCount -> viewModel.setScreenCount(newCount) }
        )

        if (isPortrait) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { change, dragAmount ->
                            change.consume()
                            if (dragAmount < -50 && currentPage < screenCount - 1) {
                                viewModel.setCurrentScreen(currentPage+1)
                            } else if (dragAmount > 50 && currentPage > 0) {
                                viewModel.setCurrentScreen(currentPage-1)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                when (currentPage) {
                    1 -> Screen1(viewModel)
                    2 -> Screen2(viewModel)
                    3 -> Screen3(viewModel)
                }
            }
        } else {
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
                    .padding(8.dp)
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
            Text("Aplikacja SMS")
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


//@Composable
//fun MyDropdownMenu() {
//
//    Column {
//        Text("Wybierz liczbę ekranów:", style = MaterialTheme.typography.titleMedium)
//        Box {
//            Button(onClick = { expanded = true }) {
//                Text("Liczba ekranów: $screenCount")
//            }
//
//        }
//    }
//}

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
            label = { Text("Numer telefonu")}
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
fun Screen3(viewModel: MyViewModel, modifier: Modifier = Modifier) {
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