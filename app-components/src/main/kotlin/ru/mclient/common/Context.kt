package ru.mclient.common

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope
import ru.mclient.common.utils.createCoroutineScope
import kotlin.reflect.KClass

interface CoroutineComponentContext : ComponentContext {

    val componentScope: CoroutineScope

}

private class DefaultCoroutineComponentContext(
    componentContext: ComponentContext,
) : CoroutineComponentContext, ComponentContext by componentContext {

    override val componentScope = createCoroutineScope()

}

interface DIComponentContext : CoroutineComponentContext, KoinScopeComponent

private class DefaultDIComponentContext(
    componentContext: CoroutineComponentContext,
    override val scope: Scope
) : DIComponentContext, CoroutineComponentContext by componentContext

fun CoroutineComponentContext.withDI(scope: Scope): DIComponentContext {
    return DefaultDIComponentContext(this, scope)
}

fun ComponentContext.withCoroutine(): CoroutineComponentContext {
    return DefaultCoroutineComponentContext(this)
}

fun CoroutineComponentContext.childCoroutineComponentContext(
    key: String,
    lifecycle: Lifecycle? = null
): CoroutineComponentContext {
    return childContext(key, lifecycle).withCoroutine()
}

fun DIComponentContext.childDIContext(
    key: String,
    lifecycle: Lifecycle? = null
): DIComponentContext {
    return childCoroutineComponentContext(key, lifecycle).withDI(scope)
}


inline fun <C : Parcelable, T : Any> CoroutineComponentContext.androidChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    configurationClass: KClass<out C>,
    key: String = "DefaultChildStack",
    handleBackButton: Boolean = false,
    crossinline childFactory: (configuration: C, CoroutineComponentContext) -> T
): Value<ChildStack<C, T>> {
    return childStack(
        source,
        initialStack,
        configurationClass,
        key,
        handleBackButton
    ) { configuration, componentContext ->
        childFactory(configuration, componentContext.withCoroutine())
    }
}

inline fun <reified C : Parcelable, T : Any> CoroutineComponentContext.androidChildStack(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, CoroutineComponentContext) -> T
): Value<ChildStack<C, T>> =
    androidChildStack(
        source = source,
        initialStack = initialStack,
        configurationClass = C::class,
        key = key,
        handleBackButton = handleBackButton,
        childFactory = childFactory,
    )

inline fun <reified C : Parcelable, T : Any> CoroutineComponentContext.androidChildStack(
    source: StackNavigationSource<C>,
    initialConfiguration: C,
    key: String = "DefaultRouter",
    handleBackButton: Boolean = false,
    noinline childFactory: (configuration: C, CoroutineComponentContext) -> T
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
