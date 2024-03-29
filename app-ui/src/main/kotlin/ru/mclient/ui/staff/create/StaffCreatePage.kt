package ru.mclient.ui.staff.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedTextField
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class StaffCreatePageState(
    val username: String,
    val codename: String,
    val role: String,
    val isLoading: Boolean,
    val error: String?,
    val isButtonsEnabled: Boolean
)

class StaffCreatePageInput(
    val username: String,
    val codename: String,
    val role: String
)

fun StaffCreatePageState.toInput(
    username: String = this.username,
    codename: String = this.codename,
    role: String = this.role,
): StaffCreatePageInput {
    return StaffCreatePageInput(
        username = username,
        codename = codename,
        role = role
    )
}


@Composable
fun StaffCreatePage(
    modifier: Modifier,
    state: StaffCreatePageState,
    onUpdate: (StaffCreatePageInput) -> Unit,
    onCreate: (StaffCreatePageInput) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
//        codename
        DesignedTextField(
            value = state.codename,
            onValueChange = {
                onUpdate(state.toInput(codename = it))
            },
            label = stringResource(R.string.company_staff_codename),
            maxLines = 1,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

//        username
        DesignedTextField(
            value = state.username,
            onValueChange = {
                onUpdate(state.toInput(username = it))
            },
            label = stringResource(R.string.company_staff_name),
            maxLines = 1,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

//        role
        DesignedTextField(
            value = state.role,
            onValueChange = {
                onUpdate(state.toInput(role = it))
            },
            label = stringResource(R.string.company_staff_role),
            maxLines = 1,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

//        create Staff
        DesignedButton(
            text = R.string.company_staff_register_new.toDesignedString(),
            enabled = state.isButtonsEnabled,
            onClick = { onCreate(state.toInput()) }
        )

    }
}

@Preview
@Composable
fun StaffCreatePagePreview() {
    var state by remember {
        mutableStateOf(
            StaffCreatePageState(
                username = "",
                codename = "",
                role = "",
                isLoading = false,
                error = null,
                isButtonsEnabled = true
            )
        )
    }

    StaffCreatePage(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onUpdate = {
            state = state.copy(username = it.username, codename = it.codename, role = it.role)
        },
        onCreate = {

        }
    )
}