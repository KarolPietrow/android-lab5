package pl.karolpietrow.kp5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
    val screenCount by viewModel.screenCount.collectAsState()
    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        MainTopAppBar (
            screenCount = screenCount,
            onScreenCountChange = { newCount -> viewModel.setScreenCount(newCount) }
        )
        Screen1(viewModel)
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
fun Screen1(viewModel: MyViewModel) {
    val place1 = viewModel.place1.collectAsState().value
    Column(
        modifier = Modifier
//            .fillMaxHeight()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Ekran 1",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(15.dp),
            text = "Wprowadź numer telefonu oraz treść wiadomości SMS",
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.padding(10.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = place1,
            onValueChange = { viewModel.updatePlace1(it) },
            label = { Text("Numer telefonu")}
        )
        Text(text=place1)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = place1,
            onValueChange = { viewModel.updatePlace1(it) },
            label = { Text("Treść wiadomości")}
        )
        Text(text=place1)
    }
}