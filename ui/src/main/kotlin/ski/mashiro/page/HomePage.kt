package ski.mashiro.page

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ski.mashiro.component.MenuComponent
import ski.mashiro.router.Router

/**
 * @author mashirot
 * 2024/1/3 19:33
 */
@Composable
fun HomePage() {
    Row(modifier = Modifier.fillMaxSize()) {
        MenuComponent()
        Divider(
            modifier = Modifier.width(1.5.dp).fillMaxHeight(),
            color = Color.LightGray
        )
        Box(
            modifier = Modifier.padding(10.dp)
                .fillMaxSize()
//                .border(1.5.dp, color = Color.Green)
        ) {
            Router.navController = Router.initNavController()
        }
    }
}










