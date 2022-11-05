package ru.mclient.common.staff.list

import ru.mclient.common.bar.MergedHost

interface StaffListHost : MergedHost {

    val list: StaffList

}