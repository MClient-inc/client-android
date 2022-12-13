package ru.mclient.ui.staff.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedOutlinedTitledBlock
import ru.shafran.ui.R

data class StaffProfileBlockState(
    val staff: Staff?,
) {
    data class Staff(
        val name: String,
        val codename: String,
        val role: String,
    )
}


@Composable
fun StaffProfileBlock(
    state: StaffProfileBlockState,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.staff != null)
        ClientProfileHeaderComponent(
            staff = state.staff,
            onEdit = onEdit,
            modifier = modifier
        )
}

@Composable
fun ClientProfileHeaderComponent(
    staff: StaffProfileBlockState.Staff,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DesignedOutlinedTitledBlock(title = "Профиль", modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            //TODO("crop icon")
            Icon(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "иконка",
                modifier = Modifier.size(75.dp)
            )
            Column {
                Text(
                    text = staff.name,
                    style = MaterialTheme.typography.headlineSmall,
                    overflow = TextOverflow.Ellipsis
                )
                if (staff.role.isNotBlank())
                    Text(
                        text = staff.role,
                        style = MaterialTheme.typography.labelSmall,
                        overflow = TextOverflow.Ellipsis
                    )
                Text(
                    text = staff.codename,
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun StaffProfileBlockPreview() {
    StaffProfileBlock(
        state = StaffProfileBlockState(
            staff = StaffProfileBlockState.Staff(
                name = "user user user",
                role = "Менеджер",
                codename = "user_4234234"
            ),
        ),
        onEdit = {},
        modifier = Modifier.fillMaxSize(),
    )
}