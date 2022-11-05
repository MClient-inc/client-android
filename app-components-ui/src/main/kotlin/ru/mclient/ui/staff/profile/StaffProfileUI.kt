package ru.mclient.ui.staff.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalTime
import ru.mclient.common.staff.profile.StaffProfile
import ru.mclient.common.staff.profile.StaffProfileState
import ru.mclient.ui.view.DesignedRefreshColumn

@Composable
fun StaffProfileUI(
    component: StaffProfile,
    modifier: Modifier,
) {
    val state = component.state
    DesignedRefreshColumn(
        refreshing = state.isRefreshing,
        onRefresh = component::onRefresh,
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StaffProfileBlock(
            state = component.state.toStaffBlockUI(),
            onEdit = component::onEditProfile,
            modifier = Modifier.fillMaxWidth(),
        )
        StaffScheduleBlock(
            state = component.state.toStaffScheduleUI(),
            onEdit = component::onEditSchedule,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


fun StaffProfileState.toStaffBlockUI(): StaffProfileBlockState {
    return StaffProfileBlockState(
        staff = staff?.toStaffBlockUI(),
    )
}


fun StaffProfileState.toStaffScheduleUI(): StaffScheduleBlockState {
    return StaffScheduleBlockState(
        schedule = schedule.takeIf { !isLoading || isRefreshing }?.map {
            StaffScheduleBlockState.Schedule(
                date = it.date.toKotlinLocalDate(),
                start = it.start.toKotlinLocalTime(),
                end = it.end.toKotlinLocalTime(),
            )
        }
    )
}


fun StaffProfileState.Staff.toStaffBlockUI(): StaffProfileBlockState.Staff {
    return StaffProfileBlockState.Staff(name, codename, role)
}