package ru.mclient.common.servicecategory.list

import ru.mclient.common.bar.MergedHost

interface ServiceCategoriesListHost : MergedHost {

    val list: ServiceCategoriesList

}