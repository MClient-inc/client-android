package ru.mclient.ui.company

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import ru.mclient.common.company.Company
import ru.mclient.ui.LocalChildrenStackAnimator
import ru.mclient.ui.company.profile.CompanyProfileHostUI
import ru.mclient.ui.companynetwork.profile.CompanyNetworkProfileHostUI
import ru.mclient.ui.service.list.ServiceListHostUI
import ru.mclient.ui.servicecategory.create.ServiceCategoryCreateHostUI
import ru.mclient.ui.servicecategory.list.ServiceCategoriesListHostUI
import ru.mclient.ui.staff.create.StaffCreateHostUI
import ru.mclient.ui.staff.list.StaffListHostUI
import ru.mclient.ui.staff.profile.StaffProfileHostUI


@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun CompanyUI(component: Company, modifier: Modifier) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(LocalChildrenStackAnimator.current),
    ) {
        CompanyNavHost(child = it.instance, modifier = modifier)
    }
}

@Composable
fun CompanyNavHost(child: Company.Child, modifier: Modifier) {
    when (child) {
        is Company.Child.CompanyProfile ->
            CompanyProfileHostUI(
                component = child.component,
                modifier = modifier,
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

        is Company.Child.CompanyNetwork ->
            CompanyNetworkProfileHostUI(
                component = child.component,
                modifier = modifier,
            )

        is Company.Child.ServiceCategoriesList ->
            ServiceCategoriesListHostUI(
                component = child.component,
                modifier = modifier,
            )

        is Company.Child.ServiceList ->
            ServiceListHostUI(
                component = child.component,
                modifier = modifier,
            )

        is Company.Child.ServiceCategoryCreate ->
            ServiceCategoryCreateHostUI(
                component = child.component,
                modifier = modifier,
            )
    }
}