package ru.mclient.ui.company.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mclient.ui.view.DesignedButton
import ru.mclient.ui.view.DesignedTextField
import ru.mclient.ui.view.toDesignedString
import ru.shafran.ui.R

data class CompanyCreatePageState(
    val title: String,
    val description: String,
    val codename: String,
    val isLoading: Boolean,
    val error : String?
)

class CompanyCreatePageInput(
    val title: String,
    val description: String,
    val codename: String
)

fun CompanyCreatePageState.toInput(
    title: String = this.title,
    description: String = this.description,
    codename: String = this.codename
): CompanyCreatePageInput {
    return CompanyCreatePageInput(
        title = title,
        description = description,
        codename = codename
    )
}

@Composable
fun CompanyCreatePage(
    modifier: Modifier,
    state: CompanyCreatePageState,
    onUpdate: (CompanyCreatePageInput) -> Unit,
    onCreate: (CompanyCreatePageInput) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        DesignedTextField(
            value = state.codename,
            onValueChange = {
                onUpdate(state.toInput(codename = it))
            },
            label = stringResource(R.string.company_codename),
            singleLine = false,
            maxLines = 3,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedTextField(
            value = state.title,
            onValueChange = {
                onUpdate(state.toInput(title = it))
            },
            label = stringResource(R.string.company_title),
            singleLine = true,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedTextField(
            value = state.description,
            onValueChange = {
                onUpdate(state.toInput(description = it))
            },
            label = stringResource(R.string.company_description),
            singleLine = false,
            maxLines = 6,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        )
        DesignedButton(
            text = stringResource(R.string.company_registerButtonName).toDesignedString(),
            onClick = { onCreate(state.toInput()) }
        )
    }
}