package ru.mclient.ui.company.staff.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.shafran.ui.R

data class StaffProfilePageState(
    val username: String,
    val codename: String
)


@Composable
fun StaffProfilePage(
    modifier: Modifier,
    state: StaffProfilePageState
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(30.dp)) {
        Icon(painter = painterResource(id = R.drawable.user), contentDescription = "userIcon", modifier = Modifier.size(150.dp))
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Text(text = state.codename, fontSize = 25.sp)
            Text(text = state.username, fontSize = 25.sp)
        }
    }
}

@Preview
@Composable
fun testt() {
    StaffProfilePage(
        modifier = Modifier.fillMaxWidth(), state = StaffProfilePageState(
            username = "Вайтенко Генадий Акакиевич",
            codename = "дрочила-1337"
        )
    )
}