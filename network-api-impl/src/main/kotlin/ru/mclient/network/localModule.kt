package ru.mclient.network

import org.koin.dsl.module
import org.koin.ksp.generated.module


val apiNetworkModule = module {
    includes(ApiNetworkModule().module)
}