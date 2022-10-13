package ru.mclient.startup.oauth

import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.mclient.common.auth.oauth.OAuthRequest

val androidOAuthModule = module {
    factory {
        AuthorizationService(
            get(),
            AppAuthConfiguration.Builder()
                .setSkipIssuerHttpsCheck(true)
                .setConnectionBuilder(DebugConnectionBuilder).build()
        )
    }
    factory { AndroidOAuthRequestInstance(get()) } bind OAuthRequest::class
}