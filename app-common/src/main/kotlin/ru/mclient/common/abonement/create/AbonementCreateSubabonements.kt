package ru.mclient.common.abonement.create

data class AbonementCreateSubabonementsState(
    val subabonements: List<Subabonement>,
    val creation: Creation,
    val isSuccess: Boolean,
) {

    data class Subabonement(
        val title: String,
        val usages: Int,
        val cost: Long,
        val uniqueId: Int,
    )

    data class Creation(
        val title: String,
        val usages: Int,
        val cost: Long,
        val isAvailable: Boolean,
        val isButtonAvailable: Boolean,
    )

}

interface AbonementCreateSubabonements {

    val state: AbonementCreateSubabonementsState

    fun onCreate()

    fun onUpdate(title: String, usages: String, cost: String)

    fun onDelete(uniqueId: Int)

}