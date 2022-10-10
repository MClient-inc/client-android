package ru.mclient.ui.staff.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedTextButton
import ru.shafran.ui.R

data class StaffProfilePageState(
    val name: String,
    val codename: String
)


@Composable
fun StaffProfilePage(
    state: StaffProfilePageState,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //TODO("crop icon")
        Icon(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "иконка",
            modifier = Modifier.size(125.dp)
        )
        Column {
            Text(text = state.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = state.codename, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(10.dp))
            DesignedTextButton(
                text = "Редактировать",
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun StaffProfilePagePreview() {
    StaffProfilePage(
        state = StaffProfilePageState(
            name = "user user user",
            codename = "user_4234234",
        ),
        onEdit = {},
        modifier = Modifier.fillMaxWidth()
    )
}