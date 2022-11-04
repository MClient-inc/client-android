package ru.mclient.common.record.list

class RecordsListState(
    val records: List<Record>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val isFailure: Boolean,
) {

    class Record(
        val id: Long,
        val client: Client,
        val staff: Staff,
        val services: List<Service>,
        val cost: Long,
        val formattedCost: String,
        val formattedTime: String,
    )

    class Client(
        val name: String,
        val phone: String,
        val formattedPhone: String,
    )

    class Staff(
        val name: String,
    )

    class Service(
        val title: String,
    )

}

interface RecordsList {

    val state: RecordsListState

    fun onRefresh()

    fun onSelect(recordId: Long)

}