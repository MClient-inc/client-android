package ru.mclient.common.abonement.list

import ru.mclient.common.bar.MergedHost

interface AbonementsListHost : MergedHost {

    val abonementsList: AbonementsList

}