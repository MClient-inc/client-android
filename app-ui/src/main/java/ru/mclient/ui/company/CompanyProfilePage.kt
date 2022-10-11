package ru.mclient.ui.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedTextButton
import ru.shafran.ui.R

data class CompanyProfilePageState(
    val title: String,
    val codename: String,
    val description: String,
)


@Composable
fun CompanyProfilePage(
    state: CompanyProfilePageState,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CompanyProfileHeaderComponent(
            state = state,
            onEdit = onEdit,
            modifier = Modifier.fillMaxWidth()
        )
        CompanyProfileBodyComponent(modifier = Modifier.fillMaxSize())
    }

}

class MenuItem(
//    val icon: Int,
    val title: String,
)

val items by lazy {
    listOf(
        MenuItem(title = "Клиенты"),
        MenuItem(title = "Услуги"),
        MenuItem(title = "Работники"),
        MenuItem(title = "Сеть")
    )
}


@Preview
@Composable
fun CompanyProfilePagePreview() {
    CompanyProfilePage(
        state = CompanyProfilePageState(
            title = "Компания А",
            codename = "mycompany_1234",
            description = "это реп для коллег человек куллер говорит буль буль"
        ),
        onEdit = {

        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CompanyProfileHeaderComponent(
    state: CompanyProfilePageState,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.company),
            contentDescription = "иконка",
            modifier = Modifier.size(125.dp)
        )
        Column {
            Text(
                text = state.title,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = state.codename,
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(10.dp))
            DesignedTextButton(
                text = "Редактировать",
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProfileBodyComponent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        items.forEach {
            ListItem(
                headlineText = { Text(text = it.title) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}