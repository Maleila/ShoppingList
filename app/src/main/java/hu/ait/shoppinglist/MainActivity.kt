package hu.ait.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.shoppinglist.screen.ShoppingListScreen
import hu.ait.shoppinglist.screen.SplashScreen
import hu.ait.shoppinglist.ui.theme.ShoppingListTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   ShoppingAppNavHost()
                }
            }
        }
    }
}

@Composable
fun ShoppingAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = stringResource(R.string.splashscreen_route)
) {
    var context = LocalContext.current

    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(context.getString(R.string.splashscreen_route)) { SplashScreen(
            onNavigateToMain = { -> navController.navigate(context.getString(R.string.shoppinglist_route))}
        )}
        composable(context.getString(R.string.shoppinglist_route)) { ShoppingListScreen() }
    }
}