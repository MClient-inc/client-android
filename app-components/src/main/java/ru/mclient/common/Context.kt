package ru.mclient.common

import androidx.activity.ComponentActivity
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.parcelable.Parcelable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope
import kotlin.reflect.KClass

interface ActivityComponentContext : ComponentContext {
    val activity: ComponentActivity
}

private class DefaultActivityComponentContext(
    componentContext: ComponentContext,
    override val activity: ComponentActivity
) :
    ActivityComponentContext, ComponentContext by componentContext {
}

interface DIComponentContext : ActivityComponentContext, KoinScopeComponent

private class DefaultDIComponentContext(
    componentContext: ActivityComponentContext,
    override val scope: Scope
) : DIComponentContext, ActivityComponentContext by componentContext

fun ActivityComponentContext.withDI(scope: Scope): DIComponentContext {
    return DefaultDIComponentContext(this, scope)
}

fun ComponentContext.withActivity(activity: ComponentActivity): ActivityComponentContext {
    return DefaultActivityComponentContext(this, activity)
}

fun ActivityComponentContext.childAndroidComponentContext(
    key: String,
    lifecycle: Lifecycle? = null
): ActivityComponentContext {
    return childContext(key, lifecycle).withActivity(activity)
}

fun DIComponentContext.childKodeinContext(
    key: String,
    lifecycle: Lifecycle? = null
): DIComponentContext {
    return childAndroidComponentContext(key, lifecycle).withDI(scope)
}


inline fun <C : Parcelable, T : Any> ActivityComponentContext.androidChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    configurationClass: KClass<out C>,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    crossinline childFactory: (configuration: C, ActivityComponentContext) -> T
): Value<ChildStack<C, T>> {
    return childStack(
        source,
        initialStack,
        configurationClass,
        key,
        handleBackButton
    ) { configuration, componentContext ->
        childFactory(configuration, componentContext.withActivity(activity))
    }
}

inline fun <reified C : Parcelable, T : Any> ActivityComponentContext.androidChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, ActivityComponentContext) -> T
): Value<ChildStack<C, T>> =
    androidChildStack(
        source = source,
        initialStack = initialStack,
        configurationClass = C::class,
        key = key,
        handleBackButton = handleBackButton,
        childFactory = childFactory,
    )

inline fun <reified C : Parcelable, T : Any> ActivityComponentContext.androidChildStack(
    source: StackNavigationSource<C>,
    initialConfiguration: C,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, ActivityComponentContext) -> T
): Value<ChildStack<C, T>> =
    androidChildStack(
        source = source,
        initialStack = { listOf(initialConfiguration) },
        configurationClass = C::class,
        key = key,
        handleBackButton = handleBackButton,
        childFactory = childFactory,
    )



inline fun <C : Parcelable, T : Any> DIComponentContext.diChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    configurationClass: KClass<out C>,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    crossinline childFactory: (configuration: C, DIComponentContext) -> T
): Value<ChildStack<C, T>> {
    return androidChildStack(
        source,
        initialStack,
        configurationClass,
        key,
        handleBackButton
    ) { configuration, componentContext ->
        childFactory(configuration, componentContext.withDI(scope))
    }
}

inline fun <reified C : Parcelable, T : Any> DIComponentContext.diChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, DIComponentContext) -> T
): Value<ChildStack<C, T>> =
    diChildStack(
        source = source,
        initialStack = initialStack,
        configurationClass = C::class,
        key = key,
        handleBackButton = handleBackButton,
        childFactory = childFactory,
    )

inline fun <reified C : Parcelable, T : Any> DIComponentContext.diChildStack(
    source: StackNavigationSource<C>,
    initialConfiguration: C,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, DIComponentContext) -> T
): Value<ChildStack<C, T>> =
    diChildStack(
        source = source,
        initialStack = { listOf(initialConfiguration) },
        configurationClass = C::class,
        key = key,
        handleBackButton = handleBackButton,
        childFactory = childFactory,
    )
