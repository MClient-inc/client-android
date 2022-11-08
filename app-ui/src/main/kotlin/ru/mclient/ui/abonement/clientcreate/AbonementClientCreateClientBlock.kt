package ru.mclient.ui.abonement.clientcreate

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.record.profile.toPhoneFormat
import ru.mclient.ui.view.outlined
import ru.shafran.ui.R

class AbonementClientCreateClientBlockState(
    val client: Client?,
    val isLoading: Boolean,
) {
    class Client(
        val name: String,
        val phone: String,
    )
}

@Composable
fun AbonementClientCreateClientBlock(
    state: AbonementClientCreateClientBlockState,
    modifier: Modifier,
) {
    if (state.client != null)
        AbonementClientCreateClientBlockHeader(
            client = state.client,
            modifier = modifier,
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbonementClientCreateClientBlockHeader(
    client: AbonementClientCreateClientBlockState.Client,
    modifier: Modifier,
) {
    ListItem(
        headlineText = {
            Text(client.name)
        },
        supportingText = {
            if (client.phone.isNotBlank())
                Text(client.phone.toPhoneFormat())
        },
        leadingContent = {
            Icon(
                painterResource(id = R.drawable.client),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        },
        modifier = modifier.outlined()
    )
}