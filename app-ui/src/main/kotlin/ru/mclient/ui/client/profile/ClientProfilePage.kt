package ru.mclient.ui.client.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedIcon
import ru.mclient.ui.view.DesignedRefreshColumn
import ru.mclient.ui.view.DesignedString
import ru.mclient.ui.view.DesignedText
import ru.mclient.ui.view.outlined
import ru.mclient.ui.view.toDesignedDrawable
import ru.mclient.ui.view.toDesignedString
import ru.mclient.ui.view.toStringComposable
import ru.shafran.ui.R

data class ClientProfilePageState(
    val profile: Profile?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    data class Profile(
        val username: DesignedString,
        val codename: DesignedString,
        val phoneNumber: DesignedString,
    )
}

@Composable
fun ClientProfilePage(
    state: ClientProfilePageState,
    onRefresh: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (state.profile != null)
            ClientProfileHeaderComponent(
                profile = state.profile,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .outlined()
                    .padding(10.dp)
            )
    }
}

fun phoneNumberFormatter(phoneNumber: String): DesignedString {
    val formatterNumber = StringBuilder().append(phoneNumber)
    if (phoneNumber.length == 11) {
        if (phoneNumber.startsWith("7")) {
            formatterNumber.insert(0, "+")
            formatterNumber.insert(2, "(")
            formatterNumber.insert(6, ")")
            formatterNumber.insert(10, "-")
            formatterNumber.insert(13, "-")
        }
    }
    return formatterNumber.toString().toDesignedString()
}

@Composable
fun ClientProfileHeaderComponent(
    profile: ClientProfilePageState.Profile,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DesignedIcon(
            icon = painterResource(id = R.drawable.user).toDesignedDrawable(),
            modifier = Modifier.size(125.dp)
        )
        Column {
            DesignedText(
                text = profile.username,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            DesignedText(
                text = profile.codename,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            DesignedText(
                text = phoneNumberFormatter(profile.phoneNumber.toStringComposable()),
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(10.dp))
            DesignedButton(
                text = "Редактировать".toDesignedString(),
                onClick = onEdit,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun ClientProfilePagePreview() {
    ClientProfilePage(
        state = ClientProfilePageState(
            profile = ClientProfilePageState.Profile(
                username = "Александр Сергеевич Пушкин".toDesignedString(),
                codename = "client_007".toDesignedString(),
                phoneNumber = "79133781440".toDesignedString()
            ),
            isLoading = false,
            isRefreshing = false
        ),
        onRefresh = {},
        onEdit = {},
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    )
}