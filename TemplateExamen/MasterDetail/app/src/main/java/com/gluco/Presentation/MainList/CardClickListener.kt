package com.gluco.Presentation.MainList

import com.gluco.Data.Local.TaskDomainModel

interface CardClickListener {
    fun onDeletedClicked(task: TaskDomainModel)
}