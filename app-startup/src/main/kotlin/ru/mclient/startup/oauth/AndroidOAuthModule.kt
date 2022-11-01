package ru.mclient.startup.oauth

import android.content.Context
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import ru.mclient.common.auth.oauth.OAuthRequest

@Module
class AndroidOAuthModule {

    @Factory
    fun bindAuthorizationService(context: Context): AuthorizationService {
        return AuthorizationService(
            context,
            AppAuthConfiguration.Builder()
                .setSkipIssuerHttpsCheck(true)
                .setConnectionBuilder(DebugConnectionBuilder).build()
        )
    }


    @Single
    fun bindOauthRequestInstance(service: AuthorizationService): OAuthRequest {
        return AndroidOAuthRequestInstance(service)
    }

}