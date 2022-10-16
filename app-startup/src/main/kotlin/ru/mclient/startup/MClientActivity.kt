package ru.mclient.startup

import android.content.ComponentCallbacks
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.getKoin
import org.koin.core.component.getScopeId
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.scope.Scope
import ru.mclient.common.root.Root
import ru.mclient.common.root.RootComponent
import ru.mclient.common.withCoroutine
import ru.mclient.common.withDI
import ru.mclient.startup.theme.MClientTheme
import ru.mclient.ui.root.RootUI

fun AppCompatActivity.setupMainActivity() {
    val component = rootComponent()
    setupSplash(component)
    setContent(component)
}

fun AppCompatActivity.rootComponent(): Root {
    val scope = createActivityScope()
    return RootComponent(
        componentContext = defaultComponentContext().withCoroutine().withDI(scope)
    )
}

fun AppCompatActivity.setContent(component: Root) {
    setContent {
        MClientTheme(
            dynamicColor = false,
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                RootUI(component = component, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

fun AppCompatActivity.setupSplash(component: Root) {
    installSplashScreen().setKeepOnScreenCondition {
        component.isSplashShown
    }
}

fun AppCompatActivity.createActivityScope(): Scope {
    return getKoin().getScopeOrNull(getScopeId()) ?: createScopeForCurrentLifecycle(this)
}

private fun ComponentCallbacks.createScopeForCurrentLifecycle(owner: LifecycleOwner): Scope {
    val scope = getKoin().createScope(getScopeId(), TypeQualifier(AppCompatActivity::class), this)
    owner.registerScopeForLifecycle(scope)
    return scope
}

internal fun LifecycleOwner.registerScopeForLifecycle(
    scope: Scope
) {
    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                scope.close()
            }
        }
    )
}