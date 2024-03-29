package ru.mclient.mvi.record.create

import ru.mclient.mvi.ParametrizedStore
import java.time.LocalDateTime

interface RecordCreateStore :
    ParametrizedStore<RecordCreateStore.Intent, RecordCreateStore.State, RecordCreateStore.Label, RecordCreateStore.Params> {

    data class Params(
        val companyId: Long,
    )

    sealed class Intent {

        class Create(
            val clientId: Long,
            val staffId: Long,
            val dateTime: LocalDateTime,
            val servicesIds: List<Long>,
        ) : Intent()

    }

    data class State(
        val isAvailable: Boolean,
        val isLoading: Boolean,
        val isSuccess: Boolean,
    )

    sealed class Label

}