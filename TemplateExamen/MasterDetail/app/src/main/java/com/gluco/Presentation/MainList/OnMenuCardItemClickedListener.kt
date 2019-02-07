package com.gluco.Presentation.MainList

import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Local.TaskDomainModel

interface OnMenuCardItemClickedListener {
    fun onEditClicked(entry: TaskDomainModel)
    fun onDeleteClicked(entry: TaskDomainModel)
}