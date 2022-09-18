package ru.mclient.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import io.ktor.client.*
import io.ktor.client.engine.android.*
import ru.mclient.app.ui.login.LoginComponentUI
import ru.mclient.app.ui.register.RegisterComponentUI
import ru.mclient.app.ui.theme.MClientTheme
import ru.mclient.common.login.WorkingLoginComponent
import ru.mclient.common.register.WorkingRegisterComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val loginComponent = WorkingLoginComponent(
//            componentContext = defaultComponentContext(),
//            client = HttpClient(Android)
//        )

        val registerComponent = WorkingRegisterComponent(
            componentContext = defaultComponentContext(),
            client = HttpClient(Android)
        )

        setContent {
            MClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterComponentUI(
                        component = registerComponent,
                        modifier = Modifier.fillMaxWidth()
                    )
//                    LoginComponentUI(
//                        component = loginComponent,
//                        modifier = Modifier.fillMaxWidth(),
//                    )
                }
            }
        }
    }
}