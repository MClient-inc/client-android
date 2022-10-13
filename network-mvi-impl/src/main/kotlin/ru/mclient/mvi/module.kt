package ru.mclient.mvi

import org.koin.dsl.module
import org.koin.ksp.generated.module

val mviModule = module {
    includes(NetworkMVIModule().module)
}