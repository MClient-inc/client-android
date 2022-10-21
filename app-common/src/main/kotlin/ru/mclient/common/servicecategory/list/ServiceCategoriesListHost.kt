package ru.mclient.common.servicecategory.list

import ru.mclient.common.bar.TopBarHost

interface ServiceCategoriesListHost : TopBarHost {

    val list: ServiceCategoriesList

}