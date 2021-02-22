import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component.TabMenu
import theme.AppTheme



@ExperimentalMaterialApi
fun main() = Window {
    var isDarkMode by remember { mutableStateOf(false) }
    // Test labels: Microsoft Word Ribbon labels
    val tabLabels = listOf(
        "File",
        "Home",
        "Insert",
        "Layout",
        "References",
        "Review",
        "View",
        "Help"
    )

    AppTheme(isDarkMode) {
        Column(
            Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Switch Theme")
                Spacer(Modifier.width(8.dp))
                Button(onClick = { isDarkMode = !isDarkMode }) {
                    Text("To ${if (isDarkMode) "Light" else "Dark"} Theme")
                }
            }
            TabMenu(tabLabels) {
                Content(tabLabels[it])
            }
        }
    }
}

@Composable
fun Content(text: String) {
    Text("Component $text")
}