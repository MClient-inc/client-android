package ru.mclient.common.home

import ru.mclient.common.bar.TopBarHost
import ru.mclient.common.record.upcoming.UpcomingRecords

interface Home : TopBarHost {

    val upcomingRecords: UpcomingRecords

}