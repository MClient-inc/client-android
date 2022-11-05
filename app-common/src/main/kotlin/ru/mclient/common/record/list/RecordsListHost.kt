package ru.mclient.common.record.list

import ru.mclient.common.bar.TopBarHost

interface RecordsListHost : TopBarHost {

    val recordsList: RecordsList

    fun onRecordCreate()

}