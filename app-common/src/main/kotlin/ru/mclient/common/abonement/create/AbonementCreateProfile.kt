package ru.mclient.common.abonement.create


data class AbonementCreateProfileState(
    val title: String,
    val isSuccess: Boolean,
)

interface AbonementCreateProfile {

    val state: AbonementCreateProfileState

    fun onUpdate(title: String)

}