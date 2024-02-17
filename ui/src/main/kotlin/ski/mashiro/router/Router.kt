package ski.mashiro.router

import androidx.compose.runtime.Composable
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import ski.mashiro.view.*

/**
 * @author mashirot
 * 2024/1/3 20:55
 */
object Router {
    lateinit var navController: Navigator

    @Composable
    fun initNavController(): Navigator {
        val navController = rememberNavigator()
        NavHost(
            navigator = navController,
            navTransition = NavTransition(),
            initialRoute = "/home"
        ) {
            scene(
                route = "/home",
            ) {
                HomeView()
            }
            scene(
                route = "/manual_song_req",
            ) {
                ManualSongRequestView()
            }
            scene(
                route = "/setting",
            ) {
                SettingView()
            }
            scene(
                route = "/config",
            ) {
                ConfigView()
            }
            scene(
                route = "/info",
            ) {
                InfoView()
            }
        }
        return navController
    }
}