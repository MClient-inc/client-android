package ru.mclient.ui.company

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import ru.mclient.common.company.Company
import ru.mclient.ui.company.profile.CompanyProfileHostUI
import ru.mclient.ui.staff.create.StaffCreateHostUI
import ru.mclient.ui.staff.list.StaffListHostUI
import ru.mclient.ui.staff.profile.StaffProfileHostUI


@Composable
fun CompanyUI(component: Company, modifier: Modifier) {
    Children(stack = component.childStack) {
        CompanyNavHost(child = it.instance, modifier = modifier)
    }
}

@Composable
fun CompanyNavHost(child: Company.Child, modifier: Modifier) {
    when (child) {
        is Company.Child.CompanyProfile ->
            CompanyProfileHostUI(
                component = child.component,
                modifier = modifier
            )

        is Company.Child.StaffList ->
            StaffListHostUI(
                component = child.component,
                modifier = modifier,
            )

        is Company.Child.StaffProfile ->
            StaffProfileHostUI(
                component = child.component,
                modifier = modifier,
            )

        is Company.Child.StaffCreate ->
            StaffCreateHostUI(
                component = child.component,
                modifier = modifier,
            )
    }
}