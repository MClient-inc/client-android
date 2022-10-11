package ru.mclient.ui.company

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mclient.common.company.Company


@Composable
fun CompanyUI(component: Company, modifier: Modifier) {
    CompanyProfilePage(modifier = modifier)
}